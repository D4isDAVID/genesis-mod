package dev.d4vid.mods.genesis.server.custom.item.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.Identifier;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import net.minecraft.world.item.component.ItemLore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SoulboundUtil {
    private static final Set<Identifier> SOULBOUND_IDS = new HashSet<>();
    private static final Map<UUID, List<ItemStack>> RESPAWN_ITEMS = new ConcurrentHashMap<>();

    public static void register(Identifier id) {
        SOULBOUND_IDS.add(id);
    }

    public static boolean isSoulbound(ItemStack stack) {
        Identifier id = GenesisItem.getId(stack);
        return id != null && SOULBOUND_IDS.contains(id);
    }

    public static void storeForRespawn(ServerPlayer player) {
        List<ItemStack> kept = new ArrayList<>();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (isSoulbound(stack)) {
                kept.add(stack.copy());
                player.getInventory().setItem(i, ItemStack.EMPTY);
            }
        }
        RESPAWN_ITEMS.put(player.getUUID(), kept);
    }

    public static void restoreAfterRespawn(ServerPlayer oldPlayer, ServerPlayer newPlayer) {
        List<ItemStack> kept = RESPAWN_ITEMS.remove(oldPlayer.getUUID());
        if (kept == null) return;
        for (ItemStack stack : kept) {
            newPlayer.getInventory().add(stack);
        }
    }
    public static void applyLore(ItemStack stack) {
        List<Component> lore = new ArrayList<>();
        ItemLore existing = stack.get(DataComponents.LORE);
        if (existing != null) lore.addAll(existing.lines());

        boolean alreadyHasLine = lore.stream()
            .anyMatch(c -> c.getString().contains("Soulbound"));
        if (!alreadyHasLine) {
            lore.add(Component.literal("Soulbound")
                .withStyle(s -> s
                    .withItalic(false)
                    .withColor(0xA040FF)));
        }
        stack.set(DataComponents.LORE, new ItemLore(lore));
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
    }

    public static void storeForRespawn(ServerPlayer player, List<ItemStack> items) {
        RESPAWN_ITEMS.put(player.getUUID(), new ArrayList<>(items));
    }
}
