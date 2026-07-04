package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CreeperPantsItem extends GenesisItem {
    private static final int CREEPER_PANTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Creeper Pants")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(CREEPER_PANTS_COLOR));

    public CreeperPantsItem() {
        super("explorers_charm", Items.DIAMOND_LEGGINGS, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {

    }
}
