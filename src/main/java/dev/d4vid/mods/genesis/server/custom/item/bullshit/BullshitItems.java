package dev.d4vid.mods.genesis.server.custom.item.bullshit;

import net.minecraft.resources.Identifier;
import java.util.LinkedHashMap;
import java.util.Map;

public class BullshitItems {
    public static final Map<Identifier, BullshitItem> REGISTRY = new LinkedHashMap<>();

    static {
        register(new BlahajItem());
        register(new CollarItem());
        register(new ClickerItem());
    }

    public static void register(BullshitItem item) {
        REGISTRY.put(item.getId(), item);
    }
}
