package dev.d4vid.mods.genesis.server.custom.item.legendary;

import dev.d4vid.mods.genesis.server.custom.item.ToolRulesBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Tool;

public class GigaDrillItem extends LegendaryItem {
    private static final int MEGA_DRILL_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;

    public GigaDrillItem() {
        super("mega_drill", Items.NETHERITE_PICKAXE);

        set(DataComponents.ITEM_MODEL, getId());
        set(DataComponents.CUSTOM_NAME, Component
            .literal("Giga Drill™")
            .withStyle(s -> s.withItalic(false).withBold(true).withColor(MEGA_DRILL_COLOR)));
        set(DataComponents.UNBREAKABLE, Unit.INSTANCE);

        addSetter(DataComponents.TOOL, (data) -> new Tool(
            new ToolRulesBuilder(data.registries())
                .add(BlockTags.MINEABLE_WITH_PICKAXE, 9.0f, true)
                .add(BlockTags.MINEABLE_WITH_SHOVEL, 9.0f, true)
                .add(BlockTags.MINEABLE_WITH_AXE, 9.0f, true)
                .add(BlockTags.MINEABLE_WITH_HOE, 9.0f, true)
                .add(BlockTags.SWORD_INSTANTLY_MINES, 9.0f, true)
                .build(),
            1.0f,
            1,
            true
        ));
    }
}
