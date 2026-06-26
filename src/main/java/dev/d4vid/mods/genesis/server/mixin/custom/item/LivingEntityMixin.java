package dev.d4vid.mods.genesis.server.mixin.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    private void genesis$causeFallDamage(double fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }

        boolean result = GenesisCustomItemEvents.INSTANCE.getALLOW_PLAYER_FALL_DAMAGE().invoker().allowPlayerFallDamage(player, fallDistance, multiplier, source);

        if (!result) {
            info.setReturnValue(false);
        }
    }
}
