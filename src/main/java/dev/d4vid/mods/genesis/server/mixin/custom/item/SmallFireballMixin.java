package dev.d4vid.mods.genesis.server.mixin.custom.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmallFireball.class)
public class SmallFireballMixin {
    @Inject(method = "onHitBlock", at = @At("HEAD"), cancellable = true)
    private void genesis$onHitBlock(BlockHitResult result, CallbackInfo info) {
        SmallFireball self = (SmallFireball)(Object) this;
        if (self.getTags().contains("genesis_ghast_fireball")) {
            info.cancel();
        }
    }
}
