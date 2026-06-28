package dev.d4vid.mods.genesis.server.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.item.ItemStack

object GenesisItemEvents {
    val ALLOW_TOTEM = EventFactory.createArrayBacked(AllowTotem::class.java) { listeners ->
        AllowTotem { source ->
            for (listener in listeners) {
                val result = listener.allowTotem(source)

                if (!result) {
                    return@AllowTotem false
                }
            }

            true
        }
    }

    val INVENTORY_ITEM_ADD = EventFactory.createArrayBacked(InventoryItemAdd::class.java) { listeners ->
        InventoryItemAdd { player, stack, slot ->
            for (listener in listeners) {
                listener.inventoryItemAdd(player, stack, slot)
            }
        }
    }

    val INVENTORY_ITEM_SET = EventFactory.createArrayBacked(InventoryItemSet::class.java) { listeners ->
        InventoryItemSet { player, stack, slot ->
            for (listener in listeners) {
                listener.inventoryItemSet(player, stack, slot)
            }
        }
    }

    val PLAYER_ITEM_DROP = EventFactory.createArrayBacked(PlayerItemDrop::class.java) { listeners ->
        PlayerItemDrop { player, stack ->
            for (listener in listeners) {
                listener.playerItemDrop(player, stack)
            }
        }
    }

    val PLAYER_CONTAINER_CLOSE = EventFactory.createArrayBacked(PlayerContainerClose::class.java) { listeners ->
        PlayerContainerClose { player ->
            for (listener in listeners) {
                listener.playerContainerClose(player)
            }
        }
    }

    val MODIFY_DEFAULT_MAX_STACK_SIZE =
        EventFactory.createArrayBacked(ModifyDefaultMaxStackSize::class.java) { listeners ->
            ModifyDefaultMaxStackSize { stack ->
                for (listener in listeners) {
                    val result = listener.modifyDefaultMaxStackSize(stack)

                    if (result != null) {
                        return@ModifyDefaultMaxStackSize result
                    }
                }

                null
            }
        }

    fun interface AllowTotem {
        fun allowTotem(source: DamageSource): Boolean
    }

    fun interface InventoryItemAdd {
        fun inventoryItemAdd(player: ServerPlayer, stack: ItemStack, slot: Int)
    }

    fun interface InventoryItemSet {
        fun inventoryItemSet(player: ServerPlayer, stack: ItemStack, slot: Int)
    }

    fun interface PlayerItemDrop {
        fun playerItemDrop(player: ServerPlayer, stack: ItemStack)
    }

    fun interface PlayerContainerClose {
        fun playerContainerClose(player: ServerPlayer)
    }

    fun interface ModifyDefaultMaxStackSize {
        fun modifyDefaultMaxStackSize(stack: ItemStack): Int?
    }
}
