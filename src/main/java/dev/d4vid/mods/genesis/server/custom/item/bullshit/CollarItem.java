package dev.d4vid.mods.genesis.server.custom.item.bullshit;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CollarItem extends BullshitItem{
    private static final int COLOR = 0x87CEEB;

    public CollarItem() {
        super("collar", Items.DIAMOND_HELMET, Component.literal("Collar").withStyle(s -> s
            .withItalic(false)
            .withBold(true)
            .withColor(COLOR)
        ));
    }

    @Override
    protected void build(RegistryAccess registries, ItemStack stack) {

    }
}
