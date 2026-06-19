package dev.d4vid.mods.genesis.server.mixin.totem;

import dev.d4vid.mods.genesis.server.GenesisConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(LivingEntity.class)
class LivingEntityMixin {
    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void genesis$checkTotemDeathProtection(DamageSource source, CallbackInfoReturnable<Boolean> callback) {
        if (GenesisConfig.INSTANCE.isTotemDeathProtectionDisabled()) {
            callback.setReturnValue(false);
        }
    }
}
