package de.lucalabs.fairylights.items.crafting.ingredient;

import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.items.crafting.Ingredients;
import de.lucalabs.fairylights.items.crafting.RegularIngredient;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public class BasicRegularIngredient implements RegularIngredient {
    protected final Ingredient ingredient;
    protected final ImmutableList<ItemStack> displayInputs;

    public BasicRegularIngredient(final Ingredient ingredient) {
        this(ingredient, null);
    }

    public BasicRegularIngredient(final Ingredient ingredient, @Nullable final List<ItemStack> displayInputs) {
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
        this.displayInputs = displayInputs == null ? null : ImmutableList.copyOf(displayInputs);
    }

    @Override
    public final GenericRecipe.MatchResultRegular matches(final ItemStack input) {
        return new GenericRecipe.MatchResultRegular(this, input, this.ingredient.test(input), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        return this.displayInputs != null ? this.displayInputs : Ingredients.displayStacks(this.ingredient);
    }

    @Override
    public String toString() {
        return this.ingredient.toString();
    }
}
