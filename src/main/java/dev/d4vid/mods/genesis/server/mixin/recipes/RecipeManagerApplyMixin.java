package dev.d4vid.mods.genesis.server.mixin.recipes;

import dev.d4vid.mods.genesis.server.custom.item.util.CraftingManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Mixin(RecipeManager.class)
abstract class RecipeManagerApplyMixin {
    @Shadow
    private RecipeMap recipes;

    @Inject(method = "apply", at = @At("TAIL"))
    private void genesis$apply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo callback) {
        List<RecipeHolder<?>> merged = new ArrayList<>(this.recipes.values());
        merged.addAll(CraftingManager.buildRecipes());
        this.recipes = RecipeMap.create(merged);
    }
}
