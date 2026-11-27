package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PennantBuntingConnection;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.feature.Pennant;
import de.lucalabs.fairylights.items.FairyLightItems;
import de.lucalabs.fairylights.items.DyeableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class PennantItemRenderer {

    private final ItemModelResolver resolver;

    public PennantItemRenderer(final ItemModelResolver resolver) {
        this.resolver = resolver;
    }

    public void render(
            final Fastener<?> fastener,
            final float delta,
            final PoseStack pose,
            final SubmitNodeCollector collector,
            final int light,
            final int overlay) {
        for (final Connection connection : fastener.getAllConnections()) {
            if (connection.getFastener() != fastener || !(connection instanceof PennantBuntingConnection bunting)) {
                continue;
            }
            final Pennant[] pennants = bunting.getFeatures();
            if (pennants == null) {
                continue;
            }
            for (final Pennant pennant : pennants) {
                final ItemStack stack = new ItemStack(pennant.getItem() == FairyLightItems.SQUARE_PENNANT
                        ? FairyLightItems.SQUARE_PENNANT_CLOTH
                        : FairyLightItems.TRIANGLE_PENNANT_CLOTH);
                DyeableItem.setColor(stack, pennant.getColor());
                final ItemStackRenderState state = new ItemStackRenderState();
                this.resolver.updateForTopItem(state, stack, ItemDisplayContext.FIXED, fastener.getWorld(), Minecraft.getInstance().player, 0);
                final Vec3 pos = pennant.getPoint(delta);
                pose.pushPose();
                pose.translate(pos.x, pos.y, pos.z);
                pose.mulPose(Axis.YP.rotation(-pennant.getYaw(delta)));
                pose.mulPose(Axis.ZP.rotation(pennant.getPitch(delta)));
                pose.mulPose(Axis.XP.rotation(pennant.getRoll(delta)));
                state.submit(pose, collector, light, overlay, 0);
                pose.popPose();
            }
        }
    }
}
