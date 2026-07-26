package dev.d4vid.mods.genesis.server.mobgriefing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("unused")
@Mixin(ServerLevel.class)
public class ExplosionMixin {
    @ModifyVariable(method = "explode", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Level.ExplosionInteraction genesis$noCreeperBlockDamage(
        Level.ExplosionInteraction interaction,
        Entity entity,
        DamageSource damageSource,
        ExplosionDamageCalculator damageCalculator,
        double x,
        double y,
        double z,
        float radius,
        boolean fire
    ) {
        if (entity instanceof Creeper) {
            return Level.ExplosionInteraction.NONE;
        }

        return interaction;
    }
}
