package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class ExploreresCharmItem extends GenesisItem {
    private static final int EXPLORERS_CHARM_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Explorers Charm")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(EXPLORERS_CHARM_COLOR));

    public ExploreresCharmItem() {
        super("explorers_charm", Items.CARROT_ON_A_STICK, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        applyLore(item);
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
