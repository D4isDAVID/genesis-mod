package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class DragonSteelChestplateItem extends GenesisItem{
    private static final int DRAGON_CHESTPLATE_COLOR = 0x404040;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Dragon Steel Chestplate")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(DRAGON_CHESTPLATE_COLOR));

    public DragonSteelChestplateItem() {
        super("dragon_chestplate", Items.NETHERITE_CHESTPLATE, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        enchant(registries, item);
        applyLore(item);
        applyAttributes(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 4)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(LORE_COLOR))
        )));
    }
    private void applyAttributes(ItemStack item) {
        item.set(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
            .add(Attributes.MAX_HEALTH, new AttributeModifier(
                    Identifier.fromNamespaceAndPath("minecraft", "max_health"),
                    10.0,
                    AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.CHEST)
            .build());
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    @Override
    public boolean isDragonItem() {
        return true;
    }
}
