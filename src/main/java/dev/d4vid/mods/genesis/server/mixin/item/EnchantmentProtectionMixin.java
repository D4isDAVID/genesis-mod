package dev.d4vid.mods.genesis.server.mixin.item;

import dev.d4vid.mods.genesis.server.item.CustomItem;
import dev.d4vid.mods.genesis.server.item.CustomItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import org.apache.logging.log4j.core.jmx.Server;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentMenu.class)
public class EnchantmentProtectionMixin {

    @Inject(method = "clickMenuButton", at = @At("HEAD"),cancellable = true)
    private void genesis$clickMenuButton(Player player, int i, CallbackInfoReturnable<Boolean> info) {
        EnchantmentMenu menu = (EnchantmentMenu) (Object) this;
        ItemStack input = menu.getSlot(0).getItem();

        if (CustomItems.isCustomItem(input)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }
}
