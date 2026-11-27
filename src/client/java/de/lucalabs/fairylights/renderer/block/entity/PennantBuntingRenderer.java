package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.lucalabs.fairylights.connection.PennantBuntingConnection;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.util.Curve;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class PennantBuntingRenderer extends ConnectionRenderer<PennantBuntingConnection> {

    public PennantBuntingRenderer(final Function<ModelLayerLocation, ModelPart> baker) {
        super(baker, FairyLightModelLayers.PENNANT_WIRE, 0.25F);
    }

    @Override
    protected void render(
            final PennantBuntingConnection conn,
            final Curve catenary,
            final float delta,
            final PoseStack matrix,
            final VertexConsumer buf,
            final int packedLight,
            final int packedOverlay) {
        super.render(conn, catenary, delta, matrix, buf, packedLight, packedOverlay);
    }

    public static LayerDefinition wireLayer() {
        return WireModel.createLayer(0, 17, 1);
    }
}
