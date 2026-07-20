package dev.d4vid.mods.genesis.server.custom.item.legendary;

import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public class LegendaryItem extends GenesisItem {
    protected LegendaryItem(String name, Item baseItem) {
        super(name, baseItem);

        set(DataComponents.LORE, new ItemLore(
            List.of(
                Component.empty(),
                Component.literal("LEGENDARY")
                    .withStyle(s -> s.withItalic(false).withBold(true).withColor(ChatFormatting.GOLD))
            )
        ));
    }
}
