package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.Genesis;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenesisItem {
    private static final String CUSTOM_ID_TAG = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, "id").toString();
    private final Identifier identifier;
    private final ItemStack baseStack;
    private final List<DataComponentSetter<?>> dataComponentSetters = new ArrayList<>();

    protected static CompoundTag readCustomData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    protected static void saveCustomData(ItemStack stack, CompoundTag tag) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static @Nullable Identifier getId(ItemStack stack) {
        return readCustomData(stack)
            .getString(CUSTOM_ID_TAG)
            .map(Identifier::parse)
            .orElseGet(() -> stack.get(DataComponents.ITEM_MODEL));
    }

    protected GenesisItem(String name, Item baseItem) {
        identifier = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, name);
        baseStack = new ItemStack(baseItem);

        CompoundTag tag = readCustomData(baseStack);
        tag.putString(CUSTOM_ID_TAG, identifier.toString());
        saveCustomData(baseStack, tag);
    }

    public Identifier getId() {
        return identifier;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean is(ItemStack stack) {
        return identifier.equals(getId(stack));
    }

    public ItemStack build(HolderLookup.Provider registries) {
        ItemStack stack = baseStack.copy();
        update(registries, stack);
        return stack;
    }

    public void update(HolderLookup.Provider registries, ItemStack stack) {
        DataComponentSetter.Data setterData = new DataComponentSetter.Data(registries, stack);

        stack.applyComponents(baseStack.getComponents());
        for (DataComponentSetter<?> setter : dataComponentSetters) {
            setter.set(setterData);
        }
    }

    protected <T> void set(DataComponentType<T> type, T data) {
        baseStack.set(type, data);
    }

    protected <T> void addSetter(DataComponentType<T> type, Function<DataComponentSetter.Data, T> setter) {
        dataComponentSetters.add(new DataComponentSetter<>(type, setter));
    }
}
