package dev.d4vid.mods.genesis.server.custom.item.util;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import dev.d4vid.mods.genesis.server.event.GenesisItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static dev.d4vid.mods.genesis.server.custom.item.util.CraftingManager.isDragonItem;

public class UltimateManager {
    private static final Set<Identifier> ULTIMATE_IDS = new HashSet<>();
    private static final Map<UUID, Identifier> CRAFTED_BY = new ConcurrentHashMap<>();
    private static final Set<Identifier> RETURNS_ON_DEATH = new HashSet<>();
    private static final String NBT_KEY = "genesis_crafted_ultimate";

    public static void register(GenesisItem item) {
        ULTIMATE_IDS.add(item.getId());
    }

    public static boolean isUltimate(ItemStack stack) {
        Identifier id = GenesisItem.getId(stack);
        return id != null && ULTIMATE_IDS.contains(id);
    }

    public static boolean hasUltimate(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (isUltimate(player.getInventory().getItem(i))) return true;
        }
        return false;
    }

    public static void resetCraftedUltimate(ServerPlayer player) {
        CRAFTED_BY.remove(player.getUUID());
    }

    public static Optional<ItemStack> getUltimate(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (isUltimate(stack)) return Optional.of(stack);
        }
        return Optional.empty();
    }

    public static void removeUltimate(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            if (isUltimate(player.getInventory().getItem(i))) {
                player.getInventory().setItem(i, ItemStack.EMPTY);
                return;
            }
        }
    }

    public static boolean hasCraftedUltimate(ServerPlayer player) {
        return CRAFTED_BY.containsKey(player.getUUID());
    }

    public static void setCraftedUltimate(ServerPlayer player, Identifier itemId) {
        CRAFTED_BY.put(player.getUUID(), itemId);
    }

    public static boolean canCraft(ServerPlayer player, ItemStack result) {
        if (!isUltimate(result)) return true;
        if (isDragonItem(result)) return !CraftingManager.isDragonItem(result);
        return !hasCraftedUltimate(player);
    }

    public static void recordCraft(ServerPlayer player, ItemStack result) {
        if (!isUltimate(result)) return;
        CRAFTED_BY.put(player.getUUID(), GenesisItem.getId(result));
    }

    public static void initialize() {
        for (GenesisItem item : GenesisItems.REGISTRY.values()) {
            ULTIMATE_IDS.add(item.getId());
            if (item.returnsOnDeath()) RETURNS_ON_DEATH.add(item.getId());
        }

        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(entity instanceof ServerPlayer killed)) return;
            if (source.getEntity() instanceof ServerPlayer killer) {
                getUltimate(killed).ifPresent(stack -> {
                    removeUltimate(killed);
                    if (!killer.getInventory().add(stack)) {
                        killer.containerMenu.setCarried(stack);
                        killer.sendSystemMessage(Component.literal(
                            "You claimed " + killed.getName().getString() + "'s ultimate! (check your cursor)"));
                    } else {
                        killer.sendSystemMessage(Component.literal(
                            "You claimed " + killed.getName().getString() + "'s ultimate!"));
                    }
                });
            } else {
                Identifier id = GenesisItem.getId(findUltimate(killed));
                if (id != null && RETURNS_ON_DEATH.contains(id)) {
                    ItemStack stack = findAndRemoveUltimate(killed);
                    SoulboundUtil.storeForRespawn(killed, List.of(stack));
                }
            }
        });
    }

    public static ItemStack findUltimate(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (isUltimate(stack)) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findAndRemoveUltimate(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (isUltimate(stack)) {
                ItemStack copy = stack.copy();
                player.getInventory().setItem(i, ItemStack.EMPTY);
                return copy;
            }
        }
        return ItemStack.EMPTY;
    }
}
