package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.legendary.LegendaryItem;
import dev.d4vid.mods.genesis.server.event.GenesisCustomItemEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonDeathPhase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.List;

public class DrillItem extends LegendaryItem {
    private static final int DRILL_COLOR = 0xD3D3D3;
    private static final int LORE_COLOR = 0x888888;

    private float speed;

    protected DrillItem(String name, Item baseItem, float speed) {
        super(name, baseItem);
        EnderDragon

        this.speed = speed;

        set(DataComponents.ITEM_MODEL, getId());
        set(DataComponents.CUSTOM_NAME, Component
            .literal("Drill")
            .withStyle(s -> s.withItalic(false).withBold(true).withColor(DRILL_COLOR)));

        addSetter(DataComponents.LORE, this::getLore);
        addSetter(DataComponents.ENCHANTMENTS, this::getEnchantments);
        addSetter(DataComponents.TOOL, this::getTool);

        GenesisCustomItemEvents.INSTANCE.getALLOW_PLAYER_ACTION().register((player, packet) -> {
            if (packet.getAction() != ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND) {
                return true;
            }

            ItemStack stack = player.getMainHandItem();
            if (!this.is(stack)) {
                return true;
            }

            toggle(player, stack);
            return false;
        });
    }

    private ItemLore getLore(DataComponentSetter.Data data) {
        return getLore(data.registries(), data.stack());
    }

    private ItemLore getLore(HolderLookup.Provider registries, ItemStack stack) {
        boolean silkTouch = isSilkTouch(registries, stack);
        return new ItemLore(List.of(
            Component.empty(),
            Component.empty()
                .append("Press [")
                .append(Component.keybind("key.swapOffhand"))
                .append("] to toggle:")
                .withStyle(s -> s.withItalic(true).withColor(LORE_COLOR)),
            Component.empty()
                .append(Component.literal("Silk Touch").withStyle(s -> s.withBold(silkTouch)))
                .append(Component.literal(" / "))
                .append(Component.literal("Fortune").withStyle(s -> s.withBold(!silkTouch)))
                .withStyle(s -> s.withItalic(true).withColor(LORE_COLOR))
        ));
    }

    private ItemEnchantments getEnchantments(DataComponentSetter.Data data) {
        return getEnchantments(data.registries(), data.stack());
    }

    private ItemEnchantments getEnchantments(HolderLookup.Provider registries, ItemStack stack) {
        boolean silkTouch = isSilkTouch(registries, stack);
        ItemEnchantmentsBuilder enchantments = new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.EFFICIENCY, 5)
            .add(Enchantments.MENDING, 1)
            .add(Enchantments.UNBREAKING, 3);
        if (silkTouch) {
            enchantments.add(Enchantments.SILK_TOUCH, 1);
        } else {
            enchantments.add(Enchantments.FORTUNE, 3);
        }
        return enchantments.build();
    }

    private Tool getTool(DataComponentSetter.Data data) {
        return getTool(data.registries());
    }

    private Tool getTool(HolderLookup.Provider registries) {
        return new Tool(
            new ToolRulesBuilder(registries)
                .add(BlockTags.MINEABLE_WITH_PICKAXE, speed, true)
                .add(BlockTags.MINEABLE_WITH_SHOVEL, speed, true)
                .add(BlockTags.MINEABLE_WITH_AXE, speed, true)
                .add(BlockTags.MINEABLE_WITH_HOE, speed, true)
                .add(BlockTags.SWORD_INSTANTLY_MINES, speed, true)
                .build(),
            1.0f,
            1,
            true
        );
    }

    private void toggle(ServerPlayer player, ItemStack stack) {
        RegistryAccess registries = player.level().registryAccess();
    }

    private boolean isSilkTouch(HolderLookup.Provider registries, ItemStack stack) {
        Holder<Enchantment> silkTouch = registries
            .lookupOrThrow(Registries.ENCHANTMENT)
            .getOrThrow(Enchantments.SILK_TOUCH);

        return stack.getEnchantments().getLevel(silkTouch) > 0;
    }
}
