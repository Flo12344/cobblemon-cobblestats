package io.github.flo12344.cobblestats.net.server;

import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.net.ServerData;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

import java.util.UUID;


public class ServerJoinHandler {
    public static void onPlayerLogin(final JoinServerPayload data, final ServerPlayNetworking.Context context) {
        ServerData.playerWithMod.add(UUID.fromString(data.uuid()));
        System.out.println(data.uuid() + " joined with cobblestats");
    }
}
