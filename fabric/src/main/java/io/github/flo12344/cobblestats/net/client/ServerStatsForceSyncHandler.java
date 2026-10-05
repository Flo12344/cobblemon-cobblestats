package io.github.flo12344.cobblestats.net.client;

import io.github.flo12344.cobblestats.common.client.BattleStateTracker;
import io.github.flo12344.cobblestats.common.net.payload.ServerStatsSyncForcePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.ArrayDeque;

public class ServerStatsForceSyncHandler {
    public static void handleReconciliation(final ServerStatsSyncForcePayload payload, final ClientPlayNetworking.Context ctx) {
        var pkm = BattleStateTracker.getPokemon(payload.target());
        var stat = switch (payload.stat().toLowerCase()) {
            case "atk" -> "attack";
            case "def" -> "defense";
            case "spa" -> "special_attack";
            case "spd" -> "special_defense";
            case "spe" -> "speed";
            default -> payload.stat();
        };
        var s = pkm.pendingStatDeltas.getOrDefault(stat, new ArrayDeque<>());
        s.add(payload.value());
        pkm.pendingStatDeltas.put(stat, s);
    }
}
