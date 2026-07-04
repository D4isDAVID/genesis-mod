package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

    }
}
