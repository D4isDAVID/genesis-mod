package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class CreeperPantsItem extends GenesisItem {
    private static final int CREEPER_PANTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Creeper Pants")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(CREEPER_PANTS_COLOR));

    public CreeperPantsItem() {
        super("creeper_pants", Items.DIAMOND_LEGGINGS, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.PROTECTION, 3)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.SWIFT_SNEAK, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(LORE_COLOR))
        )));
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }
}
