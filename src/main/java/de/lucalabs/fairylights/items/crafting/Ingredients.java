package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.recipe.v1.ingredient.DefaultCustomIngredients;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public final class Ingredients {

    private Ingredients() {
    }

    public static Ingredient stack(final ItemStack stack) {
        return DefaultCustomIngredients.components(stack.copyWithCount(1));
    }

    public static Ingredient stacks(final ItemStack... stacks) {
        final Ingredient[] ingredients = Arrays.stream(stacks)
                .filter(stack -> !stack.isEmpty())
                .map(Ingredients::stack)
                .toArray(Ingredient[]::new);
        return ingredients.length == 0 ? empty() : DefaultCustomIngredients.any(ingredients);
    }

    public static Ingredient empty() {
        return Ingredient.of(Stream.empty());
    }

    public static ImmutableList<ItemStack> displayStacks(final Ingredient ingredient) {
        return ImmutableList.copyOf(ingredient.items().map(holder -> new ItemStack(holder.value())).toList());
    }
}
