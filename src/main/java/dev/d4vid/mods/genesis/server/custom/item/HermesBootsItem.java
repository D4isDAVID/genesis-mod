package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.config.GenesisConfig;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import net.minecraft.world.entity.EquipmentSlotGroup;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


import java.util.List;

public class HermesBootsItem extends GenesisItem {
    private static final Identifier SPEED_BOOST = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "hermes_speed_boost");
    private static final int HERMES_BOOTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Hermes Boots")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(HERMES_BOOTS_COLOR));

    private final GenesisConfig config;

    public HermesBootsItem(GenesisConfig config) {
        super("hermes_boots", Items.DIAMOND_BOOTS, DISPLAY_NAME);
        this.config = config;

        ServerEntityEvents.EQUIPMENT_CHANGE.register((entity, slot, previousStack, currentStack) -> {
            if (slot != EquipmentSlot.FEET || !(entity instanceof ServerPlayer player)) {
                return;
            }

            if (this.is(currentStack)) {
                grantSpeedBoost(player);
            } else {
                removeSpeedBoost(player);
            }
        });
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
        applyAttributes(item);
    }

    private void grantSpeedBoost(ServerPlayer player) {
        getSpeedBoost(player).addOrUpdateTransientModifier(new AttributeModifier(
            SPEED_BOOST,
            config.getData().getCustom().getItems().getHermesBoots().getAddSpeedMultiplier(),
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ));
    }

    private void removeSpeedBoost(ServerPlayer player) {
        getSpeedBoost(player).removeModifier(SPEED_BOOST);
    }

    private AttributeInstance getSpeedBoost(ServerPlayer player) {
        return player.getAttributes().getInstance(Attributes.MOVEMENT_SPEED);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        ItemEnchantmentsBuilder enchantments = new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 4)
            .add(Enchantments.FEATHER_FALLING, 4)
            .add(Enchantments.DEPTH_STRIDER, 3)
            .add(Enchantments.SOUL_SPEED, 3)
            .add(Enchantments.SOUL_SPEED, 3);
            enchantments.enchant(item);

    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("Grants you speed")
                .withStyle(s -> s.withItalic(true).withColor(LORE_COLOR))
        )));
    }

    private void applyAttributes(ItemStack item) {
        AttributeModifier speedModifier = new AttributeModifier(
            Identifier.fromNamespaceAndPath("genesis", "hermes_boots_speed"),
            0.25,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        );

        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(
                Identifier.fromNamespaceAndPath("genesis", "hermes_boots_armor"),
                3.0,
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.FEET)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                Identifier.fromNamespaceAndPath("genesis", "hermes_boots_toughness"),
                2.0,
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.FEET)
            .add(Attributes.MOVEMENT_SPEED, speedModifier, EquipmentSlotGroup.FEET)
            .build();
            item.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}
