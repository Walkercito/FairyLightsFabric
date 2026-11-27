package de.lucalabs.fairylights.data;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.FairyLightCraftingRecipes;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;

public final class FairyLightRecipeProvider extends FabricRecipeProvider {

    @Override
    public String getName() {
        return "Fairy Lights Recipes";
    }

    public FairyLightRecipeProvider(final FabricPackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(final HolderLookup.Provider registries, final RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {
                final HolderGetter<Item> items = registries.lookupOrThrow(Registries.ITEM);

                ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, FairyLightItems.INCANDESCENT_LIGHT, 4)
                        .pattern(" I ")
                        .pattern("ITI")
                        .pattern(" G ")
                        .define('I', Items.IRON_INGOT)
                        .define('G', Items.GLASS_PANE)
                        .define('T', Items.TORCH)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_torch", has(Items.TORCH))
                        .save(output, key("incandescent_light"));

                ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, FairyLightItems.OIL_LANTERN, 4)
                        .pattern(" I ")
                        .pattern("STS")
                        .pattern("IGI")
                        .define('I', Items.IRON_INGOT)
                        .define('G', Items.GLASS_PANE)
                        .define('T', Items.TORCH)
                        .define('S', Items.STICK)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_torch", has(Items.TORCH))
                        .save(output, key("oil_lantern"));

                ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, FairyLightItems.CANDLE_LANTERN, 4)
                        .pattern(" I ")
                        .pattern("GTG")
                        .pattern("IGI")
                        .define('I', Items.IRON_INGOT)
                        .define('G', Items.GOLD_NUGGET)
                        .define('T', Items.TORCH)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_torch", has(Items.TORCH))
                        .save(output, key("candle_lantern"));

                ShapedRecipeBuilder.shaped(items, RecipeCategory.DECORATIONS, FairyLightItems.GARLAND, 2)
                        .pattern("I-I")
                        .define('I', Items.IRON_INGOT)
                        .define('-', Items.VINE)
                        .unlockedBy("has_iron", has(Items.IRON_INGOT))
                        .unlockedBy("has_vine", has(Items.VINE))
                        .save(output, key("garland"));

            }
        };
    }

    private static ResourceKey<Recipe<?>> key(final String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(FairyLights.ID, name));
    }
}
