package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class TheLeechItem extends GenesisItem {
    private static final int THE_LEECH_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("The Leech")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(THE_LEECH_COLOR));

    public TheLeechItem() {
        super("the_leech", Items.DIAMOND_SWORD, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
    }
    @Override
    public boolean canBeEnchanted() {
        return true;
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(THE_LEECH_COLOR))
        )));
    }
}
