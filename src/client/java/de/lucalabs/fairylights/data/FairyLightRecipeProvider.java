package de.lucalabs.fairylights.data;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.crafting.FairyLightCraftingRecipes;
import de.lucalabs.fairylights.util.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import java.util.function.Consumer;

public final class FairyLightRecipeProvider extends FabricRecipeProvider {
    public FairyLightRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.INCANDESCENT_LIGHT, 4)
                .pattern(" I ")
                .pattern("ITI")
                .pattern(" G ")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', Items.TORCH)
                .criterion("has_iron", has(Items.IRON_INGOT))
                .criterion("has_torch", has(Items.TORCH))
                .offerTo(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.OIL_LANTERN, 4)
                .pattern(" I ")
                .pattern("STS")
                .pattern("IGI")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GLASS_PANE)
                .input('T', Items.TORCH)
                .input('S', Items.STICK)
                .criterion("has_iron", has(Items.IRON_INGOT))
                .criterion("has_torch", has(Items.TORCH))
                .offerTo(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.CANDLE_LANTERN, 4)
                .pattern(" I ")
                .pattern("GTG")
                .pattern("IGI")
                .input('I', Items.IRON_INGOT)
                .input('G', Items.GOLD_NUGGET)
                .input('T', Items.TORCH)
                .criterion("has_iron", has(Items.IRON_INGOT))
                .criterion("has_torch", has(Items.TORCH))
                .offerTo(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, FairyLightItems.GARLAND, 2)
                .pattern("I-I")
                .input('I', Items.IRON_INGOT)
                .input('-', Items.VINE)
                .criterion("has_iron", has(Items.IRON_INGOT))
                .criterion("has_vine", has(Items.VINE))
                .offerTo(consumer);

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.HANGING_LIGHTS)
                .unlockedBy("has_lights", has(Tags.LIGHTS))
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "hanging_lights"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.HANGING_LIGHTS_AUGMENTATION)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "hanging_lights_augmentation"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.PENNANT_BUNTING)
                .unlockedBy("has_pennants", has(Tags.PENNANTS))
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "pennant_bunting"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.TINSEL_GARLAND)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_string", has(Items.STRING))
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "tinsel_garland"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.PENNANT_BUNTING_AUGMENTATION)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "pennant_bunting_augmentation"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.EDIT_COLOR)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "edit_color"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.COPY_COLOR)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "copy_color"));

        this.pennantRecipe(FairyLightCraftingRecipes.TRIANGLE_PENNANT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "triangle_pennant"));

        this.pennantRecipe(FairyLightCraftingRecipes.SQUARE_PENNANT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "square_pennant"));

        this.lightRecipe(FairyLightCraftingRecipes.FAIRY_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "fairy_light"));
        this.lightRecipe(FairyLightCraftingRecipes.PAPER_LANTERN)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "paper_lantern"));
        this.lightRecipe(FairyLightCraftingRecipes.ORB_LANTERN)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "orb_lantern"));
        this.lightRecipe(FairyLightCraftingRecipes.FLOWER_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "flower_light"));
        this.lightRecipe(FairyLightCraftingRecipes.CANDLE_LANTERN_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "candle_lantern_light"));
        this.lightRecipe(FairyLightCraftingRecipes.JACK_O_LANTERN)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "jack_o_lantern"));
        this.lightRecipe(FairyLightCraftingRecipes.SKULL_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "skull_light"));
        this.lightRecipe(FairyLightCraftingRecipes.GHOST_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "ghost_light"));
        this.lightRecipe(FairyLightCraftingRecipes.SPIDER_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "spider_light"));
        this.lightRecipe(FairyLightCraftingRecipes.WITCH_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "witch_light"));
        this.lightRecipe(FairyLightCraftingRecipes.SNOWFLAKE_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "snowflake_light"));
        this.lightRecipe(FairyLightCraftingRecipes.HEART_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "heart_light"));
        this.lightRecipe(FairyLightCraftingRecipes.MOON_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "moon_light"));
        this.lightRecipe(FairyLightCraftingRecipes.STAR_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "star_light"));
        this.lightRecipe(FairyLightCraftingRecipes.ICICLE_LIGHTS)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "icicle_lights"));
        this.lightRecipe(FairyLightCraftingRecipes.METEOR_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "meteor_light"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.LIGHT_TWINKLE)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "light_twinkle"));

        GenericRecipeBuilder.customRecipe(FairyLightCraftingRecipes.COLOR_CHANGING_LIGHT)
                .build(consumer, Identifier.fromNamespaceAndPath(FairyLights.ID, "color_changing_light"));
    }

    GenericRecipeBuilder lightRecipe(final RecipeSerializer<?> serializer) {
        return GenericRecipeBuilder.customRecipe(serializer)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .unlockedBy("has_dye", has(Tags.DYES));
    }

    GenericRecipeBuilder pennantRecipe(final RecipeSerializer<?> serializer) {
        return GenericRecipeBuilder.customRecipe(serializer)
                .unlockedBy("has_paper", has(Items.PAPER))
                .unlockedBy("has_string", has(Items.STRING));
    }
}
