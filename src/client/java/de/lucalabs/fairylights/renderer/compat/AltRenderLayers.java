package de.lucalabs.fairylights.renderer.compat;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;

public final class AltRenderLayers {
    private AltRenderLayers() {}

    public static RenderType getUnsortedTranslucent(Identifier textureLocation)
    {
        return Internal.UNSORTED_TRANSLUCENT.apply(textureLocation);
    }

    private static class Internal extends RenderType {

        private Internal(String name, VertexFormat fmt, VertexFormat.Mode glMode, int size, boolean doCrumbling, boolean depthSorting, Runnable onEnable, Runnable onDisable)
        {
            super(name, fmt, glMode, size, doCrumbling, depthSorting, onEnable, onDisable);
            throw new IllegalStateException("This class must not be instantiated");
        }

        public static Function<Identifier, RenderType> UNSORTED_TRANSLUCENT = Util.memoize(Internal::unsortedTranslucent);
        private static RenderType unsortedTranslucent(Identifier textureLocation)
        {
            final boolean sortingEnabled = false;
            CompositeState renderState = CompositeState.builder()
                    .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                    .setTextureState(new TextureStateShard(textureLocation, false, false))
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setLightmapState(LIGHTMAP)
                    .setOverlayState(OVERLAY)
                    .createCompositeState(true);
            return create(
                    "forge_entity_unsorted_translucent",
                    DefaultVertexFormat.NEW_ENTITY,
                    VertexFormat.Mode.QUADS,
                    256,
                    true,
                    sortingEnabled,
                    renderState);
        }
    }
}
