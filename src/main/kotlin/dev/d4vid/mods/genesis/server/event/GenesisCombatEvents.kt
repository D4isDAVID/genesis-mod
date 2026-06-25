package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.TamableAnimal

object GenesisCombatEvents {
    val ALLOW_ARROW_EFFECT = EventFactory.createArrayBacked(AllowArrowEffect::class.java) { listeners ->
        AllowArrowEffect { effect ->
            for (listener in listeners) {
                val result = listener.allowArrowEffect(effect)

                if (!result) {
                    return@AllowArrowEffect false
                }
            }

            true
        }
    }

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

    val MODIFY_MINECART_TNT_EXPLOSION_RADIUS =
        EventFactory.createArrayBacked(ModifyMinecartTntExplosionRadius::class.java) { listeners ->
            ModifyMinecartTntExplosionRadius { radius ->
                for (listener in listeners) {
                    val result = listener.modifyMinecartTntExplosionRadius(radius)

                    if (result != null) {
                        return@ModifyMinecartTntExplosionRadius result
                    }
                }

                null
            }
        }

    val MODIFY_RESPAWN_ANCHOR_EXPLOSION_RADIUS =
        EventFactory.createArrayBacked(ModifyRespawnAnchorExplosionRadius::class.java) { listeners ->
            ModifyRespawnAnchorExplosionRadius { radius ->
                for (listener in listeners) {
                    val result = listener.modifyRespawnAnchorExplosionRadius(radius)

                    if (result != null) {
                        return@ModifyRespawnAnchorExplosionRadius result
                    }
                }

                null
            }
        }

    val MODIFY_END_CRYSTAL_EXPLOSION_RADIUS =
        EventFactory.createArrayBacked(ModifyEndCrystalExplosionRadius::class.java) { listeners ->
            ModifyEndCrystalExplosionRadius { radius ->
                for (listener in listeners) {
                    val result = listener.modifyEndCrystalExplosionRadius(radius)

                    if (result != null) {
                        return@ModifyEndCrystalExplosionRadius result
                    }
                }

                null
            }
        }

    fun interface AllowArrowEffect {
        fun allowArrowEffect(effect: MobEffectInstance): Boolean
    }

    fun interface AllowPetDamage {
        fun allowPetDamage(level: ServerLevel, pet: TamableAnimal, source: DamageSource, damage: Float): Boolean
    }

    fun interface ModifyMinecartTntExplosionRadius {
        fun modifyMinecartTntExplosionRadius(radius: Float): Float?
    }

    fun interface ModifyRespawnAnchorExplosionRadius {
        fun modifyRespawnAnchorExplosionRadius(radius: Float): Float?
    }

    fun interface ModifyEndCrystalExplosionRadius {
        fun modifyEndCrystalExplosionRadius(radius: Float): Float?
    }
}
