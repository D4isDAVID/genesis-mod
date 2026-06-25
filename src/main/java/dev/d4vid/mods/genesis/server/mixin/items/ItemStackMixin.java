package dev.d4vid.mods.genesis.server.mixin.items;

import dev.d4vid.mods.genesis.server.event.GenesisItemEvents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void genesis$getMaxStackSize(CallbackInfoReturnable<Integer> callback) {
        ItemStack item = (ItemStack) (Object) this;
        Integer result = GenesisItemEvents.INSTANCE.getMODIFY_DEFAULT_MAX_STACK_SIZE().invoker().modifyDefaultMaxStackSize(item);

        if (result != null && item.getCount() <= result) {
            callback.setReturnValue(result);
        }
    }
}
