package dev.d4vid.mods.genesis.server.custom.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolRulesBuilder {
    private final HolderLookup.RegistryLookup<Block> registry;
    private final ArrayList<Tool.Rule> rules;

    public ToolRulesBuilder(HolderLookup.Provider registries) {
        registry = registries.lookupOrThrow(Registries.BLOCK);
        rules = new ArrayList<>();
    }

    public List<Tool.Rule> build() {
        return rules;
    }

    public ToolRulesBuilder add(TagKey<Block> key, @Nullable Float speed, @Nullable Boolean correctForDrops) {
        rules.add(new Tool.Rule(registry.getOrThrow(key), Optional.ofNullable(speed), Optional.ofNullable(correctForDrops)));
        return this;
    }
}
