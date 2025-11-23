package io.github.flo12344.cobblestats.common.net;

import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;

import java.util.*;

public class ServerData {
    public static List<UUID> playerWithMod = new ArrayList<>();
    public static Map<UUID, List<BattlePokemon>> startTeams = new HashMap<>();

    public static void clear() {
        playerWithMod.clear();
        startTeams.clear();
    }

}
