package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.HangingLightsConnectionItem;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.items.crafting.ingredient.BasicAuxiliaryIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.BasicRegularIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.InertBasicAuxiliaryIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.LazyTagIngredient;
import de.lucalabs.fairylights.string.StringTypes;
import de.lucalabs.fairylights.util.Blender;
import de.lucalabs.fairylights.util.OreDictUtils;
import de.lucalabs.fairylights.util.Tags;
import de.lucalabs.fairylights.util.Utils;
import de.lucalabs.fairylights.util.styled.StyledString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class FairyLightCraftingRecipes {

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS = special(
            "crafting_special_hanging_lights",
            FairyLightCraftingRecipes::createHangingLights);

    public static final RecipeSerializer<GenericRecipe> HANGING_LIGHTS_AUGMENTATION = special(
            "crafting_special_hanging_lights_augmentation",
            FairyLightCraftingRecipes::createHangingLightsAugmentation);

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING = special(
            "crafting_special_pennant_bunting",
            FairyLightCraftingRecipes::createPennantBunting);

    public static final RecipeSerializer<GenericRecipe> PENNANT_BUNTING_AUGMENTATION = special(
            "crafting_special_pennant_bunting_augmentation",
            FairyLightCraftingRecipes::createPennantBuntingAugmentation);

    public static final RecipeSerializer<GenericRecipe> TINSEL_GARLAND = special(
            "crafting_special_tinsel_garland",
            FairyLightCraftingRecipes::createTinselGarland);

    public static final RecipeSerializer<GenericRecipe> TRIANGLE_PENNANT = special(
            "crafting_special_triangle_pennant",
            FairyLightCraftingRecipes::createTrianglePennant);

    public static final RecipeSerializer<GenericRecipe> SQUARE_PENNANT = special(
            "crafting_special_square_pennant",
            FairyLightCraftingRecipes::createSquarePennant);

    public static final RecipeSerializer<GenericRecipe> FAIRY_LIGHT = special(
            "crafting_special_fairy_light",
            FairyLightCraftingRecipes::createFairyLight);

    public static final RecipeSerializer<GenericRecipe> PAPER_LANTERN = special(
            "crafting_special_paper_lantern",
            FairyLightCraftingRecipes::createPaperLantern);

    public static final RecipeSerializer<GenericRecipe> ORB_LANTERN = special(
            "crafting_special_orb_lantern",
            FairyLightCraftingRecipes::createOrbLantern);

    public static final RecipeSerializer<GenericRecipe> FLOWER_LIGHT = special(
            "crafting_special_flower_light",
            FairyLightCraftingRecipes::createFlowerLight);

    public static final RecipeSerializer<GenericRecipe> CANDLE_LANTERN_LIGHT = special(
            "crafting_special_candle_lantern_light",
            FairyLightCraftingRecipes::createCandleLanternLight);

    public static final RecipeSerializer<GenericRecipe> JACK_O_LANTERN = special(
            "crafting_special_jack_o_lantern",
            FairyLightCraftingRecipes::createJackOLantern);

    public static final RecipeSerializer<GenericRecipe> SKULL_LIGHT = special(
            "crafting_special_skull_light",
            FairyLightCraftingRecipes::createSkullLight);

    public static final RecipeSerializer<GenericRecipe> GHOST_LIGHT = special(
            "crafting_special_ghost_light",
            FairyLightCraftingRecipes::createGhostLight);

    public static final RecipeSerializer<GenericRecipe> SPIDER_LIGHT = special(
            "crafting_special_spider_light",
            FairyLightCraftingRecipes::createSpiderLight);

    public static final RecipeSerializer<GenericRecipe> WITCH_LIGHT = special(
            "crafting_special_witch_light",
            FairyLightCraftingRecipes::createWitchLight);

    public static final RecipeSerializer<GenericRecipe> SNOWFLAKE_LIGHT = special(
            "crafting_special_snowflake_light",
            FairyLightCraftingRecipes::createSnowflakeLight);

    public static final RecipeSerializer<GenericRecipe> HEART_LIGHT = special(
            "crafting_special_heart_light",
            FairyLightCraftingRecipes::createHeartLight);

    public static final RecipeSerializer<GenericRecipe> MOON_LIGHT = special(
            "crafting_special_moon_light",
            FairyLightCraftingRecipes::createMoonLight);

    public static final RecipeSerializer<GenericRecipe> STAR_LIGHT = special(
            "crafting_special_star_light",
            FairyLightCraftingRecipes::createStarLight);

    public static final RecipeSerializer<GenericRecipe> ICICLE_LIGHTS = special(
            "crafting_special_icicle_lights",
            FairyLightCraftingRecipes::createIcicleLights);

    public static final RecipeSerializer<GenericRecipe> METEOR_LIGHT = special(
            "crafting_special_meteor_light",
            FairyLightCraftingRecipes::createMeteorLight);

    public static final RecipeSerializer<GenericRecipe> LIGHT_TWINKLE = special(
            "crafting_special_light_twinkle",
            FairyLightCraftingRecipes::createLightTwinkle);

    public static final RecipeSerializer<GenericRecipe> COLOR_CHANGING_LIGHT = special(
            "crafting_special_color_changing_light",
            FairyLightCraftingRecipes::createColorChangingLight);

    public static final RecipeSerializer<GenericRecipe> EDIT_COLOR = special(
            "crafting_special_edit_color",
            FairyLightCraftingRecipes::createDyeColor);

    public static final RecipeSerializer<CopyColorRecipe> COPY_COLOR = register(
            "crafting_special_copy_color",
            () -> LazyRecipeSerializer.create(CopyColorRecipe::new));

    public static RegularIngredient dyeSubtypeIngredient() {
        return new BasicRegularIngredient(LazyTagIngredient.of(Tags.DYES)) {
            @Override
            public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                return DyeableItem.getDyeColor(output).map(dye -> ImmutableList.of(OreDictUtils.getDyes(dye))).orElse(ImmutableList.of());
            }

            @Override
            public boolean dictatesOutputType() {
                return true;
            }

            @Override
            public void matched(final ItemStack ingredient, final ItemStack output) {
                DyeableItem.setColor(output, OreDictUtils.getDyeColor(ingredient));
            }
        };
    }

    private FairyLightCraftingRecipes() {}


    private static GenericRecipe createDyeColor(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> EDIT_COLOR)
                .withShape("I")
                .withIngredient('I', Tags.DYEABLE).withOutput('I')
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<Blender>(LazyTagIngredient.of(Tags.DYES), true, 8) {
                    @Override
                    public Blender accumulator() {
                        return new Blender();
                    }

                    @Override
                    public void consume(final Blender data, final ItemStack ingredient) {
                        data.add(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
                    }

                    @Override
                    public boolean finish(final Blender data, final ItemStack output) {
                        DyeableItem.setColor(output, data.blend());
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createLightTwinkle(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> LIGHT_TWINKLE)
                .withShape("L")
                .withIngredient('L', Tags.TWINKLING_LIGHTS).withOutput('L')
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(Ingredient.of(Items.GLOWSTONE_DUST), true, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return useInputsForTagBool(output, "twinkle", true) ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final ItemStack output) {
                        output.set(FairyLightItemComponents.TWINKLE, true);
                    }

                    @Override
                    public void absent(final ItemStack output) {
                        output.set(FairyLightItemComponents.TWINKLE, false);
                    }

                    @Override
                    public void addTooltip(final List<Component> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.twinkling_lights.glowstone"));
                    }
                })
                .build();
    }

    private static GenericRecipe createColorChangingLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> COLOR_CHANGING_LIGHT)
                .withShape("IG")
                .withIngredient('I', Tags.DYEABLE_LIGHTS).withOutput('I')
                .withIngredient('G', Items.GOLD_NUGGET)
                .withAuxiliaryIngredient(new BasicAuxiliaryIngredient<List<Integer>>(LazyTagIngredient.of(Tags.DYES), true, 8) {
                    @Override
                    public List<Integer> accumulator() {
                        return new ArrayList<>();
                    }

                    @Override
                    public void consume(final List<Integer> data, final ItemStack ingredient) {
                        data.add(DyeableItem.getColor(OreDictUtils.getDyeColor(ingredient)));
                    }

                    @Override
                    public boolean finish(final List<Integer> data, final ItemStack output) {
                        if (!data.isEmpty()) {
                            if (output.has(net.minecraft.core.component.DataComponents.DYED_COLOR)) {
                                data.add(0, net.minecraft.world.item.component.DyedItemColor.getOrDefault(output, 0xFFFFFFFF));
                                output.remove(net.minecraft.core.component.DataComponents.DYED_COLOR);
                            }
                            output.set(FairyLightItemComponents.COLORS, data);
                        }
                        return false;
                    }
                })
                .build();
    }

    private static GenericRecipe createHangingLights(Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS, FairyLightItems.HANGING_LIGHTS)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new LightIngredient(true))
                .withAuxiliaryIngredient(new InertBasicAuxiliaryIngredient(LazyTagIngredient.of(Tags.DYES_WHITE), false, 1) {
                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return HangingLightsConnectionItem.getString(output) == StringTypes.WHITE_STRING ? super.getInput(output) : ImmutableList.of();
                    }

                    @Override
                    public void present(final ItemStack output) {
                        HangingLightsConnectionItem.setString(output, StringTypes.WHITE_STRING);
                    }

                    @Override
                    public void absent(final ItemStack output) {
                        HangingLightsConnectionItem.setString(output, StringTypes.BLACK_STRING);
                    }

                    @Override
                    public void addTooltip(final List<Component> tooltip) {
                        super.addTooltip(tooltip);
                        tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.string"));
                    }
                })
                .build();
    }

    private static boolean useInputsForTagBool(final ItemStack output, final String key, final boolean value) {
        return "twinkle".equals(key) && output.getOrDefault(FairyLightItemComponents.TWINKLE, false) == value;
    }

    /*
     *  The JEI shown recipe is adding glowstone, eventually I should allow a recipe to provide a number of
     *  different recipe layouts the the input ingredients can be generated for so I could show applying a
     *  new light pattern as well.
     */
    private static GenericRecipe createHangingLightsAugmentation(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> HANGING_LIGHTS_AUGMENTATION, FairyLightItems.HANGING_LIGHTS)
                .withShape("F")
                .withIngredient('F', new BasicRegularIngredient(Ingredient.of(FairyLightItems.HANGING_LIGHTS)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Ingredients.displayStacks(this.ingredient).stream()
                                .map(ItemStack::copy)
                                .flatMap(stack -> makeHangingLightsExamples(stack).stream())
                                .collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        if (!output.has(FairyLightItemComponents.PATTERN)) {
                            return ImmutableList.of();
                        }
                        return ImmutableList.of(ImmutableList.of(output.copyWithCount(1)));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final ItemStack output) {
                        output.applyComponents(ingredient.getComponentsPatch());
                    }
                })
                .withAuxiliaryIngredient(new LightIngredient(true) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return ImmutableList.of();
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        return ImmutableList.of();
                    }
                })
                .build();
    }

    private static ImmutableList<ItemStack> makeHangingLightsExamples(final ItemStack stack) {
        return ImmutableList.of(
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.MAGENTA, DyeColor.CYAN, DyeColor.WHITE),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.LIGHT_BLUE, DyeColor.CYAN, DyeColor.LIGHT_BLUE),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PINK, DyeColor.CYAN, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.LIGHT_GRAY, DyeColor.PURPLE, DyeColor.LIGHT_GRAY, DyeColor.GREEN),
                makeHangingLights(stack, DyeColor.CYAN, DyeColor.YELLOW, DyeColor.CYAN, DyeColor.PURPLE)
        );
    }

    public static ItemStack makeHangingLights(final ItemStack base, final DyeColor... colors) {
        final ItemStack stack = base.copy();
        final List<ItemStack> lights = new ArrayList<>();
        for (final DyeColor color : colors) {
            lights.add(DyeableItem.setColor(new ItemStack(FairyLightItems.FAIRY_LIGHT), color));
        }
        stack.set(FairyLightItemComponents.PATTERN, lights);
        HangingLightsConnectionItem.setString(stack, StringTypes.BLACK_STRING);
        return stack;
    }


    private static GenericRecipe createPennantBunting(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING, FairyLightItems.PENNANT_BUNTING)
                .withShape("I-I")
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static GenericRecipe createTinselGarland(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> TINSEL_GARLAND, FairyLightItems.TINSEL)
                .withShape(" P ", "I-I", " D ")
                .withIngredient('P', Items.PAPER)
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', dyeSubtypeIngredient())
                .build();
    }

    private static GenericRecipe createPennantBuntingAugmentation(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return new GenericRecipeBuilder(name, () -> PENNANT_BUNTING_AUGMENTATION, FairyLightItems.PENNANT_BUNTING)
                .withShape("B")
                .withIngredient('B', new BasicRegularIngredient(Ingredient.of(FairyLightItems.PENNANT_BUNTING)) {
                    @Override
                    public ImmutableList<ItemStack> getInputs() {
                        return Ingredients.displayStacks(this.ingredient).stream()
                                .map(ItemStack::copy)
                                .flatMap(stack -> makePennantExamples(stack).stream())
                                .collect(ImmutableList.toImmutableList());
                    }

                    @Override
                    public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
                        if (!output.has(FairyLightItemComponents.PATTERN)) {
                            return ImmutableList.of();
                        }
                        return ImmutableList.of(makePennantExamples(output));
                    }

                    @Override
                    public void matched(final ItemStack ingredient, final ItemStack output) {
                        output.applyComponents(ingredient.getComponentsPatch());
                    }
                })
                .withAuxiliaryIngredient(new PennantIngredient())
                .build();
    }

    private static ImmutableList<ItemStack> makePennantExamples(final ItemStack stack) {
        return ImmutableList.of(
                makePennant(stack, DyeColor.BLUE, DyeColor.YELLOW, DyeColor.RED),
                makePennant(stack, DyeColor.PINK, DyeColor.LIGHT_BLUE),
                makePennant(stack, DyeColor.ORANGE, DyeColor.WHITE),
                makePennant(stack, DyeColor.LIME, DyeColor.YELLOW)
        );
    }

    public static ItemStack makePennant(final ItemStack base, final DyeColor... colors) {
        final ItemStack stack = base.copy();
        final List<ItemStack> pennants = new ArrayList<>();
        for (final DyeColor color : colors) {
            final ItemStack pennant = new ItemStack(FairyLightItems.TRIANGLE_PENNANT);
            DyeableItem.setColor(pennant, color);
            pennants.add(pennant);
        }
        stack.set(FairyLightItemComponents.PATTERN, pennants);
        stack.set(FairyLightItemComponents.TEXT, StyledString.serialize(new StyledString()));
        return stack;
    }

    private static GenericRecipe createPennant(final Identifier name, final Supplier<RecipeSerializer<GenericRecipe>> serializer, final Item item, final String pattern) {
        return new GenericRecipeBuilder(name, serializer, item)
                .withShape("- -", "PDP", pattern)
                .withIngredient('P', Items.PAPER)
                .withIngredient('-', Items.STRING)
                .withIngredient('D', dyeSubtypeIngredient())
                .build();
    }

    private static GenericRecipe createTrianglePennant(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createPennant(name, () -> TRIANGLE_PENNANT, FairyLightItems.TRIANGLE_PENNANT, " P ");
    }

    private static GenericRecipe createSquarePennant(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createPennant(name, () -> SQUARE_PENNANT, FairyLightItems.SQUARE_PENNANT, "PPP");
    }

    private static GenericRecipe createFairyLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> FAIRY_LIGHT, () -> FairyLightItems.FAIRY_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.GLASS_PANE)
        );
    }

    private static GenericRecipe createPaperLantern(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> PAPER_LANTERN, () -> FairyLightItems.PAPER_LANTERN, b -> b
                .withShape(" I ", "PDP", "PPP")
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createOrbLantern(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> ORB_LANTERN, () -> FairyLightItems.ORB_LANTERN, b -> b
                .withShape(" I ", "SDS", " W ")
                .withIngredient('S', Items.STRING)
                .withIngredient('W', Items.WHITE_WOOL)
        );
    }

    private static GenericRecipe createFlowerLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> FLOWER_LIGHT, () -> FairyLightItems.FLOWER_LIGHT, b -> b
                .withShape(" I ", "RDB", " Y ")
                .withIngredient('R', Items.POPPY)
                .withIngredient('Y', Items.DANDELION)
                .withIngredient('B', Items.BLUE_ORCHID)
        );
    }

    private static GenericRecipe createCandleLanternLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> CANDLE_LANTERN_LIGHT, () -> FairyLightItems.CANDLE_LANTERN_LIGHT, b -> b
                .withShape(" I ", "GDG", "IGI")
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createJackOLantern(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> JACK_O_LANTERN, () -> FairyLightItems.JACK_O_LANTERN, b -> b
                .withShape(" I ", "SDS", "GPG")
                .withIngredient('S', ItemTags.WOODEN_SLABS)
                .withIngredient('G', Items.TORCH)
                .withIngredient('P', Items.JACK_O_LANTERN)
        );
    }

    private static GenericRecipe createSkullLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SKULL_LIGHT, () -> FairyLightItems.SKULL_LIGHT, b -> b
                .withShape(" I ", "IDI", " B ")
                .withIngredient('B', Items.BONE)
        );
    }

    private static GenericRecipe createGhostLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> GHOST_LIGHT, () -> FairyLightItems.GHOST_LIGHT, b -> b
                .withShape(" I ", "PDP", "IGI")
                .withIngredient('P', Items.PAPER)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createSpiderLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SPIDER_LIGHT, () -> FairyLightItems.SPIDER_LIGHT, b -> b
                .withShape(" I ", "WDW", "SES")
                .withIngredient('W', Items.COBWEB)
                .withIngredient('S', Items.STRING)
                .withIngredient('E', Items.SPIDER_EYE)
        );
    }

    private static GenericRecipe createWitchLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> WITCH_LIGHT, () -> FairyLightItems.WITCH_LIGHT, b -> b
                .withShape(" I ", "BDW", " S ")
                .withIngredient('B', Items.GLASS_BOTTLE)
                .withIngredient('W', Items.WHEAT)
                .withIngredient('S', Items.STICK)
        );
    }

    private static GenericRecipe createSnowflakeLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> SNOWFLAKE_LIGHT, () -> FairyLightItems.SNOWFLAKE_LIGHT, b -> b
                .withShape(" I ", "SDS", " G ")
                .withIngredient('S', Items.SNOWBALL)
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createHeartLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> HEART_LIGHT, () -> FairyLightItems.HEART_LIGHT, b -> b
                .withShape(" I ", "IDI", " G ")
                .withIngredient('G', Items.RED_STAINED_GLASS_PANE)
        );
    }

    private static GenericRecipe createMoonLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> MOON_LIGHT, () -> FairyLightItems.MOON_LIGHT, b -> b
                .withShape(" I ", "GDG", " C ")
                .withIngredient('G', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('C', Items.CLOCK)
        );
    }


    private static GenericRecipe createStarLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> STAR_LIGHT, () -> FairyLightItems.STAR_LIGHT, b -> b
                .withShape(" I ", "PDP", " G ")
                .withIngredient('P', Items.WHITE_STAINED_GLASS_PANE)
                .withIngredient('G', Items.GOLD_NUGGET)
        );
    }

    private static GenericRecipe createIcicleLights(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> ICICLE_LIGHTS, () -> FairyLightItems.ICICLE_LIGHTS, b -> b
                .withShape(" I ", "GDG", " B ")
                .withIngredient('G', Items.GLASS_PANE)
                .withIngredient('B', Items.WATER_BUCKET)
        );
    }

    private static GenericRecipe createMeteorLight(final Identifier name, CraftingBookCategory craftingBookCategory) {
        return createLight(name, () -> METEOR_LIGHT, () -> FairyLightItems.METEOR_LIGHT, b -> b
                .withShape(" I ", "GDG", "IPI")
                .withIngredient('G', Items.GLOWSTONE_DUST)
                .withIngredient('P', Items.PAPER)
        );
    }

    private static GenericRecipe createLight(final Identifier name, final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer, final Supplier<? extends Item> variant, final UnaryOperator<GenericRecipeBuilder> recipe) {
        return recipe.apply(new GenericRecipeBuilder(name, serializer))
                .withIngredient('I', Items.IRON_INGOT)
                .withIngredient('D', dyeSubtypeIngredient())
                .withOutput(variant.get(), 4)
                .build();
    }

    private static class LightIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private LightIngredient(final boolean isRequired) {
            super(LazyTagIngredient.of(Tags.LIGHTS), isRequired, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(FairyLightItemComponents.PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            return pattern.stream().map(ImmutableList::of).collect(ImmutableList.toImmutableList());
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return new ArrayList<>();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient.copyWithCount(1));
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final ItemStack output) {
            if (!pattern.isEmpty()) {
                output.set(FairyLightItemComponents.PATTERN, pattern);
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Component> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.hangingLights.light"));
        }
    }

    private static class PennantIngredient extends BasicAuxiliaryIngredient<List<ItemStack>> {
        private PennantIngredient() {
            super(LazyTagIngredient.of(Tags.PENNANTS), true, 8);
        }

        @Override
        public ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
            final List<ItemStack> pattern = output.get(FairyLightItemComponents.PATTERN);
            if (pattern == null || pattern.isEmpty()) {
                return ImmutableList.of();
            }
            return pattern.stream().map(ImmutableList::of).collect(ImmutableList.toImmutableList());
        }

        @Override
        public boolean dictatesOutputType() {
            return true;
        }

        @Override
        public List<ItemStack> accumulator() {
            return new ArrayList<>();
        }

        @Override
        public void consume(final List<ItemStack> patternList, final ItemStack ingredient) {
            patternList.add(ingredient.copyWithCount(1));
        }

        @Override
        public boolean finish(final List<ItemStack> pattern, final ItemStack output) {
            if (!pattern.isEmpty()) {
                output.set(FairyLightItemComponents.PATTERN, pattern);
                output.set(FairyLightItemComponents.TEXT, StyledString.serialize(new StyledString()));
            }
            return false;
        }

        @Override
        public void addTooltip(final List<Component> tooltip) {
            tooltip.add(Utils.formatRecipeTooltip("recipe.fairylights.pennantBunting.pennant"));
        }
    }


    public static void generate(final RecipeOutput output) {
        final CraftingBookCategory category = CraftingBookCategory.MISC;
        accept(output, "hanging_lights", createHangingLights(id("hanging_lights"), category));
        accept(output, "hanging_lights_augmentation", createHangingLightsAugmentation(id("hanging_lights_augmentation"), category));
        accept(output, "pennant_bunting", createPennantBunting(id("pennant_bunting"), category));
        accept(output, "tinsel_garland", createTinselGarland(id("tinsel_garland"), category));
        accept(output, "pennant_bunting_augmentation", createPennantBuntingAugmentation(id("pennant_bunting_augmentation"), category));
        accept(output, "edit_color", createDyeColor(id("edit_color"), category));
        accept(output, "copy_color", new CopyColorRecipe());
        accept(output, "triangle_pennant", createTrianglePennant(id("triangle_pennant"), category));
        accept(output, "square_pennant", createSquarePennant(id("square_pennant"), category));
        accept(output, "fairy_light", createFairyLight(id("fairy_light"), category));
        accept(output, "paper_lantern", createPaperLantern(id("paper_lantern"), category));
        accept(output, "orb_lantern", createOrbLantern(id("orb_lantern"), category));
        accept(output, "flower_light", createFlowerLight(id("flower_light"), category));
        accept(output, "candle_lantern_light", createCandleLanternLight(id("candle_lantern_light"), category));
        accept(output, "jack_o_lantern", createJackOLantern(id("jack_o_lantern"), category));
        accept(output, "skull_light", createSkullLight(id("skull_light"), category));
        accept(output, "ghost_light", createGhostLight(id("ghost_light"), category));
        accept(output, "spider_light", createSpiderLight(id("spider_light"), category));
        accept(output, "witch_light", createWitchLight(id("witch_light"), category));
        accept(output, "snowflake_light", createSnowflakeLight(id("snowflake_light"), category));
        accept(output, "heart_light", createHeartLight(id("heart_light"), category));
        accept(output, "moon_light", createMoonLight(id("moon_light"), category));
        accept(output, "star_light", createStarLight(id("star_light"), category));
        accept(output, "icicle_lights", createIcicleLights(id("icicle_lights"), category));
        accept(output, "meteor_light", createMeteorLight(id("meteor_light"), category));
        accept(output, "light_twinkle", createLightTwinkle(id("light_twinkle"), category));
        accept(output, "color_changing_light", createColorChangingLight(id("color_changing_light"), category));
    }

    private static Identifier id(final String name) {
        return Identifier.fromNamespaceAndPath(FairyLights.ID, name);
    }

    private static void accept(final RecipeOutput output, final String name, final Recipe<?> recipe) {
        output.accept(ResourceKey.create(Registries.RECIPE, id(name)), recipe, null);
    }

    private static RecipeSerializer<GenericRecipe> special(
            final String name,
            final BiFunction<Identifier, CraftingBookCategory, GenericRecipe> factory) {
        final Identifier id = Identifier.tryBuild(FairyLights.ID, name);
        final RecipeSerializer<GenericRecipe>[] holder = new RecipeSerializer[1];
        final RecipeSerializer<GenericRecipe> serializer = LazyRecipeSerializer.create(
                () -> GenericRecipe.lazy(() -> factory.apply(id, CraftingBookCategory.MISC), () -> holder[0]));
        holder[0] = serializer;
        return register(name, () -> serializer);
    }

    private static <T extends RecipeSerializer<?>> T register(final String name, Supplier<T> supplier) {
        Identifier identifier = Identifier.tryBuild(FairyLights.ID, name);
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, identifier, supplier.get());
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering items");
    }
}
