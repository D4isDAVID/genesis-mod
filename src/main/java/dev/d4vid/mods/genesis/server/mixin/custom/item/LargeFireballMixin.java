package dev.d4vid.mods.genesis.server.mixin.custom.item;

import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LargeFireball.class)
public class LargeFireballMixin {
    @Inject(method = "onHit", at = @At("HEAD"), cancellable = true)
    private void genesis$onHit(HitResult result, CallbackInfo info) {
        if (result.getType() != HitResult.Type.BLOCK) return;
        LargeFireball self = (LargeFireball)(Object) this;
        if (self.getTags().contains("genesis_ghast_fireball")) {
            info.cancel();
        }
    }
}
