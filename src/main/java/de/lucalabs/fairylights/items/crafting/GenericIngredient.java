package de.lucalabs.fairylights.items.crafting;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface GenericIngredient<I extends GenericIngredient<I, M>, M extends GenericRecipe.MatchResult<I, M>> {
    ImmutableList<ItemStack> getInputs();

    default ImmutableList<ImmutableList<ItemStack>> getInput(final ItemStack output) {
        return ImmutableList.of(this.getInputs());
    }

    M matches(final ItemStack input);

    default boolean dictatesOutputType() {
        return false;
    }

    default void present(final ItemStack output) {
    }

    default void absent(final ItemStack output) {
    }

    default ImmutableList<ItemStack> getMatchingSubtypes(final Ingredient stack) {
        Objects.requireNonNull(stack, "stack");
        return Ingredients.displayStacks(stack);
    }

    default void addTooltip(final List<Component> tooltip) {
    }
}
