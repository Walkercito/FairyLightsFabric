package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

public final class TinselConnectionItem extends ConnectionItem
{

    public TinselConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.TINSEL_GARLAND);
    }

    @Override
    public Component getName(final ItemStack stack) {
        return DyeableItem.getDisplayName(stack, super.getName(stack));
    }

    @Override
    public CompoundTag serializeLogic(final ItemStack stack) {
        final CompoundTag tag = new CompoundTag();
        tag.putInt("color", DyeableItem.getColor(stack));
        return tag;
    }

    @Override
    public void deserializeLogic(final ItemStack stack, final CompoundTag tag) {
        DyeableItem.setColor(stack, tag.getIntOr("color", DyeableItem.getColor(DyeColor.LIGHT_GRAY)));
    }
}
