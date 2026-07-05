package dev.d4vid.mods.genesis.server.mixin.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "hasAnyEnchantments", at = @At("HEAD"), cancellable = true)
    private static void genesis$hasAnyEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        GenesisItem item = GenesisItems.get(stack);
        if (item != null && !item.canBeEnchanted()) {
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "canStoreEnchantments", at = @At("HEAD"), cancellable = true)
    private static void genesis$canStoreEnchantments(ItemStack stack, CallbackInfoReturnable<Boolean> callback) {
        if (GenesisItems.is(stack)) {
            callback.setReturnValue(false);
        }
    }

}
