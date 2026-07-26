package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class WingsItem extends GenesisItem {
    private static final int WINGS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Wings")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(WINGS_COLOR));

    public WingsItem() {
        super("wings", Items.ELYTRA, DISPLAY_NAME, Identifier.fromNamespaceAndPath("minecraft","elytra"));

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        enchant(registries, item);
        applyLore(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
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
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    @Override
    public boolean isDragonItem() {
        return true;
    }
}
