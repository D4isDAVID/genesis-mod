package dev.d4vid.mods.genesis.server.custom.item.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.*;

public class UltimatePlayerData extends SavedData {
    private final Map<UUID, Identifier> craftedUltimates;

    public static final Codec<UltimatePlayerData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Codec.unboundedMap(UUIDUtil.STRING_CODEC, Identifier.CODEC)
                .fieldOf("crafted").forGetter(d -> d.craftedUltimates),
            UUIDUtil.STRING_CODEC.listOf()
                .fieldOf("allowed").forGetter(d -> new ArrayList<>(d.craftingAllowed))
        ).apply(instance, UltimatePlayerData::new)
    );

    private UltimatePlayerData(Map<UUID, Identifier> crafted, List<UUID> allowed) {
        this.craftedUltimates = crafted;
        this.craftingAllowed = new HashSet<>(allowed);
    }

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

    private Set<UUID> craftingAllowed = new HashSet<>();

    public boolean isCraftingAllowed(UUID playerId) {
        return craftingAllowed.contains(playerId);
    }

    public void setCraftingAllowed(UUID playerId, boolean allowed) {
        if (allowed) craftingAllowed.add(playerId);
        else craftingAllowed.remove(playerId);
        setDirty();
    }

    public boolean hasAnyoneCrafted(Identifier itemId) {
        return craftedUltimates.containsValue(itemId);
    }
}
