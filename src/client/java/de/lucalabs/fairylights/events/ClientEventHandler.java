package de.lucalabs.fairylights.events;

import com.google.common.collect.Sets;
import de.lucalabs.fairylights.collision.Collidable;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.components.FairyLightComponents;
import de.lucalabs.fairylights.components.GenericComponent;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.FastenerType;
import de.lucalabs.fairylights.net.serverbound.InteractionConnectionMessage;
import de.lucalabs.fairylights.renderer.RenderConstants;
import de.lucalabs.fairylights.renderer.RenderUtils;
import java.util.ConcurrentModificationException;
import java.util.Set;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentAccess;

public final class ClientEventHandler {
    private ClientEventHandler() {
    }

    public static void register() {
        LevelRenderEvents.COLLECT_SUBMITS.register(context -> {
            final AABB box = getHitBox();
            if (box == null) {
                return;
            }
            final Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().position();
            RenderUtils.submit(context.submitNodeCollector(), context.poseStack(), RenderTypes.LINES, (pose, buffer) ->
                    ShapeRenderer.renderShape(pose, buffer, Shapes.create(box.inflate(0.002D)), -camera.x, -camera.y, -camera.z, 0x000000, RenderConstants.HIGHLIGHT_ALPHA));
        });
    }

    @Nullable
    public static AABB getHitBox() {
        if (Minecraft.getInstance().hitResult instanceof EntityHitResult result && result.getEntity() instanceof HitConnection hit) {
            return hit.result.intersection.hitBox();
        }
        return null;
    }

    @Nullable
    public static Connection getHitConnection() {
        final net.minecraft.world.phys.HitResult result = Minecraft.getInstance().hitResult;
        if (result instanceof EntityHitResult) {
            final Entity entity = ((EntityHitResult) result).getEntity();
            if (entity instanceof HitConnection hitConnection) {
                return hitConnection.result.connection;
            }
        }
        return null;
    }

    @Nullable
    private static HitResult getHitConnection(final Level world, final Entity viewer) {
        final AABB bounds = new AABB(viewer.blockPosition()).inflate(Connection.MAX_LENGTH + 1.0D);
        final Set<Fastener<?>> fasteners = collectFasteners(world, bounds);
        return getHitConnection(viewer, bounds, fasteners);
    }

    @Nullable
    private static HitResult getHitConnection(final Entity viewer, final AABB bounds, final Set<Fastener<?>> fasteners) {
        if (fasteners.isEmpty()) {
            return null;
        }
        final Vec3 origin = viewer.getEyePosition(1.0F);
        final Vec3 look = viewer.getLookAngle();
        final Player player = Minecraft.getInstance().player;
        final double reach = player != null ? player.entityInteractionRange() : 6.0D;
        final Vec3 end = origin.add(look.x * reach, look.y * reach, look.z * reach);
        Connection found = null;
        Intersection rayTrace = null;
        double distance = Double.MAX_VALUE;
        for (final Fastener<?> fastener : fasteners) {
            for (final Connection connection : fastener.getOwnConnections()) {
                if (connection.getDestination().getType() == FastenerType.PLAYER) {
                    continue;
                }
                final Collidable collision = connection.getCollision();
                final Intersection result = collision.intersect(origin, end);
                if (result != null) {
                    final double dist = result.result().distanceTo(origin);
                    if (dist < distance) {
                        distance = dist;
                        found = connection;
                        rayTrace = result;
                    }
                }
            }
        }
        if (found == null) {
            return null;
        }
        return new HitResult(found, rayTrace);
    }

    public static void updateHitConnection() {
        final Minecraft mc = Minecraft.getInstance();
        final Entity viewer = mc.getCameraEntity();
        if (mc.hitResult != null && mc.level != null && viewer != null) {
            final HitResult result = getHitConnection(mc.level, viewer);
            if (result != null) {
                final Vec3 eyes = viewer.getEyePosition(1.0F);
                if (result.intersection.result().distanceTo(eyes) < mc.hitResult.getLocation().distanceTo(eyes)) {
                    mc.hitResult = new EntityHitResult(new HitConnection(mc.level, result));
                    mc.crosshairPickEntity = null;
                }
            }
        }
    }

    private static Set<Fastener<?>> collectFasteners(final Level world, final AABB bounds) {
        final Set<Fastener<?>> fasteners = Sets.newLinkedHashSet();
        world.getEntitiesOfClass(FenceFastenerEntity.class, bounds)
                .forEach(entity -> collectFastenersAttachedToEntity(entity, bounds, fasteners));
        final int minX = MthFloor(bounds.minX / 16.0D);
        final int maxX = MthCeil(bounds.maxX / 16.0D);
        final int minZ = MthFloor(bounds.minZ / 16.0D);
        final int maxZ = MthCeil(bounds.maxZ / 16.0D);
        final ChunkSource provider = world.getChunkSource();
        for (int x = minX; x < maxX; x++) {
            for (int z = minZ; z < maxZ; z++) {
                final LevelChunk chunk = provider.getChunk(x, z, false);
                if (chunk != null) {
                    try {
                        for (final BlockEntity entity : chunk.getBlockEntities().values()) {
                            collectFastenersAttachedToEntity(entity, bounds, fasteners);
                        }
                    } catch (final ConcurrentModificationException ignored) {
                    }
                }
            }
        }
        return fasteners;
    }

    private static int MthFloor(final double value) {
        return (int) Math.floor(value);
    }

    private static int MthCeil(final double value) {
        return (int) Math.ceil(value);
    }

    private static void collectFastenersAttachedToEntity(final ComponentAccess entity, final AABB bounds, final Set<Fastener<?>> fasteners) {
        FairyLightComponents.FASTENER.maybeGet(entity).flatMap(GenericComponent::get).ifPresent(fastener -> {
            if (bounds.contains(fastener.getConnectionPoint())) {
                fasteners.add(fastener);
            }
        });
    }

    static class HitConnection extends Entity {
        final HitResult result;

        HitConnection(final Level world, final HitResult result) {
            super(EntityType.ITEM, world);
            this.setId(-1);
            this.result = result;
            this.setPos(result.intersection.result());
        }

        @Override
        protected void defineSynchedData(final SynchedEntityData.Builder builder) {
        }

        @Override
        public boolean hurtServer(final ServerLevel level, final DamageSource source, final float amount) {
            return false;
        }

        @Override
        protected void readAdditionalSaveData(final ValueInput input) {
        }

        @Override
        protected void addAdditionalSaveData(final ValueOutput output) {
        }

        @Override
        public boolean hurtClient(final DamageSource source) {
            if (source.getEntity() == Minecraft.getInstance().player) {
                this.processAction(PlayerAction.ATTACK);
                return true;
            }
            return false;
        }

        @Override
        public InteractionResult interact(final Player player, final InteractionHand hand, final Vec3 hit) {
            if (player == Minecraft.getInstance().player) {
                this.processAction(PlayerAction.INTERACT);
                return InteractionResult.SUCCESS;
            }
            return super.interact(player, hand, hit);
        }

        private void processAction(final PlayerAction action) {
            ClientPlayNetworking.send(
                    InteractionConnectionMessage.create(this.result.connection, action, this.result.intersection));
        }
    }

    private record HitResult(Connection connection, Intersection intersection) {
    }
}
