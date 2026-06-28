package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.config.GenesisConfigLoadCallback;
import dev.d4vid.mods.genesis.server.config.data.custom.item.HermesBootsConfig;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;

public class HermesBootsItem extends GenesisItem {
    private static final Identifier SPEED_KEY = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "hermes_boots_speed");
    private static final Identifier ARMOR_KEY = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "hermes_boots_armor");
    private static final Identifier TOUGHNESS_KEY = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "hermes_boots_toughness");
    private static final int HERMES_BOOTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Hermes Boots")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(HERMES_BOOTS_COLOR));

    private HermesBootsConfig config;

    public HermesBootsItem() {
        super("hermes_boots", Items.DIAMOND_BOOTS, DISPLAY_NAME);

        GenesisConfigLoadCallback.Companion.getEVENT().register(it -> config = it.getCustom().getItems().getHermesBoots());

        GenesisCustomItemEvents.INSTANCE.getALLOW_PLAYER_FALL_DAMAGE().register(((player, fallDistance, multiplier, source) -> {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

            return !this.is(boots);
        }));
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
        applyAttributes(item);
        applyEquippable(item);
    }

    private void applyEquippable(ItemStack item) {
        ResourceKey<EquipmentAsset> assetKey = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "hermes_boots")
        );

        item.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.FEET)
            .setAsset(assetKey)
            .setDamageOnHurt(false)
            .build());
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 4)
            .add(Enchantments.FEATHER_FALLING, 4)
            .add(Enchantments.DEPTH_STRIDER, 3)
            .add(Enchantments.SOUL_SPEED, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("Grants you speed and")
                .withStyle(s -> s.withColor(LORE_COLOR)),
            Component.literal("protects you from falling")
                .withStyle(s -> s.withColor(LORE_COLOR))
        )));
    }

    private void applyAttributes(ItemStack item) {
        ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(
                ARMOR_KEY,
                config.getAddArmor(),
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.FEET)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                TOUGHNESS_KEY,
                config.getAddToughness(),
                AttributeModifier.Operation.ADD_VALUE
            ), EquipmentSlotGroup.FEET)
            .add(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                SPEED_KEY,
                config.getAddSpeedMultiplier(),
                AttributeModifier.Operation.ADD_MULTIPLIED_BASE
            ), EquipmentSlotGroup.FEET)
            .build();
        item.set(DataComponents.ATTRIBUTE_MODIFIERS, modifiers);
    }
}
