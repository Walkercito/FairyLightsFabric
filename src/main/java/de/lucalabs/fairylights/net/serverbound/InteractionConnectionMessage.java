package de.lucalabs.fairylights.net.serverbound;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.fastener.accessor.FastenerAccessor;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.net.ConnectionMessage;
import de.lucalabs.fairylights.util.Utils;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class InteractionConnectionMessage {

    public static final CustomPacketPayload.Type<Payload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(FairyLights.ID, "interaction_connection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Payload> CODEC =
            StreamCodec.composite(ByteBufCodecs.TRUSTED_COMPOUND_TAG, Payload::data, Payload::new);

    private static final float RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);
    private static final float REACH = 6 * 6;

    private InteractionConnectionMessage() {
    }

    public record Payload(CompoundTag data) implements CustomPacketPayload {
        @Override
        public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public static Payload create(final Connection connection, final PlayerAction action, final Intersection intersection) {
        final Fastener<?> fastener = connection.getFastener();
        final Vec3 hit = intersection.result();
        final CompoundTag data = new CompoundTag();
        data.putLong("pos", fastener.getPos().asLong());
        data.put("fastener", ConnectionMessage.serializeAccessor(fastener.createAccessor()));
        data.putString("uuid", connection.getUUID().toString());
        data.putInt("action", action.ordinal());
        data.putDouble("hitX", hit.x);
        data.putDouble("hitY", hit.y);
        data.putDouble("hitZ", hit.z);
        data.putInt("featureType", intersection.featureType().getId());
        data.putInt("featureId", intersection.feature().getId());
        return new Payload(data);
    }

    public static void apply(final Payload payload, final ServerPlayer player, final MinecraftServer server) {
        final CompoundTag data = payload.data();
        final CompoundTag fastenerTag = data.getCompoundOrEmpty("fastener");
        final FastenerAccessor accessor = ConnectionMessage.deserializeAccessor(fastenerTag);
        final UUID id = UUID.fromString(data.getStringOr("uuid", ""));
        final PlayerAction action = Utils.getEnumValue(PlayerAction.class, data.getIntOr("action", 0));
        final Vec3 hit = new Vec3(
                data.getDoubleOr("hitX", 0.0D),
                data.getDoubleOr("hitY", 0.0D),
                data.getDoubleOr("hitZ", 0.0D));
        final FeatureType featureType = FeatureType.fromId(data.getIntOr("featureType", 0));
        final int featureId = data.getIntOr("featureId", 0);
        server.execute(() -> ConnectionMessage.getConnection(accessor, id, c -> true, player.level()).ifPresent(connection -> {
            if (connection.isModifiable(player)
                    && player.distanceToSqr(Vec3.atLowerCornerOf(connection.getFastener().getPos())) < RANGE
                    && player.distanceToSqr(hit) < REACH) {
                if (action == PlayerAction.ATTACK) {
                    connection.disconnect(player, hit);
                } else {
                    interact(player, connection, featureType, featureId, hit);
                }
            }
        }));
    }

    private static void interact(
            final Player player,
            final Connection connection,
            final FeatureType featureType,
            final int featureId,
            final Vec3 hit) {
        for (final InteractionHand hand : InteractionHand.values()) {
            final ItemStack stack = player.getItemInHand(hand);
            final ItemStack oldStack = stack.copy();
            if (connection.interact(player, hit, featureType, featureId, stack, hand)) {
                updateItem(player, oldStack, stack, hand);
                break;
            }
        }
    }

    private static void updateItem(
            final Player player,
            final ItemStack oldStack,
            final ItemStack stack,
            final InteractionHand hand) {
        if (stack.getCount() <= 0 && !player.getAbilities().instabuild) {
            player.setItemInHand(hand, ItemStack.EMPTY);
        } else if (stack.getCount() < oldStack.getCount() && player.getAbilities().instabuild) {
            stack.setCount(oldStack.getCount());
        }
    }
}
