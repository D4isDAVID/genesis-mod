package dev.d4vid.mods.genesis.server.mixin.items;

import dev.d4vid.mods.genesis.server.custom.item.DragonWingsItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import dev.d4vid.mods.genesis.server.custom.item.WingsItem;
import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(AbstractContainerMenu.class)
public abstract class ContainerMenuMixin {

    @Shadow
    public abstract ItemStack getCarried();

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void genesis$doClick(int slotId, int button, ClickType clickType, Player player, CallbackInfo info) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
        ItemStack carried = getCarried();

        ItemStack inHand = getCarried();
        if (inHand.getItem() == Items.ELYTRA) {
            ItemStack chest = serverPlayer.getItemBySlot(EquipmentSlot.CHEST);
            if (!(GenesisItems.get(chest) instanceof DragonWingsItem || GenesisItems.get(chest) instanceof WingsItem)) {
                info.cancel();
                return;
            }
        }

        boolean validSlot = slotId >= 0 && slotId < menu.slots.size();
        ItemStack slotItem = validSlot ? menu.slots.get(slotId).getItem() : ItemStack.EMPTY;

        boolean isDropAction = clickType == ClickType.THROW || slotId == -999;

        if (isDropAction) {
            ItemStack dropStack = slotId == -999 ? carried : slotItem;
            if (UltimateManager.isUltimate(dropStack)) {
                info.cancel();
                serverPlayer.containerMenu.broadcastFullState();
                return;
            }
        }

        if (genesis$isUltimateBundleInteraction(carried, slotItem)) {
            info.cancel();
            return;
        }

        boolean isPlayerInventorySlot = validSlot && menu.slots.get(slotId).container == serverPlayer.getInventory();

        boolean hasForeignContainer = false;
        for (Slot slot : menu.slots) {
            if (slot.container != serverPlayer.getInventory()) {
                hasForeignContainer = true;
                break;
            }
        }

        if (!hasForeignContainer) return;

        if (!isPlayerInventorySlot) {
            if (UltimateManager.isUltimate(carried)) {
                info.cancel();
                return;
            }

            if (clickType == ClickType.SWAP && button >= 0 && button < 9) {
                ItemStack hotbarStack = serverPlayer.getInventory().getItem(button);
                if (UltimateManager.isUltimate(hotbarStack)) {
                    info.cancel();
                }
            }

            return;
        }

        if (clickType == ClickType.QUICK_MOVE && UltimateManager.isUltimate(slotItem)) {
            info.cancel();
        }
    }

    @Unique
    private boolean genesis$isUltimateBundleInteraction(ItemStack carried, ItemStack slotItem) {
        boolean carriedIsUltimate = UltimateManager.isUltimate(carried);
        boolean slotIsUltimate = UltimateManager.isUltimate(slotItem);
        boolean carriedIsBundle = carried.getItem() instanceof BundleItem;
        boolean slotIsBundle = slotItem.getItem() instanceof BundleItem;

        return (carriedIsUltimate && slotIsBundle) || (slotIsUltimate && carriedIsBundle);
    }
}
