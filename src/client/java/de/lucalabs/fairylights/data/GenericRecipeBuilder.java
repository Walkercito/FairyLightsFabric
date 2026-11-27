package de.lucalabs.fairylights.data;

import com.google.gson.JsonObject;
import de.lucalabs.fairylights.FairyLights;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class GenericRecipeBuilder {
    private final RecipeSerializer<?> serializer;

    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public GenericRecipeBuilder(final RecipeSerializer<?> serializer) {
        this.serializer = Objects.requireNonNull(serializer, "serializer");
    }

    public static GenericRecipeBuilder customRecipe(final RecipeSerializer<?> serializer) {
        return new GenericRecipeBuilder(serializer);
    }

    public GenericRecipeBuilder unlockedBy(final String name, final CriterionTriggerInstance criterion) {
        this.advancementBuilder.addCriterion(name, criterion);
        return this;
    }

    public void build(final Consumer<FinishedRecipe> consumer, final Identifier id) {
        final Supplier<JsonObject> advancementBuilder;
        final Identifier advancementId;
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            advancementBuilder = () -> null;
            advancementId = Identifier.parse("");
        } else {
            advancementBuilder = this.advancementBuilder.parent(Identifier.parse("recipes/root"))
                    .addCriterion("has_the_recipe", new RecipeUnlockedTrigger.TriggerInstance(ContextAwarePredicate.ANY, id))
                    .rewards(AdvancementRewards.Builder.recipe(id))
                    .requirements(RequirementsStrategy.OR)
                    ::serializeToJson;
            advancementId = Identifier.fromNamespaceAndPath(id.getNamespace(), "recipes/" + FairyLights.ID + "/" + id.getPath());
        }
        consumer.accept(new Result(this.serializer, id, advancementBuilder, advancementId));
    }

    static class Result implements FinishedRecipe {
        final RecipeSerializer<?> serializer;

        final Identifier id;
        final Supplier<JsonObject> advancementJson;
        final Identifier advancementId;

        public Result(
                final RecipeSerializer<?> serializer,
                final Identifier id,
                final Supplier<JsonObject> advancementJson,
                final Identifier advancementId) {

            this.serializer = serializer;
            this.id = id;
            this.advancementJson = advancementJson;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(final JsonObject json) {
        }

        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Override
        public Identifier getId() {
            return this.id;
        }

        @Override
        public JsonObject serializeAdvancement() {
            return this.advancementJson.get();
        }

        @Override
        public Identifier getAdvancementId() {
            return this.advancementId;
        }
    }
}
