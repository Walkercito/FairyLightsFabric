package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.feature.light.LightBehavior;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public abstract class LightVariant<T extends LightBehavior> {

    public abstract boolean parallelsCord();

    public abstract float getSpacing();

    public abstract AABB getBounds();

    public abstract double getFloorOffset();

    public abstract T createBehavior(final ItemStack stack);

    public abstract boolean isOrientable();

    public static int lightLevel(final LightVariant<?> variant) {
        if (variant == SimpleLightVariant.FAIRY_LIGHT) {
            return 7;
        }
        if (variant == SimpleLightVariant.JACK_O_LANTERN) {
            return 12;
        }
        if (variant == SimpleLightVariant.CANDLE_LANTERN || variant == SimpleLightVariant.OIL_LANTERN) {
            return 12;
        }
        if (variant == SimpleLightVariant.INCANDESCENT_LIGHT) {
            return 15;
        }
        if (variant == SimpleLightVariant.PAPER_LANTERN
                || variant == SimpleLightVariant.ORB_LANTERN
                || variant == SimpleLightVariant.CANDLE_LANTERN_LIGHT
                || variant == SimpleLightVariant.SKULL_LIGHT
                || variant == SimpleLightVariant.METEOR_LIGHT) {
            return 10;
        }
        return 8;
    }

    public static synchronized Optional<LightVariant<?>> get(final ItemStack provider) {
        if (provider.getItem() instanceof LightItem lightItem) {
            return Optional.of(lightItem.getBlock().getVariant());
        }
        return Optional.empty();
    }
}
