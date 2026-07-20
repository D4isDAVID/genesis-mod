package dev.d4vid.mods.genesis.server.custom.item;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class DataComponentSetter<T> {
    private final DataComponentType<T> type;
    private final Function<Data, T> setter;

    public DataComponentSetter(DataComponentType<T> type, Function<Data, T> setter) {
        this.type = type;
        this.setter = setter;
    }

    @SuppressWarnings("unchecked")
    public void set(Data data) {
        T existingData = data.stack.get(type);
        T newData = setter.apply(data);

        if (type == DataComponents.LORE) {
            newData = (T) mergeLore((ItemLore) existingData, (ItemLore) newData);
        } else if (type == DataComponents.ENCHANTMENTS) {
            newData = (T) mergeEnchantments((ItemEnchantments) existingData, (ItemEnchantments) newData);
        }

        data.stack.set(type, newData);
    }

    private ItemLore mergeLore(@Nullable ItemLore existingLore, ItemLore newLore) {
        if (existingLore == null) {
            return newLore;
        }

        List<Component> existingLines = existingLore.lines();
        List<Component> newLines = newLore.lines();
        List<Component> merged = ImmutableList.
            <Component>builderWithExpectedSize(existingLines.size() + newLines.size())
            .addAll(existingLines)
            .addAll(newLines)
            .build();

        return new ItemLore(merged);
    }

    private ItemEnchantments mergeEnchantments(@Nullable ItemEnchantments existingEnchants, ItemEnchantments newEnchants) {
        if (existingEnchants == null) {
            return newEnchants;
        }

        ItemEnchantments.Mutable merged = new ItemEnchantments.Mutable(existingEnchants);
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : newEnchants.entrySet()) {
            merged.set(entry.getKey(), entry.getIntValue());
        }

        return merged.toImmutable();
    }

    public record Data(HolderLookup.Provider registries, ItemStack stack) {
    }
}
