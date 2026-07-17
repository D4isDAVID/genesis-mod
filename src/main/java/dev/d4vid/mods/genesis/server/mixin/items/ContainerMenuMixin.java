package dev.d4vid.mods.genesis.server.mixin.items;

import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

        AbstractContainerMenu menu = (AbstractContainerMenu)(Object) this;

        boolean isPlayerInventory = slotId >= 0
            && slotId < menu.slots.size()
            && menu.slots.get(slotId).container == serverPlayer.getInventory();

        if (isPlayerInventory) return;

        ItemStack carried = getCarried();
        if (UltimateManager.isUltimate(carried)) {
            info.cancel();
            return;
        }

        if (slotId >= 0 && slotId < menu.slots.size()) {
            ItemStack slotItem = menu.slots.get(slotId).getItem();
            if (UltimateManager.isUltimate(slotItem)) {
                info.cancel();
            }
        }
    }
}
