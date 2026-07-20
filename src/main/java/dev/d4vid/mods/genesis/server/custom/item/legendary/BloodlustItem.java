package dev.d4vid.mods.genesis.server.custom.item.legendary;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback;
import dev.d4vid.mods.genesis.server.config.data.custom.item.bloodlust.BloodlustConfig;
import dev.d4vid.mods.genesis.server.config.data.custom.item.bloodlust.BloodlustLevelConfig;
import dev.d4vid.mods.genesis.server.custom.item.DataComponentSetter;
import dev.d4vid.mods.genesis.server.custom.item.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserNameToIdResolver;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BloodlustItem extends LegendaryItem {
    private static final int BLOODLUST_RED = 0xAA0000;
    private static final int BLOODLUST_RED_DARK = BLOODLUST_RED - 0x220000;

    private BloodlustConfig config;

    public BloodlustItem() {
        super("bloodlust", Items.DIAMOND_SWORD);

        set(DataComponents.ITEM_MODEL, getId());
        set(DataComponents.CUSTOM_NAME, Component
            .literal("Bloodlust")
            .withStyle(s -> s.withItalic(false).withBold(true).withColor(BLOODLUST_RED)));
        set(DataComponents.UNBREAKABLE, Unit.INSTANCE);

        addSetter(DataComponents.LORE, this::getLore);
        addSetter(DataComponents.ENCHANTMENTS, this::getEnchantments);

        GenesisConfigLoadCallback.Companion.getEVENT().register(it -> config = it.getCustom().getItems().getBloodlust());

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

        GenesisCustomItemEvents.INSTANCE.getALLOW_PLAYER_ACTION().register((player, packet) -> {
            if (packet.getAction() != ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
                return true;
            }

            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if (!this.is(stack)) {
                return true;
            }

            sendStats(player, stack);
            return false;
        });
    }

    private ItemLore getLore(DataComponentSetter.Data data) {
        return getLore(new Data(data.stack()));
    }

    private ItemLore getLore(Data data) {
        int total = data.getTotalKillCount();

        return new ItemLore(List.of(
            Component.empty(),
            Component.literal("Gets stronger with unique kills")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(BLOODLUST_RED)),
            Component.literal(getLevelLoreLine(data))
                .withStyle(s -> s.withItalic(false).withColor(BLOODLUST_RED)),
            Component.empty(),
            Component.literal("Fallen Souls: " + total)
                .withStyle(s -> s.withColor(BLOODLUST_RED_DARK)),
            Component.empty()
                .append("Press [")
                .append(Component.keybind("key.swapOffhand"))
                .append("] for kill list")
                .withStyle(s -> s.withColor(BLOODLUST_RED_DARK))
        ));
    }

    private String getLevelLoreLine(Data data) {
        int killCount = data.getUniqueKillCount();
        int level = data.getLevel();
        List<BloodlustLevelConfig> levels = config.getLevels();

        if ((level - 1) == levels.size()) {
            return "Level MAX (" + killCount + " unique kills)";
        }

        int requiredKills = levels.get(level - 1).getRequiredKills() - killCount;
        return "Level " + level + " (next in " + requiredKills + " unique kill" + (requiredKills == 1 ? "" : "s") + ")";
    }

    private ItemEnchantments getEnchantments(DataComponentSetter.Data data) {
        return getEnchantments(data.registries(), new Data(data.stack()));
    }

    private ItemEnchantments getEnchantments(HolderLookup.Provider registries, Data data) {
        int level = data.getLevel();
        int index = level - 2;
        int sharpnessLevel = index < 0 ? config.getInitialSharpnessLevel() : config.getLevels().get(index).getSharpnessLevel();

        return new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.SHARPNESS, sharpnessLevel)
            .add(Enchantments.FIRE_ASPECT, 2)
            .add(Enchantments.LOOTING, 3)
            .add(Enchantments.SWEEPING_EDGE, 3)
            .build();
    }

    private void addKill(ItemStack stack, ServerPlayer attacker, ServerPlayer victim) {
        RegistryAccess registries = attacker.level().registryAccess();

        Data data = new Data(stack);
        int initialLevel = data.getLevel();

        if (data.addKill(victim) && data.getLevel() > initialLevel) {
            attacker.sendSystemMessage(Component
                .literal("Bloodlust has leveled up.")
                .withStyle(s -> s.withBold(true).withColor(BLOODLUST_RED)));
        }

        data.save(stack);
        update(registries, stack);
    }

    private void sendStats(ServerPlayer player, ItemStack stack) {
        UserNameToIdResolver resolver = player.level().getServer().services().nameToIdCache();
        Data data = new Data(stack);
        int players = data.uniqueKillCount;
        int total = 0;

        MutableComponent playersComponent = Component.empty()
            .withStyle(s -> s.withColor(BLOODLUST_RED));

        for (Map.Entry<String, Tag> entry : data.killEntrySet()) {
            UUID uuid = UUID.fromString(entry.getKey());
            int killCount = entry.getValue().asInt().orElseThrow();

            String name = resolver.get(uuid).map(NameAndId::name).orElse("<unknown-name>");

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
    }

    private class Data {
        private static final String KILLS_TAG = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "bloodlust_kills").toString();

        private final CompoundTag kills;
        private int uniqueKillCount;
        private int totalKillCount = 0;
        private boolean totalKillCountComputed = false;
        private int level;

        public Data(ItemStack stack) {
            this(readCustomData(stack).getCompoundOrEmpty(KILLS_TAG));
        }

        private Data(CompoundTag kills) {
            this.kills = kills;
            uniqueKillCount = kills.size();
            level = computeLevel();
        }

        public void save(ItemStack stack) {
            CompoundTag tag = readCustomData(stack);
            tag.put(KILLS_TAG, kills);
            saveCustomData(stack, tag);
        }

        public Set<Map.Entry<String, Tag>> killEntrySet() {
            return kills.entrySet();
        }

        public boolean addKill(ServerPlayer victim) {
            String uuid = victim.getStringUUID();
            Tag data = kills.get(uuid);
            int count = data == null ? 0 : data.asInt().orElse(0);

            kills.put(uuid, IntTag.valueOf(count + 1));

            if (totalKillCountComputed) {
                totalKillCount++;
            }

            if (count != 0) {
                return false;
            }

            uniqueKillCount++;
            return true;
        }

        public int getUniqueKillCount() {
            return uniqueKillCount;
        }

        public int getTotalKillCount() {
            if (!totalKillCountComputed) {
                totalKillCount = 0;
                for (Map.Entry<String, Tag> entry : kills.entrySet()) {
                    totalKillCount += entry.getValue().asInt().orElseThrow();
                }
                totalKillCountComputed = true;
            }

            return totalKillCount;
        }

        public int getLevel() {
            return level;
        }

        private int computeLevel() {
            List<BloodlustLevelConfig> levels = config.getLevels();
            int level = 1;

            for (int i = 0; i < levels.size(); i++) {
                if (uniqueKillCount >= levels.get(i).getRequiredKills()) {
                    level = i + 2;
                }
            }

            return level;
        }
    }
}
