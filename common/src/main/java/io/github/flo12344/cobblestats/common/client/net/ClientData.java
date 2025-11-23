package io.github.flo12344.cobblestats.common.client.net;

import io.github.flo12344.cobblestats.common.utils.PlayerTeamInfo;

import java.util.HashMap;
import java.util.UUID;

public class ClientData {
    public static boolean SERVER_COMPAT = false;

    public static HashMap<UUID, PlayerTeamInfo> pokemonCounts = new HashMap<>();

    public static void clearData() {
        pokemonCounts.clear();
    }
}
