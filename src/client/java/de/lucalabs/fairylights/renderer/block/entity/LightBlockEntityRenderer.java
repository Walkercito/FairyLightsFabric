package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import de.lucalabs.fairylights.blocks.LightBlock;
import de.lucalabs.fairylights.blocks.entity.LightBlockEntity;
import de.lucalabs.fairylights.feature.light.Light;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class LightBlockEntityRenderer implements BlockEntityRenderer<LightBlockEntity, LightBlockEntityRenderer.State> {

    private final LightRenderer lights;
    private final TextureAtlasSprite sprite;

    public LightBlockEntityRenderer(final BlockEntityRendererProvider.Context context) {
        this.lights = new LightRenderer(context::bakeLayer);
        this.sprite = context.sprites().get(RenderConstants.CONNECTIONS);
    }

    @Override
    public State createRenderState() {
        return new State();
    }

    @Override
    public void extractRenderState(
            final LightBlockEntity blockEntity,
            final State state,
            final float tickProgress,
            final Vec3 cameraPos,
            final @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, tickProgress, cameraPos, crumblingOverlay);
        state.light = blockEntity.getLight();
        state.blockState = blockEntity.getBlockState();
        state.delta = tickProgress;
    }

    @Override
    public void submit(
            final State state,
            final PoseStack pose,
            final SubmitNodeCollector collector,
            final CameraRenderState cameraState) {
        if (state.light == null || state.blockState == null) {
            return;
        }
        final AABB bounds = state.light.getVariant().getBounds();
        final AttachFace face = state.blockState.getValue(LightBlock.FACE);
        final float rotation = state.blockState.getValue(LightBlock.FACING).toYRot();
        pose.pushPose();
        pose.translate(0.5D, 0.5D, 0.5D);
        pose.mulPose(com.mojang.math.Axis.YP.rotationDegrees(180.0F - rotation));
        if (state.light.getVariant().isOrientable()) {
            if (face == AttachFace.WALL) {
                pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
            } else if (face == AttachFace.FLOOR) {
                pose.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-180.0F));
            }
            pose.translate(0.0D, 0.5D, 0.0D);
        } else {
            if (face == AttachFace.CEILING) {
                pose.translate(0.0D, 0.25D, 0.0D);
            } else if (face == AttachFace.WALL) {
                pose.translate(0.0D, 3.0D / 16.0D, 0.125D);
            } else {
                pose.translate(0.0D, -bounds.minY - state.light.getVariant().getFloorOffset() - 0.5D, 0.0D);
            }
        }
        RenderUtils.submit(
                collector,
                pose,
                this.sprite,
                RenderTypes.entityCutout(TextureAtlas.LOCATION_BLOCKS),
                (stack, buffer) -> this.lights.render(stack, state.light, -1, state.delta, state.lightCoords, OverlayTexture.NO_OVERLAY, buffer));
        RenderUtils.submit(
                collector,
                pose,
                this.sprite,
                RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS, false),
                (stack, buffer) -> this.lights.renderTranslucent(stack, state.light, -1, state.delta, state.lightCoords, OverlayTexture.NO_OVERLAY, buffer));
        pose.popPose();
    }

    public static class State extends BlockEntityRenderState {
        @Nullable
        Light<?> light;
        @Nullable
        BlockState blockState;
        float delta;
    }
}
