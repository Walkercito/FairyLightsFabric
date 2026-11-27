package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.renderer.RenderUtils;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class FastenerBlockEntityRenderer implements BlockEntityRenderer<FastenerBlockEntity, FastenerBlockEntityRenderer.State> {

    private final FastenerRenderer renderer;
    private final TextureAtlasSprite sprite;

    public FastenerBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.renderer = new FastenerRenderer(context::bakeLayer);
        this.sprite = context.sprites().get(RenderConstants.CONNECTIONS);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(
            final FastenerBlockEntity blockEntity,
            final State state,
            final float tickProgress,
            final Vec3 cameraPos,
            final @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.fastener = FairyLightComponents.FASTENER.get(blockEntity).get().orElse(null);
        state.offset = blockEntity.getOffset();
        state.delta = tickProgress;
    }

    @Override
    public void submit(
            final State state,
            final PoseStack pose,
            final SubmitNodeCollector collector,
            final CameraRenderState cameraState) {
        if (state.fastener == null) {
            return;
        }
        pose.pushPose();
        pose.translate(state.offset.x, state.offset.y, state.offset.z);
        RenderUtils.submit(
                collector,
                pose,
                this.sprite,
                RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                (stack, buffer) -> this.renderer.render(state.fastener, state.delta, stack, buffer, state.lightCoords, OverlayTexture.NO_OVERLAY));
        pose.popPose();
    }

    public static class State extends BlockEntityRenderState {
        @Nullable
        Fastener<?> fastener;
        Vec3 offset = Vec3.ZERO;
        float delta;
    }
}
