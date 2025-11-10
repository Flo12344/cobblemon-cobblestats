package io.github.flo12344.cobblestats.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.HashMap;
import java.util.Map;

public class TerrainBattleState {
    private static String weather = "";
    private static String terrain = "";
    private static Integer terrainTurns = 0;
    private static Integer weatherTurns = 0;
    private final static Map<String,Integer> rooms = new HashMap<>();
    private final static Map<String,Integer> hazardStateSide1 = new HashMap<>();
    private final static Map<String,Integer> hazardStateSide2 = new HashMap<>();

    public static void setTerrain(String state){
        terrain = state;
        terrainTurns = 5;
    }
    public static Object[] getTerrainState(){
        TranslatableContents translatableContents = new TranslatableContents("cobblemon.move." + terrain, null,new Object[]{});
        Component component = MutableComponent.create(translatableContents);
        return new Object[]{terrain.isEmpty()? "" :component.getString(), terrainTurns};
    }
    public static void updateTerrain()
    {
        terrainTurns--;
    }


    public static void addHazard(boolean self, String hazard)
    {
        if(self)
        {
            hazardStateSide1.put(hazard, hazardStateSide1.getOrDefault(hazard, 0) + 1);
        }
        else
        {
            hazardStateSide2.put(hazard, hazardStateSide2.getOrDefault(hazard, 0) + 1);
        }
    }
    public static void removeHazard(boolean self, String hazard)
    {
        if (self)
        {
            hazardStateSide1.remove(hazard);
        }
        else {
            hazardStateSide2.remove(hazard);
        }
    }
    public static Map<String,Integer> getHazardStates(boolean self){
        Map<String,Integer> result = new HashMap<>();
        var base = self ? hazardStateSide1 : hazardStateSide2;
        base.forEach((s, integer) -> {
            TranslatableContents translatableContents = new TranslatableContents("cobblemon.move." + s, null,new Object[]{});
            Component component = MutableComponent.create(translatableContents);
            result.put(component.getString(), integer);
        });

        return result;
    }


    public static void setWeather(String state){
        weather = state;
        weatherTurns = 5;
    }
    public static Object[] getWeatherState(){
        TranslatableContents translatableContents = new TranslatableContents("cobblemon.battle.weather." + weather + ".upkeep", null,new Object[]{});
        Component component = MutableComponent.create(translatableContents);
        return new Object[]{weather.isEmpty()? "" :component.getString(), weatherTurns};
    }
    public static void updateWeather(){
        weatherTurns--;
    }

    public static void addRoom(String state){
        rooms.put(state, 5);
    }
    public static Map<String,Integer> getRoomState(){
        Map<String,Integer> result = new HashMap<>();
        rooms.forEach((s, integer) -> {
            TranslatableContents translatableContents = new TranslatableContents("cobblemon.move." + s, null,new Object[]{});
            Component component = MutableComponent.create(translatableContents);
            result.put(component.getString(), integer);
        });
        return result;
    }
    public static void removeRoom(String state)
    {
        rooms.remove(state);
    }
    public static void updateRoom(){
        rooms.forEach((s, integer) -> rooms.put(s,integer -1));
    }

    public static void endOfBattle()
    {
        rooms.clear();
        weather = "";
        hazardStateSide1.clear();
        hazardStateSide2.clear();
    }
}
