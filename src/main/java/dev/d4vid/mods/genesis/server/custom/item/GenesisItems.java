package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class GenesisItems {
    public static final Map<Identifier, GenesisItem> REGISTRY = new LinkedHashMap<>();

    public GenesisItems() {
        GenesisRecipeEvents.INSTANCE.getALLOW().register((ingredients, result) -> {
            for (ItemStack item : ingredients) {
                if (is(item)) {
                    return false;
                }
            }

            return true;
        });

        register(new BloodlustItem());
        register(new DrillItem());
        register(new MegaDrillItem());
        register(new HermesBootsItem());
    }

    public static boolean is(ItemStack stack) {
        Identifier id = GenesisItem.getId(stack);

        return id != null && REGISTRY.containsKey(id);
    }

    private static void register(GenesisItem item) {
        REGISTRY.put(item.getId(), item);
    }
}
