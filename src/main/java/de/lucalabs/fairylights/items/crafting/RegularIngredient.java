package de.lucalabs.fairylights.items.crafting;

import net.minecraft.world.item.ItemStack;

public interface RegularIngredient extends GenericIngredient<RegularIngredient, GenericRecipe.MatchResultRegular> {
    default void matched(final ItemStack ingredient, final ItemStack output) {
    }
}
