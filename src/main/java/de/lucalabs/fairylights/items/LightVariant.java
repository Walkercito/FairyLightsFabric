package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import org.ladysnake.cca.api.v3.component.ComponentProvider;
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

    public static synchronized Optional<LightVariant<?>> get(final ItemStack provider) {
        return FairyLightComponents.LIGHT_VARIANT.get(provider).get();
    }
}
