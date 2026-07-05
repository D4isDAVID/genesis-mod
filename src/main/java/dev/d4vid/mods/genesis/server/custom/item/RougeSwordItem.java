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
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class RougeSwordItem extends GenesisItem {
    private static final int ROUGE_SWORD_COLOR = 0x64C4FF;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Rouge Sword")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(ROUGE_SWORD_COLOR));

    public RougeSwordItem() {
        super("rouge_sword", Items.GOLDEN_SWORD, DISPLAY_NAME);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        enchant(registries, item);
        applyLore(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .enchant(item);
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
