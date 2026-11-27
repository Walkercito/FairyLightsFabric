package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.GarlandTinselConnection;
import de.lucalabs.fairylights.connection.GarlandVineConnection;
import de.lucalabs.fairylights.connection.HangingLightsConnection;
import de.lucalabs.fairylights.connection.PennantBuntingConnection;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.model.light.BowModel;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.util.Tags;
import java.util.function.Function;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FastenerRenderer {
    private final HangingLightsRenderer hangingLights;
    private final PennantBuntingRenderer pennants;
    private final GarlandVineRenderer garland;
    private final GarlandTinselRenderer tinsel;
    private final BowModel bow;
    private final PennantItemRenderer pennantItems;

    public FastenerRenderer(final Function<ModelLayerLocation, ModelPart> baker, final ItemModelResolver resolver) {
        this.hangingLights = new HangingLightsRenderer(baker);
        this.pennants = new PennantBuntingRenderer(baker);
        this.garland = new GarlandVineRenderer(baker);
        this.tinsel = new GarlandTinselRenderer(baker);
        this.bow = new BowModel(baker.apply(FairyLightModelLayers.BOW));
        this.pennantItems = new PennantItemRenderer(resolver);
    }

    public void renderPennants(
            final Fastener<?> fastener,
            final float delta,
            final PoseStack pose,
            final SubmitNodeCollector collector,
            final int packedLight,
            final int packedOverlay) {
        this.pennantItems.render(fastener, delta, pose, collector, packedLight, packedOverlay);
    }

    public void render(
            final Fastener<?> fastener,
            final float delta,
            final PoseStack matrix,
            final VertexConsumer buf,
            final int packedLight,
            final int packedOverlay) {

        boolean renderBow = true;
        for (final Connection conn : fastener.getAllConnections()) {
            if (conn.getDestination().getType() == FastenerType.PLAYER) {
                continue;
            }
            if (conn.getFastener() == fastener) {
                this.renderConnection(delta, matrix, buf, packedLight, packedOverlay, conn);
            }
            if (renderBow && conn instanceof GarlandVineConnection && this.renderBow(fastener, matrix, buf, packedLight, packedOverlay)) {
                renderBow = false;
            }
        }
    }

    private boolean renderBow(
            final Fastener<?> fastener,
            final PoseStack matrix,
            final VertexConsumer buf,
            final int packedLight,
            final int packedOverlay) {

        if (fastener instanceof FenceFastener) {
            final Level world = fastener.getWorld();
            if (world == null) {
                return false;
            }
            final BlockState state = world.getBlockState(fastener.getPos());
            if (!state.is(Tags.FENCES)) {
                return false;
            }
            final float offset = -1.5F / 16.0F;
            final boolean north = state.getValue(FenceBlock.NORTH);
            final boolean east = state.getValue(FenceBlock.EAST);
            final boolean south = state.getValue(FenceBlock.SOUTH);
            final boolean west = state.getValue(FenceBlock.WEST);
            boolean tryDirX = true;
            boolean bow = false;
            if (!north && (east || west)) {
                this.bow(matrix, Direction.NORTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (!south && (east || west)) {
                this.bow(matrix, Direction.SOUTH, offset, buf, packedLight, packedOverlay);
                tryDirX = false;
                bow = true;
            }
            if (tryDirX) {
                if (!east && (north || south)) {
                    this.bow(matrix, Direction.EAST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
                if (!west && (north || south)) {
                    this.bow(matrix, Direction.WEST, offset, buf, packedLight, packedOverlay);
                    bow = true;
                }
            }
            return bow;
        } else if (fastener.getFacing().getAxis() != Direction.Axis.Y) {
            this.bow(matrix, fastener.getFacing(), 0.0F, buf, packedLight, packedOverlay);
            return true;
        }
        return false;
    }

    private void bow(final PoseStack matrix, final Direction dir, final float offset, final VertexConsumer buf, final int packedLight, final int packedOverlay) {
        matrix.pushPose();
        matrix.mulPose(Axis.YP.rotationDegrees(180.0F - dir.toYRot()));
        if (offset != 0.0F) {
            matrix.translate(0.0D, 0.0D, offset);
        }
        this.bow.renderToBuffer(matrix, buf, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.popPose();
    }

    private void renderConnection(final float delta, final PoseStack matrix, final VertexConsumer buf, final int packedLight, final int packedOverlay, final Connection conn) {
        if (conn instanceof HangingLightsConnection hangingLightsConnection) {
            this.hangingLights.render(hangingLightsConnection, delta, matrix, buf, packedLight, packedOverlay);
        } else if (conn instanceof PennantBuntingConnection pennantBuntingConnection) {
            this.pennants.render(pennantBuntingConnection, delta, matrix, buf, packedLight, packedOverlay);
        } else if (conn instanceof GarlandTinselConnection tinselConnection) {
            this.tinsel.render(tinselConnection, delta, matrix, buf, packedLight, packedOverlay);
        } else if (conn instanceof GarlandVineConnection vineConnection) {
            this.garland.render(vineConnection, delta, matrix, buf, packedLight, packedOverlay);
        }
    }
}
