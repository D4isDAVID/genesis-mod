package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class SpiderBootsItem extends GenesisItem {
    private static final int SPIDER_BOOTS_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Spider Boots")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(SPIDER_BOOTS_COLOR));

    public SpiderBootsItem() {
        super("spider_boots", Items.DIAMOND_BOOTS, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        enchant(registries, item);
        applyLore(item);
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(SPIDER_BOOTS_COLOR))
        )));
    }
}
