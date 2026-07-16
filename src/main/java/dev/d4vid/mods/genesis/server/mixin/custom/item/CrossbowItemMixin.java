package dev.d4vid.mods.genesis.server.mixin.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import dev.d4vid.mods.genesis.server.custom.item.MobCrossbowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    @Inject(method = "getChargeDuration", at = @At("HEAD"), cancellable = true)
    private static void genesis$getChargeDuration(ItemStack stack, LivingEntity entity, CallbackInfoReturnable<Integer> callback) {
        GenesisItem item = GenesisItems.get(stack);
        if (item instanceof MobCrossbowItem crossbow && crossbow.isGhastMode(stack)) {
            callback.setReturnValue(100);
        }
    }
}
