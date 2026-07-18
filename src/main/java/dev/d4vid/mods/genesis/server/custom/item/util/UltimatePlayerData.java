package dev.d4vid.mods.genesis.server.custom.item.util;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UltimatePlayerData extends SavedData {
    private final Map<UUID, Identifier> craftedUltimates;

    public static final Codec<UltimatePlayerData> CODEC =
        Codec.unboundedMap(UUIDUtil.STRING_CODEC, Identifier.CODEC)
            .xmap(UltimatePlayerData::new, data -> data.craftedUltimates);

    public static final SavedDataType<UltimatePlayerData> TYPE = new SavedDataType<>(
        "genesis_ultimate_data", UltimatePlayerData::new, CODEC, DataFixTypes.LEVEL
    );

    private UltimatePlayerData() {
        this(new HashMap<>());
    }

    private UltimatePlayerData(Map<UUID, Identifier> craftedUltimates) {
        this.craftedUltimates = craftedUltimates;
    }

    public static UltimatePlayerData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(TYPE);
    }

    public boolean hasCrafted(UUID playerId) {
        return craftedUltimates.containsKey(playerId);
    }

    public void setCrafted(UUID playerId, Identifier itemId) {
        craftedUltimates.put(playerId, itemId);
        setDirty();
    }

    public void reset(UUID playerId) {
        craftedUltimates.remove(playerId);
        setDirty();
    }

    public boolean hasAnyoneCrafted(Identifier itemId) {
        return craftedUltimates.containsValue(itemId);
    }
}
