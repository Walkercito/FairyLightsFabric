package de.lucalabs.fairylights.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class RenderUtils {

    private RenderUtils() {
    }

    public static int color(final float r, final float g, final float b, final float a) {
        return (int) (a * 255.0F) << 24
                | (int) (r * 255.0F) << 16
                | (int) (g * 255.0F) << 8
                | (int) (b * 255.0F);
    }

    public interface RenderTask {
        void render(PoseStack pose, VertexConsumer buffer);
    }

    public static void submit(
            final SubmitNodeCollector collector,
            final PoseStack pose,
            final RenderType type,
            final RenderTask task) {
        collector.submitCustomGeometry(pose, type, (entry, buffer) -> {
            final PoseStack stack = new PoseStack();
            stack.last().pose().set(entry.pose());
            stack.last().normal().set(entry.normal());
            task.render(stack, buffer);
        });
    }

    public static void submit(
            final SubmitNodeCollector collector,
            final PoseStack pose,
            final TextureAtlasSprite sprite,
            final RenderType type,
            final RenderTask task) {
        collector.submitCustomGeometry(pose, type, (entry, buffer) -> {
            final PoseStack stack = new PoseStack();
            stack.last().pose().set(entry.pose());
            stack.last().normal().set(entry.normal());
            task.render(stack, new SpriteVertexConsumer(buffer, sprite));
        });
    }

    public static void renderPart(
            final ModelPart part,
            final PoseStack pose,
            final VertexConsumer buffer,
            final int light,
            final int overlay,
            final float r,
            final float g,
            final float b,
            final float a) {
        part.render(pose, buffer, light, overlay, color(r, g, b, a));
    }
}
