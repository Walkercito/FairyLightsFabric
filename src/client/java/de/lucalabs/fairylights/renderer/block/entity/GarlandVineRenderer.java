package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.lucalabs.fairylights.connection.GarlandVineConnection;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.util.Curve;
import de.lucalabs.fairylights.util.MathHelper;
import de.lucalabs.fairylights.util.RandomArray;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import java.util.function.Function;

public class GarlandVineRenderer extends ConnectionRenderer<GarlandVineConnection> {
    private static final int RING_COUNT = 7;
    private static final RandomArray RAND = new RandomArray(8411, RING_COUNT * 4);

    private final RingsModel rings;

    protected GarlandVineRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FairyLightModelLayers.VINE_WIRE);
        this.rings = new RingsModel(baker.apply(FairyLightModelLayers.GARLAND_RINGS));
    }

    @Override
    protected void render(
            final GarlandVineConnection conn,
            final Curve catenary,
            final float delta,
            final PoseStack matrix,
            final MultiBufferSource source,
            final int packedLight,
            final int packedOverlay) {
        super.render(conn, catenary, delta, matrix, source, packedLight, packedOverlay);
        final int hash = conn.getUUID().hashCode();
        final VertexConsumer buf = RenderConstants.SOLID_TEXTURE.buffer(source, RenderType::entityCutout);
        catenary.visitPoints(0.25F, false, (index, x, y, z, yaw, pitch) -> {
            matrix.push();
            matrix.translate(x, y, z);
            matrix.multiply(RotationAxis.POSITIVE_Y.rotation(-yaw));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotation(pitch));
            matrix.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(RAND.get(index + hash) * 45.0F));
            matrix.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(RAND.get(index + 8 + hash) * 60.F + 90.0F));
            this.rings.setWhich(index % RING_COUNT);
            this.rings.render(matrix, buf, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
            matrix.pop();
        });
    }

    public static LayerDefinition wireLayer() {
        return WireModel.createLayer(39, 0, 1);
    }

    public static class RingsModel extends Model {
        final ModelPart[] roots;
        int which;

        RingsModel(final ModelPart root) {
            super(RenderType::entityCutout);
            ModelPart[] roots = new ModelPart[RING_COUNT];
            for (int i = 0; i < RING_COUNT; i++) {
                roots[i] = root.getChild(Integer.toString(i));
            }
            this.roots = roots;
        }

        public static LayerDefinition createLayer() {
            final float size = 4.0F;
            CubeListBuilder root = CubeListBuilder.create()
                    .texOffs(14, 91)
                    .addBox(-size / 2.0F, -size / 2.0F, -size / 2.0F, size, size, size);
            PartPose crossPose = PartPose.rotation(0.0F, 0.0F, MathHelper.HALF_PI);
            MeshDefinition mesh = new MeshDefinition();
            for (int i = 0; i < RING_COUNT; i++) {
                mesh.getRoot().addOrReplaceChild(Integer.toString(i), root, PartPose.ZERO)
                        .addOrReplaceChild("cross_" + i, CubeListBuilder.create()
                                .texOffs(i * 8, 64)
                                .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F)
                                .addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F), crossPose);
            }
            return LayerDefinition.create(mesh, 128, 128);
        }

        public void setWhich(int which) {
            this.which = which;
        }

        @Override
        public void renderToBuffer(final PoseStack matrix, final VertexConsumer builder, final int light, final int overlay, final float r, final float g, final float b, final float a) {
            this.roots[this.which].render(matrix, builder, light, overlay, r, g, b, a);
        }
    }
}
