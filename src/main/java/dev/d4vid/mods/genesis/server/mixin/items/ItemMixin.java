package dev.d4vid.mods.genesis.server.mixin.items;

import dev.d4vid.mods.genesis.server.event.GenesisItemEvents;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "getDefaultMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void genesis$getDefaultMaxStackSize(CallbackInfoReturnable<Integer> callback) {
        Item item = (Item) (Object) this;
        Integer result = GenesisItemEvents.INSTANCE.getMODIFY_DEFAULT_MAX_STACK_SIZE().invoker().modifyDefaultMaxStackSize(item.getDefaultInstance());

        if (result != null) {
            callback.setReturnValue(result);
        }
    }
}
