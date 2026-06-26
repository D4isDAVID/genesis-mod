package dev.d4vid.mods.genesis.server.custom.item.bullshit;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BullshitItem {
    protected final Identifier identifier;
    protected final ItemStack baseItem;

    protected BullshitItem(String name, Item baseItem, Component displayName) {
        identifier = Identifier.fromNamespaceAndPath("bullshit", name);
        ItemStack stack = new ItemStack(baseItem);
        stack.set(DataComponents.ITEM_MODEL, identifier);
        stack.set(DataComponents.CUSTOM_NAME, displayName);
        this.baseItem = stack;
    }

    protected abstract void build(RegistryAccess registries, ItemStack stack);

    public Identifier getId() {
        return identifier;
    }

    public ItemStack assemble(RegistryAccess registries) {
        ItemStack item = baseItem.copy();
        build(registries, item);
        return item;
    }
}
