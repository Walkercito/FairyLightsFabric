package de.lucalabs.fairylights.net;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;

public final class FilteredServerPlayNetworking {
    private FilteredServerPlayNetworking() {
    }

    public static void sendToPlayersTrackingChunk(ServerLevel world, ChunkPos chunkPos, Identifier packetId, FriendlyByteBuf packet) {
        for (ServerPlayer player : PlayerLookup.tracking(world, chunkPos)) {
            ServerPlayNetworking.send(player, packetId, packet);
        }
    }

    public static void sendToPlayersWatchingEntity(Entity entity, Identifier packetId, FriendlyByteBuf packet) {
        for (ServerPlayer player : PlayerLookup.tracking(entity)) {
            ServerPlayNetworking.send(player, packetId, packet);
        }
    }
}
