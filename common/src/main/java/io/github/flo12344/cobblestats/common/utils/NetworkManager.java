package io.github.flo12344.cobblestats.common.utils;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface NetworkManager {
    void sendToServer(CustomPacketPayload payload);

    void sendToClient(ServerPlayer player, CustomPacketPayload payload);

    MinecraftServer getCurrentServer();
}
