package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RagnarokAxeItem extends GenesisItem {
    private static final int RAGNAROK_AXE_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Ragnarok Axe")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(RAGNAROK_AXE_COLOR));

    public RagnarokAxeItem() {
        super("ragnarok_axe", Items.DIAMOND_AXE, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {

    }

}
