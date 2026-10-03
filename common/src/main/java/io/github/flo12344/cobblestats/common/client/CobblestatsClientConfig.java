package io.github.flo12344.cobblestats.common.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CobblestatsClientConfig {
    private static final String DIR_PATH = "./config/cobblestats";
    private static final String FILE_PATH = "./config/cobblestats/config.json";
    public static boolean loaded = false;

    public enum StatRender {MULTIPLIER, ARROW, NUMBER}

    public static StatRender StatsRenderType = StatRender.MULTIPLIER;
    public static boolean AccuratePokeballIfAvailable = true;
    public static boolean ForceHidePokeball = false;
    public static boolean ShowPokemonType = true;

    public static boolean ShowStatsStages = true;
    public static boolean ShowHazards = true;
    public static boolean ShowWeather = true;
    public static boolean ShowTerrain = true;
    public static boolean ShowOther = true;
    public static float StatsFontScale = 0.5f;
    public static float WeatherRoomFontScale = 1f;
    public static float OtherFontScale = 1f;
    public static float PokeballSize = 0.45f;
    public static float TypeSize = 0.45f;

    private static @NotNull JsonObject getJsonObject() {
        JsonObject json = new JsonObject();
        json.addProperty("ForceHidePokeball", ForceHidePokeball);
        json.addProperty("AccuratePokeballIfAvailable", AccuratePokeballIfAvailable);
        json.addProperty("ShowPokemonType", ShowPokemonType);

        json.addProperty("ShowStatsStages", ShowStatsStages);
        json.addProperty("ShowTerrain", ShowTerrain);
        json.addProperty("ShowHazards", ShowHazards);
        json.addProperty("ShowWeather", ShowWeather);
        json.addProperty("ShowOther", ShowOther);

        json.addProperty("StatsRenderType", StatsRenderType.toString());
        json.addProperty("StatsFontScale", StatsFontScale);
        json.addProperty("WeatherRoomFontScale", WeatherRoomFontScale);
        json.addProperty("OtherFontScale", OtherFontScale);
        json.addProperty("PokeballSize", PokeballSize);
        json.addProperty("TypeSize", TypeSize);
        return json;
    }

    public static void save() {
        JsonObject json = getJsonObject();

        try {
            Files.createDirectories(Path.of(DIR_PATH));
            try (FileWriter writer = new FileWriter(FILE_PATH)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                writer.write(gson.toJson(json));
            }
        } catch (IOException e) {
            LogUtils.getLogger().error("Failed to save CobbleStats config:", e);
        }
    }

    public static void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            LogUtils.getLogger().info("CobbleStats config not found, creating default.");
            save();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            if (json.has("ForceHidePokeball")) {
                ForceHidePokeball = json.get("ForceHidePokeball").getAsBoolean();
            }
            if (json.has("AccuratePokeballIfAvailable")) {
                AccuratePokeballIfAvailable = json.get("AccuratePokeballIfAvailable").getAsBoolean();
            }
            if (json.has("ShowPokemonType")) {
                ShowPokemonType = json.get("ShowPokemonType").getAsBoolean();
            }
            if (json.has("ShowStatsStages")) {
                ShowStatsStages = json.get("ShowStatsStages").getAsBoolean();
            }
            if (json.has("ShowTerrain")) {
                ShowTerrain = json.get("ShowTerrain").getAsBoolean();
            }
            if (json.has("ShowHazards")) {
                ShowHazards = json.get("ShowHazards").getAsBoolean();
            }
            if (json.has("ShowWeather")) {
                ShowWeather = json.get("ShowWeather").getAsBoolean();
            }
            if (json.has("ShowOther")) {
                ShowOther = json.get("ShowOther").getAsBoolean();
            }
            if (json.has("StatsRenderType")) {
                StatsRenderType = StatRender.valueOf(json.get("StatsRenderType").getAsString().toUpperCase());
            }
            if (json.has("StatsFontScale")) {
                StatsFontScale = json.get("StatsFontScale").getAsFloat();
            }
            if (json.has("OtherFontScale")) {
                OtherFontScale = json.get("OtherFontScale").getAsFloat();
            }
            if (json.has("PokeballSize")) {
                PokeballSize = json.get("PokeballSize").getAsFloat();
            }
            if (json.has("TypeSize")) {
                TypeSize = json.get("TypeSize").getAsFloat();
            }
            if (json.has("WeatherRoomFontScale")) {
                WeatherRoomFontScale = json.get("WeatherRoomFontScale").getAsFloat();
            }
        } catch (Exception e) {
            LogUtils.getLogger().error("Failed to save CobbleStats config:", e);
        }
        loaded = true;
    }
}
