package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.custom.item.util.ItemEnchantmentsBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;

public class WingsItem extends GenesisItem {
    private static final int WINGS_COLOR = 0xE5E5E5;
    private static final int LORE_COLOR = 0x888888;
    private static final Component DISPLAY_NAME = Component
        .literal("Wings")
        .withStyle(s -> s.withItalic(false).withBold(true).withColor(WINGS_COLOR));

    public WingsItem() {
        super("wings", Items.ELYTRA, DISPLAY_NAME/*, Identifier.fromNamespaceAndPath("minecraft","elytra")*/);

    }
    @Override
    protected void build(RegistryAccess registries, ItemStack item) {
        //item.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
        enchant(registries, item);
        applyLore(item);
        applyEquippable(item);
    }

    private void enchant(RegistryAccess registries, ItemStack item) {
        new ItemEnchantmentsBuilder(registries)
            .add(Enchantments.UNBREAKING, 3)
            .add(Enchantments.MENDING, 3)
            .enchant(item);
    }

    private void applyLore(ItemStack item) {
        item.set(DataComponents.LORE, new ItemLore(List.of(
            Component.empty(),
            Component.literal("WIP")
                .withStyle(s -> s.withItalic(false).withBold(true).withColor(LORE_COLOR))
        )));
    }

    private void applyEquippable(ItemStack item) {
        ResourceKey<EquipmentAsset> assetKey = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath("genesis", "wings")
        );
        item.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.CHEST)
            .setAsset(assetKey)
            .setDamageOnHurt(false)
            .build());
    }
    @Override
    public boolean canBeEnchanted() {
        return false;
    }

    @Override
    public boolean isDragonItem() {
        return true;
    }

    @Override
    public boolean isUltimate() {return false; }
}
