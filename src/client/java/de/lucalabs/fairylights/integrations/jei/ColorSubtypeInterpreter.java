package de.lucalabs.fairylights.integrations.jei;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class ColorSubtypeInterpreter implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public @NotNull String apply(final ItemStack stack, final UidContext context) {
        final CompoundTag compound = stack.getTag();
        if (compound != null && compound.contains("color", Tag.TAG_INT)) {
            return String.format("%06x", compound.getInt("color"));
        }
        return NONE;
    }
}
