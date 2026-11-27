package de.lucalabs.fairylights.items.crafting;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.math.IntMath;
import de.lucalabs.fairylights.items.crafting.ingredient.AuxiliaryIngredient;
import de.lucalabs.fairylights.items.crafting.ingredient.EmptyRegularIngredient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class GenericRecipe extends CustomRecipe {
    public static final EmptyRegularIngredient EMPTY = new EmptyRegularIngredient();

    private final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer;
    private final ItemStack output;
    private final RegularIngredient[] ingredients;
    private final AuxiliaryIngredient<?>[] auxiliaryIngredients;

    private final int width;
    private final int height;
    private final int outputIngredient;

    private ItemStack result = ItemStack.EMPTY;
    private int room;

    private final ImmutableList<IntUnaryOperator> xFunctions = ImmutableList.of(IntUnaryOperator.identity(), i -> this.getWidth() - 1 - i);

    protected GenericRecipe() {
        this.serializer = () -> null;
        this.output = ItemStack.EMPTY;
        this.ingredients = new RegularIngredient[0];
        this.auxiliaryIngredients = new AuxiliaryIngredient<?>[0];
        this.width = 0;
        this.height = 0;
        this.outputIngredient = -1;
    }

    public static GenericRecipe lazy(
            final Supplier<GenericRecipe> factory,
            final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer) {
        return new Lazy(factory, serializer);
    }

    GenericRecipe(
            final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer,
            final ItemStack output,
            final RegularIngredient[] ingredients,
            final AuxiliaryIngredient<?>[] auxiliaryIngredients,
            final int width,
            final int height,
            final int outputIngredient) {
        Preconditions.checkArgument(width > 0, "width must be greater than zero");
        Preconditions.checkArgument(height > 0, "height must be greater than zero");
        this.serializer = Objects.requireNonNull(serializer, "serializer");
        this.output = Objects.requireNonNull(output, "output");
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients");
        this.auxiliaryIngredients = checkIngredients(ingredients, Objects.requireNonNull(auxiliaryIngredients, "auxiliaryIngredients"));
        this.width = width;
        this.height = height;
        this.outputIngredient = outputIngredient;
        this.room = -1;
    }

    private int getRoom() {
        if (this.room < 0) {
            int room = 0;
            for (final RegularIngredient ing : this.ingredients) {
                if (ing.getInputs().isEmpty()) {
                    room++;
                }
            }
            for (final AuxiliaryIngredient<?> aux : this.auxiliaryIngredients) {
                if (aux.isRequired()) {
                    room--;
                }
            }
            this.room = room;
        }
        return this.room;
    }

    public NonNullList<Ingredient> getDisplayIngredients() {
        final NonNullList<Ingredient> ingredients = NonNullList.withSize(9, Ingredients.empty());
        for (int i = 0; i < this.ingredients.length; i++) {
            final int x = i % this.width;
            final int y = i / this.width;
            final ItemStack[] stacks = this.ingredients[i].getInputs().toArray(new ItemStack[0]);
            ingredients.set(x + y * 3, Ingredients.stacks(stacks));
        }
        for (int i = 0, slot = 0; slot < ingredients.size(); slot++) {
            final Ingredient ing = ingredients.get(slot);
            if (ing.isEmpty()) {
                while (i < this.auxiliaryIngredients.length) {
                    final AuxiliaryIngredient<?> aux = this.auxiliaryIngredients[i++];
                    if (aux.isRequired()) {
                        final ItemStack[] stacks = aux.getInputs().toArray(new ItemStack[0]);
                        ingredients.set(slot, Ingredients.stacks(stacks));
                        break;
                    }
                }
            }
        }
        return ingredients;
    }

    @Override
    public boolean isSpecial() {
        return this.output.isEmpty();
    }

    public boolean isIgnoredInRecipeBook() {
        return this.output.isEmpty();
    }

    @Override
    public CraftingBookCategory category() {
        return CraftingBookCategory.MISC;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return this.serializer.get();
    }

    public ItemStack getOutput() {
        return this.output.copy();
    }

    public RegularIngredient[] getGenericIngredients() {
        return this.ingredients.clone();
    }

    public AuxiliaryIngredient<?>[] getAuxiliaryIngredients() {
        return this.auxiliaryIngredients.clone();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean matches(final CraftingInput inventory, final Level level) {
        if (!(this.width <= inventory.width()
                && this.height <= inventory.height()
                && (this.getRoom() >= 0 || inventory.width() * inventory.height() - this.width * this.height + this.getRoom() >= 0))) {
            this.result = ItemStack.EMPTY;
            return false;
        }
        final int scanWidth = inventory.width() + 1 - this.width;
        final int scanHeight = inventory.height() + 1 - this.height;
        for (int i = 0, end = scanWidth * scanHeight; i < end; i++) {
            final int x = i % scanWidth;
            final int y = i / scanWidth;
            for (final IntUnaryOperator func : this.xFunctions) {
                final ItemStack result = this.getResult(inventory, x, y, func);
                if (!result.isEmpty()) {
                    this.result = result;
                    return true;
                }
            }
        }
        this.result = ItemStack.EMPTY;
        return false;
    }

    private ItemStack getResult(final CraftingInput inventory, final int originX, final int originY, final IntUnaryOperator funcX) {
        final MatchResultRegular[] match = new MatchResultRegular[this.ingredients.length];
        final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> auxMatchResults = LinkedListMultimap.create();
        final Map<AuxiliaryIngredient<?>, Integer> auxMatchTotals = new HashMap<>();
        final Set<GenericIngredient<?, ?>> presentCalled = new HashSet<>();
        final List<MatchResultAuxiliary> auxResults = new ArrayList<>();
        ItemStack outputIngredientStack = null;
        for (int i = 0, w = inventory.width(), size = w * inventory.height(); i < size; i++) {
            final int x = i % w;
            final int y = i / w;
            final int ingX = x - originX;
            final int ingY = y - originY;
            final ItemStack input = inventory.getItem(i);
            if (this.contains(ingX, ingY)) {
                final int index = funcX.applyAsInt(ingX) + ingY * this.width;
                final RegularIngredient ingredient = this.ingredients[index];
                final MatchResultRegular result = ingredient.matches(input);
                if (!result.doesMatch()) {
                    return ItemStack.EMPTY;
                }
                match[index] = result;
                if (index == this.outputIngredient) {
                    outputIngredientStack = input.copyWithCount(1);
                }
            } else if (!EMPTY.matches(input).doesMatch()) {
                boolean nonAuxiliary = true;
                for (final AuxiliaryIngredient<?> auxiliaryIngredient : this.auxiliaryIngredients) {
                    final MatchResultAuxiliary result = auxiliaryIngredient.matches(input);
                    if (result.doesMatch()) {
                        if (result.isAtLimit(auxMatchTotals.getOrDefault(result.ingredient, 0))) {
                            return ItemStack.EMPTY;
                        }
                        auxMatchTotals.merge(result.ingredient, 1, IntMath::checkedAdd);
                        nonAuxiliary = false;
                        result.propagate(auxMatchResults);
                    }
                    auxResults.add(result);
                }
                if (nonAuxiliary) {
                    return ItemStack.EMPTY;
                }
            }
        }
        final ItemStack output = this.output.isEmpty()
                ? (outputIngredientStack == null ? ItemStack.EMPTY : outputIngredientStack)
                : this.output.copy();
        if (output.isEmpty()) {
            return ItemStack.EMPTY;
        }
        for (final MatchResultRegular result : match) {
            result.forMatch(presentCalled, output);
        }
        for (final MatchResultAuxiliary result : auxResults) {
            result.forMatch(presentCalled, output);
        }
        final Set<GenericIngredient<?, ?>> absentCalled = new HashSet<>();
        for (final MatchResultRegular result : match) {
            result.notifyAbsence(presentCalled, absentCalled, output);
        }
        for (final MatchResultAuxiliary result : auxResults) {
            result.notifyAbsence(presentCalled, absentCalled, output);
        }
        for (final AuxiliaryIngredient<?> ingredient : this.auxiliaryIngredients) {
            if (ingredient.process(auxMatchResults, output)) {
                return ItemStack.EMPTY;
            }
        }
        return output;
    }

    private boolean contains(final int x, final int y) {
        return x >= 0 && y >= 0 && x < this.width && y < this.height;
    }

    @Override
    public ItemStack assemble(final CraftingInput inventory) {
        final ItemStack result = this.result;
        return result.isEmpty() ? result : result.copy();
    }

    public interface MatchResult<I extends GenericIngredient<I, M>, M extends MatchResult<I, M>> {
        I getIngredient();

        ItemStack getInput();

        boolean doesMatch();

        void forMatch(Set<GenericIngredient<?, ?>> called, ItemStack output);

        void notifyAbsence(Set<GenericIngredient<?, ?>> presentCalled, Set<GenericIngredient<?, ?>> absentCalled, ItemStack output);

        M withParent(M parent);
    }

    public static class MatchResultRegular implements MatchResult<RegularIngredient, MatchResultRegular> {
        protected final RegularIngredient ingredient;
        protected final ItemStack input;
        protected final boolean doesMatch;
        protected final ImmutableList<MatchResultRegular> supplementaryResults;

        public MatchResultRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final RegularIngredient getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final ItemStack output) {
            this.ingredient.matched(this.input, output);
            if (called.add(this.ingredient)) {
                this.ingredient.present(output);
            }
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final ItemStack output) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(output);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultRegular result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, output);
            }
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }
    }

    public static class MatchResultParentedRegular extends MatchResultRegular {
        protected final MatchResultRegular parent;

        public MatchResultParentedRegular(final RegularIngredient ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultRegular> supplementaryResults, final MatchResultRegular parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final ItemStack output) {
            super.forMatch(called, output);
            this.parent.forMatch(called, output);
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final ItemStack output) {
            super.notifyAbsence(presentCalled, absentCalled, output);
            this.parent.notifyAbsence(presentCalled, absentCalled, output);
        }

        @Override
        public MatchResultRegular withParent(final MatchResultRegular parent) {
            return this.parent.withParent(new MatchResultParentedRegular(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }
    }

    public static class MatchResultAuxiliary implements MatchResult<AuxiliaryIngredient<?>, MatchResultAuxiliary> {
        protected final AuxiliaryIngredient<?> ingredient;
        protected final ItemStack input;
        protected final boolean doesMatch;
        protected final ImmutableList<MatchResultAuxiliary> supplementaryResults;

        public MatchResultAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults) {
            this.ingredient = Objects.requireNonNull(ingredient, "ingredient");
            this.input = input;
            this.doesMatch = doesMatch;
            this.supplementaryResults = ImmutableList.copyOf(supplementaryResults);
        }

        @Override
        public final AuxiliaryIngredient<?> getIngredient() {
            return this.ingredient;
        }

        @Override
        public final ItemStack getInput() {
            return this.input;
        }

        @Override
        public final boolean doesMatch() {
            return this.doesMatch;
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final ItemStack output) {
            if (this.doesMatch && called.add(this.ingredient)) {
                this.ingredient.present(output);
            }
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final ItemStack output) {
            if (!presentCalled.contains(this.ingredient) && !absentCalled.contains(this.ingredient)) {
                this.ingredient.absent(output);
                absentCalled.add(this.ingredient);
            }
            for (final MatchResultAuxiliary result : this.supplementaryResults) {
                result.notifyAbsence(presentCalled, absentCalled, output);
            }
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent);
        }

        public boolean isAtLimit(final int count) {
            return count >= this.ingredient.getLimit();
        }

        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            map.put(this.ingredient, this);
        }
    }

    public static class MatchResultParentedAuxiliary extends MatchResultAuxiliary {
        protected final MatchResultAuxiliary parent;

        public MatchResultParentedAuxiliary(final AuxiliaryIngredient<?> ingredient, final ItemStack input, final boolean doesMatch, final List<MatchResultAuxiliary> supplementaryResults, final MatchResultAuxiliary parent) {
            super(ingredient, input, doesMatch, supplementaryResults);
            this.parent = Objects.requireNonNull(parent, "parent");
        }

        @Override
        public void forMatch(final Set<GenericIngredient<?, ?>> called, final ItemStack output) {
            super.forMatch(called, output);
            this.parent.forMatch(called, output);
        }

        @Override
        public void notifyAbsence(final Set<GenericIngredient<?, ?>> presentCalled, final Set<GenericIngredient<?, ?>> absentCalled, final ItemStack output) {
            super.notifyAbsence(presentCalled, absentCalled, output);
            this.parent.notifyAbsence(presentCalled, absentCalled, output);
        }

        @Override
        public MatchResultAuxiliary withParent(final MatchResultAuxiliary parent) {
            return this.parent.withParent(new MatchResultParentedAuxiliary(this.ingredient, this.input, this.doesMatch, this.supplementaryResults, parent));
        }

        @Override
        public boolean isAtLimit(final int count) {
            return super.isAtLimit(count) || this.parent.isAtLimit(count);
        }

        @Override
        public void propagate(final Multimap<AuxiliaryIngredient<?>, MatchResultAuxiliary> map) {
            super.propagate(map);
            this.parent.propagate(map);
        }
    }

    private static AuxiliaryIngredient<?>[] checkIngredients(final RegularIngredient[] ingredients, final AuxiliaryIngredient<?>[] auxiliaryIngredients) {
        checkForNulls(ingredients);
        checkForNulls(auxiliaryIngredients);
        final boolean ingredientDictator = checkDictatorship(false, ingredients);
        checkDictatorship(ingredientDictator, auxiliaryIngredients);
        return auxiliaryIngredients;
    }

    private static void checkForNulls(final GenericIngredient<?, ?>[] ingredients) {
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] == null) {
                throw new NullPointerException("Must not have null ingredients, found at index " + i);
            }
        }
    }

    private static boolean checkDictatorship(boolean foundDictator, final GenericIngredient<?, ?>[] ingredients) {
        for (final GenericIngredient<?, ?> ingredient : ingredients) {
            if (ingredient.dictatesOutputType()) {
                if (foundDictator) {
                    throw new IllegalRecipeException("Only one ingredient can dictate output type");
                }
                foundDictator = true;
            }
        }
        return foundDictator;
    }

    private static final class Lazy extends GenericRecipe {
        private final Supplier<GenericRecipe> factory;
        private final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer;
        private GenericRecipe delegate;

        private Lazy(
                final Supplier<GenericRecipe> factory,
                final Supplier<? extends RecipeSerializer<GenericRecipe>> serializer) {
            this.factory = factory;
            this.serializer = serializer;
        }

        private GenericRecipe delegate() {
            if (this.delegate == null) {
                this.delegate = this.factory.get();
            }
            return this.delegate;
        }

        @Override
        public boolean isSpecial() {
            return true;
        }

        @Override
        public boolean isIgnoredInRecipeBook() {
            return true;
        }

        @Override
        public RecipeSerializer<? extends CustomRecipe> getSerializer() {
            return this.serializer.get();
        }

        @Override
        public ItemStack getOutput() {
            return this.delegate().getOutput();
        }

        @Override
        public RegularIngredient[] getGenericIngredients() {
            return this.delegate().getGenericIngredients();
        }

        @Override
        public AuxiliaryIngredient<?>[] getAuxiliaryIngredients() {
            return this.delegate().getAuxiliaryIngredients();
        }

        @Override
        public int getWidth() {
            return this.delegate().getWidth();
        }

        @Override
        public int getHeight() {
            return this.delegate().getHeight();
        }

        @Override
        public NonNullList<Ingredient> getDisplayIngredients() {
            return this.delegate().getDisplayIngredients();
        }

        @Override
        public boolean matches(final CraftingInput inventory, final Level level) {
            return this.delegate().matches(inventory, level);
        }

        @Override
        public ItemStack assemble(final CraftingInput inventory) {
            return this.delegate().assemble(inventory);
        }
    }
}
