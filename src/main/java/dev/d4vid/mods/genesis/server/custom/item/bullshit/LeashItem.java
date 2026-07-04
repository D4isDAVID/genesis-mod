package dev.d4vid.mods.genesis.server.custom.item.bullshit;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.Equippable;

public class LeashItem extends BullshitItem {
    private static final int COLOR = 0xFFD700;

    public LeashItem() {
        super("leash", Items.RECOVERY_COMPASS, Component.literal("Leash").withStyle(s -> s
            .withItalic(false)
            .withBold(true)
            .withColor(COLOR)
        ));
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack stack) {

    }
}
