package dev.d4vid.mods.genesis.server.custom.item.util;

import net.minecraft.world.entity.LivingEntity;

public class TrueDamage {
    public static void applyExact(LivingEntity victim, float exactDamage) {
        float newHealth = Math.max(0f, victim.getHealth() - exactDamage);
        victim.setHealth(newHealth);
        if (newHealth <= 0f) {
            victim.die(victim.damageSources().generic());
        }
    }
}
