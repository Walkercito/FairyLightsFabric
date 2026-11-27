package de.lucalabs.fairylights.net;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

public class UpdateEntityFastenerMessageHandler {

    public static void apply(
            Minecraft server,
            ClientPacketListener handler,
            FriendlyByteBuf buf,
            PacketSender responseSender) {
        // TODO probably not needed because of auto sync, see other TODOs
    }
}
