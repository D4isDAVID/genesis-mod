package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class HermesBootsItem extends GenesisItem {
    private static final int HERMES_BOOTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Hermes Boots")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(HERMES_BOOTS_COLOR));

    public HermesBootsItem() {
        super("hermes_boots", Items.DIAMOND_BOOTS, DISPLAY_NAME);
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
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
            Component.literal("Grants you speed")
                .withStyle(s -> s.withItalic(true).withColor(LORE_COLOR))
        )));
    }
}
