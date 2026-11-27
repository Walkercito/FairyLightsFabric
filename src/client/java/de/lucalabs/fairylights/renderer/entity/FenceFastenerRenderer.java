package de.lucalabs.fairylights.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.renderer.RenderUtils;
import de.lucalabs.fairylights.renderer.block.entity.FastenerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.Nullable;

public final class FenceFastenerRenderer extends EntityRenderer<FenceFastenerEntity, FenceFastenerRenderer.State> {

    private final FastenerRenderer renderer;
    private final TextureAtlasSprite sprite;

    public FenceFastenerRenderer(final EntityRendererProvider.Context context) {
        super(context);
        this.renderer = new FastenerRenderer(context::bakeLayer, context.getItemModelResolver());
        this.sprite = context.getSprites().get(RenderConstants.CONNECTIONS);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(final FenceFastenerEntity entity, final State state, final float tickProgress) {
        super.extractRenderState(entity, state, tickProgress);
        state.fastener = FairyLightComponents.FASTENER.get(entity).get().orElse(null);
        state.delta = tickProgress;
    }

    @Override
    public void submit(final State state, final PoseStack pose, final SubmitNodeCollector collector, final CameraRenderState cameraState) {
        if (state.fastener != null) {
            RenderUtils.submit(
                    collector,
                    pose,
                    this.sprite,
                    RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                    (stack, buffer) -> this.renderer.render(state.fastener, state.delta, stack, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY));
            this.renderer.renderPennants(state.fastener, state.delta, pose, collector, state.lightCoords, OverlayTexture.NO_OVERLAY);
        }
        super.submit(state, pose, collector, cameraState);
    }

    public static class State extends EntityRenderState {
        @Nullable
        de.lucalabs.fairylights.fastener.Fastener<?> fastener;
        float delta;
    }
}
