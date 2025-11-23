package io.github.flo12344.cobblestats.net.server;

import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.net.ServerData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;


public class ServerJoinHandler {
    public static void onPlayerLogin(final JoinServerPayload data, final IPayloadContext context) {
        context.enqueueWork(() ->
        {
            ServerData.playerWithMod.add(UUID.fromString(data.uuid()));
        });
    }
}
