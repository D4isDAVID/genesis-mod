package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BloodlustItem extends GenesisItem {
    private static final String KILLS_TAG = "killedPlayers";
    private static final int[] LEVEL_KILLS = {0, 1, 3, 5, 9};
    private static final int BLOODLUST_RED = 0xAA0000;
    private static final int BLOODLUST_RED_DARK = BLOODLUST_RED - 0x220000;
    private static final Component DISPLAY_NAME = Component
        .literal("Bloodlust")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(BLOODLUST_RED));
    private static final Component LEVELED_UP = Component
        .literal("Bloodlust has leveled up.")
        .withStyle(s -> s.withBold(true).withColor(BLOODLUST_RED));

    public BloodlustItem() {
        super("bloodlust", Items.DIAMOND_SWORD, DISPLAY_NAME);
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);

        enchant(registries, item, 0);
        applyLore(item, 0, 0, 0);
    }

    @Override
    public void initialize() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (!(entity instanceof ServerPlayer victim)) {
                return;
            }

            Entity attackerEntity = source.getEntity();
            if (!(attackerEntity instanceof ServerPlayer attacker)) {
                return;
            }

            ItemStack item = attacker.getMainHandItem();
            if (!this.is(item)) {
                return;
            }

            this.addKill(item, attacker, victim);
        });

        GenesisCustomItemEvents.INSTANCE.getALLOW_ITEM_SWAP().register((player, stack) -> {
            if (!this.is(stack)) {
                return true;
            }

            UserNameToIdResolver resolver = player.level().getServer().services().nameToIdCache();
            CompoundTag kills = readKills(stack);
            int players = kills.size();
            int total = 0;

            MutableComponent playersComponent = Component.empty()
                .withStyle(s -> s.withColor(BLOODLUST_RED));

            for (Map.Entry<String, Tag> entry : kills.entrySet()) {
                String uuid = entry.getKey();
                String name = resolver.get(UUID.fromString(uuid)).map(NameAndId::name).orElse("<unknown-name>");
                int killCount = entry.getValue().asInt().orElseThrow();

                if (total > 0) {
                    playersComponent.append(", ");
                }

                playersComponent.append(Component.literal(name).withStyle(s ->
                    s.withBold(true).withHoverEvent(new HoverEvent.ShowText(
                        Component.literal("Deaths: " + killCount + "\n" + uuid)
                            .withStyle(s2 -> s2.withColor(BLOODLUST_RED))
                    ))
                ));

                total += killCount;
            }

            player.sendSystemMessage(
                Component
                    .empty()
                    .append(Component.literal("Bloodlust Statistics")
                        .withStyle(s -> s.withBold(true)))
                    .append(Component.literal("\n| Unique kills: "))
                    .append(Component.literal(String.valueOf(players))
                        .withStyle(s -> s.withColor(BLOODLUST_RED).withBold(true)))
                    .append(Component.literal("\n| Total kills: "))
                    .append(Component.literal(String.valueOf(total))
                        .withStyle(s -> s.withColor(BLOODLUST_RED).withBold(true)))
                    .append(Component.literal("\n| Players felled: "))
                    .append(playersComponent)
                    .withStyle(s -> s.withColor(BLOODLUST_RED_DARK))
            );

            return false;
        });
    }

    private void addKill(ItemStack item, ServerPlayer attacker, ServerPlayer victim) {
        RegistryAccess registries = attacker.level().registryAccess();

        CompoundTag kills = readKills(item);
        int killCount = kills.size();
        int initialLevel = getLevel(killCount);
        int newLevel = initialLevel;

        if (addVictimKill(kills, victim)) {
            killCount++;
            newLevel = getLevel(killCount);
        }

        int total = 0;
        for (Map.Entry<String, Tag> entry : kills.entrySet()) {
            total += entry.getValue().asInt().orElseThrow();
        }

        if (newLevel != initialLevel) {
            attacker.sendSystemMessage(LEVELED_UP);
        }

        enchant(registries, item, newLevel);
        applyLore(item, newLevel, killCount, total);
        saveKills(item, kills);
    }

    private void enchant(RegistryAccess registries, ItemStack item, int level) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.SHARPNESS, level + 2)
            .add(Enchantments.FIRE_ASPECT, 2)
            .add(Enchantments.LOOTING, 3)
            .add(Enchantments.SWEEPING_EDGE, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item, int level, int killCount, int total) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("Gets stronger with unique kills")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(BLOODLUST_RED)),
            Component.literal(getLevelLore(level, killCount))
                .withStyle(s -> s.withItalic(false).withColor(BLOODLUST_RED)),
            Component.empty(),
            Component.literal("Fallen Souls: " + total)
                .withStyle(s -> s.withColor(BLOODLUST_RED_DARK)),
            Component.empty()
                .append("Press [")
                .append(Component.keybind("key.swapOffhand"))
                .append("] for kill list")
                .withStyle(s -> s.withColor(BLOODLUST_RED_DARK))
        )));
    }

    private String getLevelLore(int level, int killCount) {
        if (level == LEVEL_KILLS.length - 1) {
            return "Level MAX (" + killCount + " unique kills)";
        }

        int requiredKills = LEVEL_KILLS[level + 1] - killCount;

        return "Level " + level + " (next in " + requiredKills + " unique kill" + (requiredKills == 1 ? "" : "s") + ")";
    }

    private int getLevel(int killCount) {
        int level = 0;

        for (int i = 0; i < LEVEL_KILLS.length; i++) {
            if (killCount >= LEVEL_KILLS[i]) {
                level = i;
            }
        }

        return level;
    }

    private boolean addVictimKill(CompoundTag kills, ServerPlayer victim) {
        Tag data = kills.get(victim.getStringUUID());
        int count = data == null ? 0 : data.asInt().orElse(0);

        kills.put(victim.getStringUUID(), IntTag.valueOf(count + 1));

        return count == 0;
    }

    public CompoundTag readKills(ItemStack item) {
        return readCustomData(item).getCompoundOrEmpty(KILLS_TAG);
    }

    private void saveKills(ItemStack item, CompoundTag kills) {
        CompoundTag tag = readCustomData(item);
        tag.put(KILLS_TAG, kills);
        saveCustomData(item, tag);
    }
}
