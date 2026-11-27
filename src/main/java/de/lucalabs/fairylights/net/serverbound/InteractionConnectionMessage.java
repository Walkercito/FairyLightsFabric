package de.lucalabs.fairylights.net.serverbound;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.collision.Intersection;
import de.lucalabs.fairylights.connection.Connection;
import de.lucalabs.fairylights.connection.PlayerAction;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.net.ConnectionMessage;
import de.lucalabs.fairylights.util.Utils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class InteractionConnectionMessage extends ConnectionMessage {

    public static final Identifier ID = Identifier.fromNamespaceAndPath(FairyLights.ID, "interaction_connection");

    private static final float RANGE = (Connection.MAX_LENGTH + 1) * (Connection.MAX_LENGTH + 1);
    private static final float REACH = 6 * 6;

    public InteractionConnectionMessage(
            final Connection connection,
            final PlayerAction type,
            final Intersection intersection) {
        super(connection);

        Vec3 hit = intersection.result();

        writeByte(type.ordinal());
        writeDouble(hit.x);
        writeDouble(hit.y);
        writeDouble(hit.z);
        writeVarInt(intersection.featureType().getId());
        writeVarInt(intersection.feature().getId());
    }

    public static void apply(
            MinecraftServer server,
            ServerPlayer player,
            ServerGamePacketListenerImpl handler,
            FriendlyByteBuf buf,
            PacketSender responseSender) {
        ParsedData baseData = parse(buf);

        short type = buf.readUnsignedByte();
        PlayerAction action = Utils.getEnumValue(PlayerAction.class, type);

        double hitX = buf.readDouble();
        double hitY = buf.readDouble();
        double hitZ = buf.readDouble();
        Vec3 hit = new Vec3(hitX, hitY, hitZ);

        FeatureType featureType = FeatureType.fromId(buf.readVarInt());
        int featureId = buf.readVarInt();

        server.execute(() -> {
            getConnection(baseData.accessor(), baseData.id(), c -> true, player.level()).ifPresent(connection -> {
                if (connection.isModifiable(player) &&
                        player.distanceToSqr(Vec3.atLowerCornerOf(connection.getFastener().getPos())) < RANGE &&
                        player.distanceToSqr(hitX, hitY, hitZ) < REACH
                ) {
                    if (action == PlayerAction.ATTACK) {
                        connection.disconnect(player, hit);
                    } else {
                        interact(player, connection, featureType, featureId, hit);
                    }
                }
            });
        });
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
//            ForgeEventFactory.onPlayerDestroyItem(player, stack, hand); // TODO check if relevant
            player.setItemInHand(hand, ItemStack.EMPTY);
        } else if (stack.getCount() < oldStack.getCount() && player.getAbilities().instabuild) {
            stack.setCount(oldStack.getCount());
        }
    }
}
