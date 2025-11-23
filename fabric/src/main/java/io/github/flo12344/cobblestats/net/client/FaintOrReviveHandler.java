package io.github.flo12344.cobblestats.net.client;

import io.github.flo12344.cobblestats.common.client.net.ClientData;
import io.github.flo12344.cobblestats.common.net.payload.FaintOrRevivePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

public class FaintOrReviveHandler {
    public static void handle(final FaintOrRevivePayload payload, final ClientPlayNetworking.Context ctx) {
        ClientData.pokemonCounts.get(UUID.fromString(payload.uuid())).faintOrRevive(payload.pos(), payload.is_ko());
    }
}
