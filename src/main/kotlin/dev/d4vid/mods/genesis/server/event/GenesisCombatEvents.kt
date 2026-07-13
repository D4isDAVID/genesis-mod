package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.alchemy.Potion

object GenesisCombatEvents {
    val ALLOW_ARROW_POTION = EventFactory.createArrayBacked(AllowArrowPotion::class.java) { listeners ->
        AllowArrowPotion { potion ->
            for (listener in listeners) {
                val result = listener.allowArrowPotion(potion)

                if (!result) {
                    return@AllowArrowPotion false
                }
            }

            true
        }
    }

    val ALLOW_LIVING_ENTITY_VULNERABLE =
        EventFactory.createArrayBacked(AllowLivingEntityVulnerable::class.java) { listeners ->
            AllowLivingEntityVulnerable { level, entity, source ->
                for (listener in listeners) {
                    val result = listener.allowLivingEntityVulnerable(level, entity, source)

                    if (!result) {
                        return@AllowLivingEntityVulnerable false
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

    val MODIFY_BED_EXPLOSION_RADIUS =
        EventFactory.createArrayBacked(ModifyBedExplosionRadius::class.java) { listeners ->
            ModifyBedExplosionRadius { radius ->
                for (listener in listeners) {
                    val result = listener.modifyBedExplosionRadius(radius)

                    if (result != null) {
                        return@ModifyBedExplosionRadius result
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

    fun interface AllowArrowPotion {
        fun allowArrowPotion(potion: Holder<Potion>): Boolean
    }

    fun interface AllowLivingEntityVulnerable {
        fun allowLivingEntityVulnerable(level: ServerLevel, entity: LivingEntity, source: DamageSource): Boolean
    }

    fun interface ModifyMinecartTntExplosionRadius {
        fun modifyMinecartTntExplosionRadius(radius: Float): Float?
    }

    fun interface ModifyBedExplosionRadius {
        fun modifyBedExplosionRadius(radius: Float): Float?
    }

    fun interface ModifyRespawnAnchorExplosionRadius {
        fun modifyRespawnAnchorExplosionRadius(radius: Float): Float?
    }

    fun interface ModifyEndCrystalExplosionRadius {
        fun modifyEndCrystalExplosionRadius(radius: Float): Float?
    }
}
