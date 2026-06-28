package dev.d4vid.mods.genesis.server.custom.item;

import dev.d4vid.mods.genesis.server.event.GenesisItemEvents;
import dev.d4vid.mods.genesis.server.event.GenesisRecipeEvents;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashMap;
import java.util.Map;

public class GenesisItems {
    public static final Map<Identifier, GenesisItem> REGISTRY = new LinkedHashMap<>();

    public static void initialize() {
        GenesisRecipeEvents.INSTANCE.getALLOW().register((ingredients, result) -> {
            for (ItemStack item : ingredients) {
                if (is(item)) {
                    return false;
                }
            }

            return true;
        });

        GenesisItemEvents.INSTANCE.getINVENTORY_ITEM_ADD().register((player, stack, slot) -> update(player, stack));
        GenesisItemEvents.INSTANCE.getINVENTORY_ITEM_SET().register((player, stack, slot) -> update(player, stack));
        GenesisItemEvents.INSTANCE.getPLAYER_ITEM_DROP().register(GenesisItems::update);

        register(new BloodlustItem());
        register(new DrillItem());
        register(new MegaDrillItem());
        register(new HermesBootsItem());
    }

    public static boolean is(ItemStack stack) {
        Identifier id = GenesisItem.getId(stack);

        return id != null && REGISTRY.containsKey(id);
    }

    private static GenesisItem get(ItemStack stack) {
        return REGISTRY.get(GenesisItem.getId(stack));
    }

    private static void register(GenesisItem item) {
        REGISTRY.put(item.getId(), item);
    }

    private static void update(ServerPlayer player, ItemStack stack) {
        RegistryAccess registries = player.level().registryAccess();
        GenesisItem item = get(stack);

        if (item == null) {
            return;
        }

        item.build(registries, stack);
    }
}
