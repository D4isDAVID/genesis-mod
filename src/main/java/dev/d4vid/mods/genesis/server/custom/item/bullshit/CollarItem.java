package dev.d4vid.mods.genesis.server.custom.item.bullshit;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.core.registries.Registries;
import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;

public class CollarItem extends BullshitItem{
    private static final int COLOR = 0xFFD700;

    public CollarItem() {
        super("collar", Items.RECOVERY_COMPASS, Component.literal("Collar").withStyle(s -> s
            .withItalic(false)
            .withBold(true)
            .withColor(COLOR)
        ));
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack stack) {
            stack.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).build());

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
            .add(Attributes.ARMOR, new AttributeModifier(
                    Identifier.fromNamespaceAndPath("minecraft", "armor"),
                    3.0,
                    AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.HEAD)
            .add(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(
                    Identifier.fromNamespaceAndPath("minecraft", "armor_toughness"),
                    2.0,
                    AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.HEAD)
            .build());
        stack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 4)
            .add(Enchantments.RESPIRATION, 3)
            .add(Enchantments.AQUA_AFFINITY, 1)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.BINDING_CURSE, 1)
            .enchant(stack);
    }
}
