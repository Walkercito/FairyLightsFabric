package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.items.crafting.GenericRecipe;
import java.util.List;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public final class FairyLightJeiPlugin implements IModPlugin {

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(FairyLights.ID, "plugin");
    }

    @Override
    public void registerVanillaCategoryExtensions(final IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(GenericRecipe.class, new ICraftingCategoryExtension<GenericRecipe>() {
            @Override
            public List<SlotDisplay> getIngredients(final RecipeHolder<GenericRecipe> holder) {
                return holder.value().getDisplayIngredients().stream().map(Ingredient::display).toList();
            }
        });
    }

    @Override
    public void registerItemSubtypes(final ISubtypeRegistration registration) {
        FairyLightItems.lights().forEach(item -> registration.registerFromDataComponentTypes(
                item,
                DataComponents.DYED_COLOR,
                FairyLightItemComponents.COLORS,
                FairyLightItemComponents.TWINKLE));
        for (final Item item : List.of(
                FairyLightItems.HANGING_LIGHTS,
                FairyLightItems.PENNANT_BUNTING,
                FairyLightItems.TINSEL,
                FairyLightItems.TRIANGLE_PENNANT,
                FairyLightItems.SQUARE_PENNANT)) {
            registration.registerFromDataComponentTypes(
                    item,
                    DataComponents.DYED_COLOR,
                    FairyLightItemComponents.COLORS,
                    FairyLightItemComponents.TWINKLE,
                    FairyLightItemComponents.STRING,
                    FairyLightItemComponents.PATTERN);
        }
    }
}
