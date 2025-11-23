package io.github.flo12344.cobblestats.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleStateTracker {
    private static final Map<String, PokemonBattleState> battleStateMap = new HashMap<>();

    public BattleStateTracker() {
    }

    public static void changeStats(String name, String stat, String severity, boolean isBoost) {
        if (!battleStateMap.containsKey(name))
            return;
        battleStateMap.get(name).boostState(getSmallName(stat), severity, isBoost);
    }

    public static void addPokemon(String name) {
        battleStateMap.put(name, new PokemonBattleState());
    }

    public static List<String> getChangedStats(String name) {
        if (battleStateMap.containsKey(name))
            return battleStateMap.get(name).getStats();
        return new ArrayList<>();
    }

    public static PokemonBattleState getStatsForHold(String name) {
        var hold = battleStateMap.get(name);
        hold.overrideType("");
        return hold;
    }

    public static PokemonBattleState getPokemon(String name) {
        return battleStateMap.getOrDefault(name, new PokemonBattleState());
    }

    public static void applyStats(String name, PokemonBattleState stat) {
        battleStateMap.put(name, stat);
    }

    public static void removePokemon(String name) {
        battleStateMap.remove(name);
    }

    public static void printAll() {
        battleStateMap.forEach((s, pokemonBattleState) ->
                {
                    pokemonBattleState.printAll();
                }
        );
    }

    private static String getSmallName(String stat) {
        return switch (stat) {
            case "attack" -> "Atk";
            case "defence" -> "Def";
            case "speed" -> "Spe";
            case "special_attack" -> "SpA";
            case "special_defence" -> "SpD";
            default -> stat;
        };
    }

    public static void endOfBattle() {
        BattleProcess.sideCount = 0;
        BattleProcess.side2 = false;
        BattleProcess.side1 = false;
        battleStateMap.clear();
    }
}
