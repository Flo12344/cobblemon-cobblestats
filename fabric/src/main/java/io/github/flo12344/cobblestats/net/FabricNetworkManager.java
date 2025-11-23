package io.github.flo12344.cobblestats.net;

import io.github.flo12344.cobblestats.common.utils.NetworkManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkManager implements NetworkManager {
    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

    @Override
    public void sendToClient(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    public static MinecraftServer CURRENT_SERVER = null;

    @Override
    public MinecraftServer getCurrentServer() {
        return CURRENT_SERVER;
    }
}
