package de.lucalabs.fairylights.integrations.jei;

import de.lucalabs.fairylights.FairyLights;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public final class FairyLightJeiPlugin implements IModPlugin {

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.fromNamespaceAndPath(FairyLights.ID, "plugin");
    }
}
