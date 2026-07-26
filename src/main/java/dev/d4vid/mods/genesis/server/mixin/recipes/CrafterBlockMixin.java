package dev.d4vid.mods.genesis.server.mixin.recipes;

import dev.d4vid.mods.genesis.server.custom.item.util.UltimateManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Mixin(CrafterBlock.class)
public class CrafterBlockMixin {
    @Inject(method = "dispenseFrom", at = @At("HEAD"), cancellable = true)
    private void genesis$noCrafterUltimates(
        BlockState state, ServerLevel level, BlockPos pos, CallbackInfo ci
    ) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof CrafterBlockEntity crafter)) return;

        CraftingInput input = crafter.asCraftInput();
        CrafterBlock.getPotentialResults(level, input).ifPresent(match -> {
            ItemStack result = match.value().assemble(input, level.registryAccess());
            if (UltimateManager.isUltimate(result)) ci.cancel();
        });
    }
}
