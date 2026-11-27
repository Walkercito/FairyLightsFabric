package de.lucalabs.fairylights.items.crafting.ingredient;

import java.util.stream.StreamSupport;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public final class LazyTagIngredient {

    private LazyTagIngredient() {
    }

    public static Ingredient of(final TagKey<Item> tag) {
        return Ingredient.of(StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(tag).spliterator(), false)
                .map(Holder::value));
    }
}
