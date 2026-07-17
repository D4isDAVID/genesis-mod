package dev.d4vid.mods.genesis.server.custom.item.util;

import dev.d4vid.mods.genesis.server.Genesis;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItem;
import dev.d4vid.mods.genesis.server.custom.item.GenesisItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import java.util.*;

public class CraftingManager {
    private static final Set<Identifier> DRAGON_IDS = new HashSet<>();
    private static MinecraftServer server;

    public static void initialize(MinecraftServer s) {
        server = s;
    }

    public static void registerDragonItem(Identifier id) {
        DRAGON_IDS.add(id);
    }

    public static boolean isDragonItem(ItemStack stack) {
        Identifier id = GenesisItem.getId(stack);
        return id != null && DRAGON_IDS.contains(id);
    }

    public static List<RecipeHolder<?>> buildRecipes() {
        List<RecipeHolder<?>> recipes = new ArrayList<>();
        if (server == null) return recipes;

        RegistryAccess registries = server.registryAccess();

        for (GenesisItem item : GenesisItems.REGISTRY.values()) {
            String[] pattern = item.getRecipePattern();
            Map<Character, String> ingredients = item.getRecipeIngredients();
            if (pattern == null || ingredients == null) continue;

            Map<Character, Ingredient> ingredientMap = new HashMap<>();
            for (Map.Entry<Character, String> entry : ingredients.entrySet()) {
                var ingredientItem = BuiltInRegistries.ITEM.getValue(Identifier.parse(entry.getValue()));
                ingredientMap.put(entry.getKey(), Ingredient.of(ingredientItem));
            }

            ShapedRecipePattern shapedPattern = ShapedRecipePattern.of(ingredientMap, Arrays.asList(pattern));
            ItemStack result = item.assemble(registries);
            ShapedRecipe recipe = new ShapedRecipe("", CraftingBookCategory.MISC, shapedPattern, result);

            Identifier recipeId = Identifier.fromNamespaceAndPath(Genesis.MOD_ID, item.getId().getPath());
            ResourceKey<Recipe<?>> recipeKey = ResourceKey.create(Registries.RECIPE, recipeId);
            recipes.add(new RecipeHolder<>(recipeKey, recipe));
        }

        return recipes;
    }
}
