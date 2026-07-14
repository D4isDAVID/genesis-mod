package dev.d4vid.mods.genesis.server.config.data.items

import dev.d4vid.mods.genesis.server.config.field.NbtMatcher
import dev.d4vid.mods.genesis.server.config.serialization.ItemMatcher
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack

@Serializable
data class ItemsConfig(
    private val maxStacks: List<ItemsMaxStackConfig> = listOf(
        ItemsMaxStackConfig(16, NbtMatcher(Identifier.withDefaultNamespace("respawn_anchor"))),
        ItemsMaxStackConfig(16, NbtMatcher(Identifier.withDefaultNamespace("end_crystal"))),
    ),
    val disableTotemDeathProtection: Boolean = true,
    private val disableUsage: List<ItemsDisableUsageConfig> = listOf(
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("ender_pearl")), true),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("trident")), true),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("weakness").toString())
            }
        }), true),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("long_weakness").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("weakness").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("long_weakness").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("slow_falling").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("long_slow_falling").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("slow_falling").toString())
            }
        })),
        ItemsDisableUsageConfig(NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
            putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                put("potion", Identifier.withDefaultNamespace("long_slow_falling").toString())
            }
        })),
    ),
    private val discard: Set<ItemMatcher> = setOf(
        NbtMatcher(Identifier.withDefaultNamespace("elytra")),
        NbtMatcher(Identifier.withDefaultNamespace("netherite_chestplate")),
        NbtMatcher(Identifier.withDefaultNamespace("netherite_leggings")),
        NbtMatcher(Identifier.withDefaultNamespace("netherite_axe")),
        NbtMatcher(Identifier.withDefaultNamespace("netherite_sword")),
    ),
    val overLimitEffects: List<ItemsOverLimitEffectConfig> = listOf(
        ItemsOverLimitEffectConfig(Identifier.withDefaultNamespace("slowness"), 4),
        ItemsOverLimitEffectConfig(Identifier.withDefaultNamespace("blindness"), 0),
    ),
    val scanItemBundleContents: Boolean = true,
    val scanItemContainers: Boolean = false,
    val limits: List<ItemsLimitConfig> = listOf(
        ItemsLimitConfig(128, NbtMatcher(Identifier.withDefaultNamespace("experience_bottle"))),
        ItemsLimitConfig(64, NbtMatcher(Identifier.withDefaultNamespace("cobweb"))),
    ),
    val groupLimits: List<ItemsGroupLimitConfig> = listOf(
        ItemsGroupLimitConfig(
            1, listOf(
                ItemsGroupLimitItemConfig(1, NbtMatcher(Identifier.withDefaultNamespace("potion"), buildJsonObject {
                    putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                        put("potion", Identifier.withDefaultNamespace("turtle_master").toString())
                    }
                })),
                ItemsGroupLimitItemConfig(1, NbtMatcher(Identifier.withDefaultNamespace("potion"), buildJsonObject {
                    putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                        put("potion", Identifier.withDefaultNamespace("long_turtle_master").toString())
                    }
                })),
                ItemsGroupLimitItemConfig(1, NbtMatcher(Identifier.withDefaultNamespace("potion"), buildJsonObject {
                    putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                        put("potion", Identifier.withDefaultNamespace("strong_turtle_master").toString())
                    }
                })),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("turtle_master").toString())
                        }
                    })
                ),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("long_turtle_master").toString())
                        }
                    })
                ),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("splash_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("strong_turtle_master").toString())
                        }
                    })
                ),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("turtle_master").toString())
                        }
                    })
                ),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("long_turtle_master").toString())
                        }
                    })
                ),
                ItemsGroupLimitItemConfig(
                    1,
                    NbtMatcher(Identifier.withDefaultNamespace("lingering_potion"), buildJsonObject {
                        putJsonObject(Identifier.withDefaultNamespace("potion_contents").toString()) {
                            put("potion", Identifier.withDefaultNamespace("strong_turtle_master").toString())
                        }
                    })
                ),
            )
        ),
    ),
) {
    fun getMaxStackForItem(stack: ItemStack): Int? {
        return maxStacks.firstOrNull { it.match.matchItem(stack) }?.max
    }

    fun isItemUsageDisabled(stack: ItemStack, inCombat: Boolean): Boolean {
        return disableUsage.any { (match, requireCombat) ->
            match.matchItem(stack) && (!requireCombat || inCombat)
        }
    }

    fun shouldDiscardItem(stack: ItemStack): Boolean {
        return discard.any { it.matchItem(stack) }
    }

    fun getLimitForItem(stack: ItemStack): ItemsLimitConfig? {
        return limits.firstOrNull { it.match.matchItem(stack) }
    }

    fun getGroupLimitForItem(stack: ItemStack): ItemsGroupLimitConfig? {
        return groupLimits.firstOrNull { group ->
            group.items.any { it.match.matchItem(stack) }
        }
    }
}
