package de.lucalabs.fairylights.items.crafting;

import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.util.Tags;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class CopyColorRecipe extends CustomRecipe {

    @Override
    public boolean matches(final CraftingInput inventory, final Level level) {
        int count = 0;
        for (int i = 0; i < inventory.size(); i++) {
            final ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty() && (!stack.is(Tags.DYEABLE) || count++ >= 2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(final CraftingInput inventory) {
        ItemStack original = ItemStack.EMPTY;
        for (int i = 0; i < inventory.size(); i++) {
            final ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.is(Tags.DYEABLE)) {
                    if (original.isEmpty()) {
                        original = stack;
                    } else {
                        final ItemStack copy = stack.copy();
                        copy.setCount(1);
                        DyeableItem.setColor(copy, DyeableItem.getColor(original));
                        return copy;
                    }
                } else {
                    break;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(final CraftingInput inventory) {
        ItemStack original = ItemStack.EMPTY;
        final NonNullList<ItemStack> remaining = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            final ItemStack stack = inventory.getItem(i);
            if (stack.getItem().getCraftingRemainder() != null) {
                remaining.set(i, stack.getItem().getCraftingRemainder().create());
            } else if (original.isEmpty() && !stack.isEmpty() && stack.is(Tags.DYEABLE)) {
                final ItemStack rem = stack.copy();
                rem.setCount(1);
                remaining.set(i, rem);
                original = stack;
            }
        }
        return remaining;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return FairyLightCraftingRecipes.COPY_COLOR;
    }
}
