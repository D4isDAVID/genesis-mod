package dev.d4vid.mods.genesis.server.mixin.spoof;

import dev.d4vid.mods.genesis.server.event.GenesisPacketSpoofEvents;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(SynchedEntityData.DataItem.class)
public class SynchedEntityDataDataItemMixin {
    @Inject(method = "isDirty", at = @At("HEAD"), cancellable = true)
    private void genesis$isDirty(CallbackInfoReturnable<Boolean> callback) {
        SynchedEntityData.DataItem<?> item = (SynchedEntityData.DataItem<?>) (Object) this;

        if (item.value().id() == 9) {
            boolean result = GenesisPacketSpoofEvents.INSTANCE.getALLOW_ENTITY_HEALTH_SYNC().invoker().allowEntityHealthSync();

            if (!result) {
                callback.setReturnValue(false);
            }
        }
    }
}
