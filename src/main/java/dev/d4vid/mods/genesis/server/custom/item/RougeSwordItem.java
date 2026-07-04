package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RougeSwordItem extends GenesisItem {
    private static final int ROUGE_SWORD_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Rouge Sword")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(ROUGE_SWORD_COLOR));

    public RougeSwordItem() {
        super("rouge_sword", Items.DIAMOND_SWORD, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {

    }
}
