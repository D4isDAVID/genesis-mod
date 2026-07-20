package dev.d4vid.mods.genesis.server.mixin.spoof;

import dev.d4vid.mods.genesis.server.event.GenesisPacketSpoofEvents;
import net.minecraft.network.syncher.SyncedDataHolder;
import net.minecraft.network.syncher.SynchedEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(SynchedEntityData.class)
public class SynchedEntityDataMixin {
    @Shadow
    private SyncedDataHolder entity;
    @Shadow
    private SynchedEntityData.DataItem<?>[] itemsById;

    @Inject(method = "packDirty", at = @At("RETURN"), cancellable = true)
    private void genesis$packDirty(CallbackInfoReturnable<List<SynchedEntityData.DataValue<?>>> callback) {
        List<SynchedEntityData.DataValue<?>> list = callback.getReturnValue();
        if (itemsById.length < 10 || list == null) {
            return;
        }

        SynchedEntityData.DataItem<?> healthItem = itemsById[9];
        boolean result = GenesisPacketSpoofEvents.INSTANCE.getALLOW_ENTITY_HEALTH_SYNC().invoker().allowEntityHealthSync(entity);

        if (!result) {
            list.remove(healthItem.value());
        }
    }
}
