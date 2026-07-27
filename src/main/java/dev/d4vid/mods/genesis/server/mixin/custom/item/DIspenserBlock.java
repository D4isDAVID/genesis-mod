package dev.d4vid.mods.genesis.server.mixin.custom.item;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.core.dispenser.EquipmentDispenseItemBehavior.class)
public class DIspenserBlock {
    @Inject(method = "dispenseStack", at = @At("HEAD"), cancellable = true)
    private void genesis$blockElytraDispense(
        BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> info
    ) {
        if (stack.getItem() == Items.ELYTRA) {
            info.cancel();
            info.setReturnValue(stack);
        }
    }
}
