package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.custom.item.util.BootMovementAbilities;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SpiderBootsItem extends GenesisItem {
    private static final int SPIDER_BOOTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private final Map<UUID, Boolean> hasDoubleJumped = new HashMap<>();
    private static final Component DISPLAY_NAME = Component
        .literal("Spider Boots")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(SPIDER_BOOTS_COLOR));

    public SpiderBootsItem() {
        super("spider_boots", Items.DIAMOND_BOOTS, DISPLAY_NAME);
        BootMovementAbilities.registerDoubleJump(this);
        BootMovementAbilities.registerWallClimb(this);
    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
        applyEquippable(item);
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.PROTECTION,3)
            .add(Enchantments.FEATHER_FALLING,4)
            .add(Enchantments.DEPTH_STRIDER,3)
            .add(Enchantments.SOUL_SPEED, 2)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(SPIDER_BOOTS_COLOR))
        )));
    }
    private void applyEquippable(ItemStack item) {
        ResourceKey<EquipmentAsset> assetKey = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "spider_boots")
        );
        item.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.FEET)
            .setAsset(assetKey)
            .build());
    }
}
