package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.TamableAnimal

object GenesisCombatEvents {
    val ALLOW_PET_DAMAGE = EventFactory.createArrayBacked(AllowPetDamage::class.java) { listeners ->
        AllowPetDamage { level, pet, source, damage ->
            for (listener in listeners) {
                val result = listener.allowPetDamage(level, pet, source, damage)

                if (!result) {
                    return@AllowPetDamage false
                }
            }

            true
        }
    }

    fun interface AllowPetDamage {
        fun allowPetDamage(level: ServerLevel, pet: TamableAnimal, source: DamageSource, damage: Float): Boolean
    }
}
