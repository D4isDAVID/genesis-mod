package dev.d4vid.mods.genesis.server.custom.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class ItemEnchantmentsBuilder {
    private final HolderLookup.RegistryLookup<Enchantment> registry;
    private final ItemEnchantments.Mutable enchantments;

    public ItemEnchantmentsBuilder(HolderLookup.Provider registries) {
        registry = registries.lookupOrThrow(Registries.ENCHANTMENT);
        enchantments = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
    }

    public ItemEnchantments build() {
        return enchantments.toImmutable();
    }

    public ItemEnchantmentsBuilder add(ResourceKey<Enchantment> key, int strength) {
        enchantments.set(registry.getOrThrow(key), strength);
        return this;
    }
}
