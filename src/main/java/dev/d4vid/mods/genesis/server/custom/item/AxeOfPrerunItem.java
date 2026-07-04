package dev.d4vid.mods.genesis.server.custom.item;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AxeOfPrerunItem extends GenesisItem {
    private static final int AXE_OF_PRERUN_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Axe Of Prerun")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(AXE_OF_PRERUN_COLOR));

    public AxeOfPrerunItem() {
        super("pre_run_axe", Items.DIAMOND_AXE, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {

    }
}
