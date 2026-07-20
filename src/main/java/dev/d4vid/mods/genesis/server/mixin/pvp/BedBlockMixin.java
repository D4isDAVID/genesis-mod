package dev.d4vid.mods.genesis.server.mixin.pvp;

import com.llamalad7.mixinextras.sugar.Local;
import dev.d4vid.mods.genesis.server.event.GenesisCombatEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unused")
@Mixin(BedBlock.class)
public class BedBlockMixin {
    @ModifyArg(
        method = "useWithoutItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;Lnet/minecraft/world/phys/Vec3;FZLnet/minecraft/world/level/Level$ExplosionInteraction;)V"
        ),
        index = 4
    )
    private float genesis$useWithoutItem(float radius, @Local Level level) {
        if (level.dimension() == Level.END) {
            return 0.0F;
        }

        Float result = GenesisCombatEvents.INSTANCE.getMODIFY_BED_EXPLOSION_RADIUS().invoker().modifyBedExplosionRadius(radius);

        return result == null ? radius : result;
    }
}
