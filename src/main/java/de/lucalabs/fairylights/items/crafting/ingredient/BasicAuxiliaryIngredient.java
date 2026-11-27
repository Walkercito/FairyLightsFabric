package de.lucalabs.fairylights.items.crafting.ingredient;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import de.lucalabs.fairylights.items.crafting.Ingredients;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public abstract class BasicAuxiliaryIngredient<A> implements AuxiliaryIngredient<A> {
    protected final Ingredient ingredient;
    protected final ImmutableList<ItemStack> displayInputs;
    protected final boolean isRequired;
    protected final int limit;

    public BasicAuxiliaryIngredient(final Ingredient ingredient, final boolean isRequired, final int limit) {
        this(ingredient, isRequired, limit, null);
    }

    public BasicAuxiliaryIngredient(final Ingredient ingredient, final boolean isRequired, final int limit, @Nullable final List<ItemStack> displayInputs) {
        Preconditions.checkArgument(limit > 0, "limit must be greater than zero");
        this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
        this.displayInputs = displayInputs == null ? null : ImmutableList.copyOf(displayInputs);
        this.isRequired = isRequired;
        this.limit = limit;
    }

    @Override
    public final GenericRecipe.MatchResultAuxiliary matches(final ItemStack input) {
        return new GenericRecipe.MatchResultAuxiliary(this, input, this.ingredient.test(input), Collections.emptyList());
    }

    @Override
    public ImmutableList<ItemStack> getInputs() {
        return this.displayInputs != null ? this.displayInputs : Ingredients.displayStacks(this.ingredient);
    }

    @Override
    public boolean isRequired() {
        return this.isRequired;
    }

    @Override
    public int getLimit() {
        return this.limit;
    }
}
