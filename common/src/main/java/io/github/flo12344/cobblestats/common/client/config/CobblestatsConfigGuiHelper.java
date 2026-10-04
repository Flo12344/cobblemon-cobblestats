package io.github.flo12344.cobblestats.common.client.config;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CobblestatsConfigGuiHelper {
    public record ConfigEntry(String translationKey, Supplier<Boolean> getter, Consumer<Boolean> setter,
                              Boolean defaults) {
    }

    public static final Set<ConfigEntry> VisiblitySettings = Set.of(
            new ConfigEntry("accurate_pokeball", () -> CobblestatsClientConfig.AccuratePokeballIfAvailable,
                    v -> CobblestatsClientConfig.AccuratePokeballIfAvailable = v, true),
            new ConfigEntry("force_hide_pokeball", () -> CobblestatsClientConfig.ForceHidePokeball,
                    v -> CobblestatsClientConfig.ForceHidePokeball = v, false),
            new ConfigEntry("show_pokemon_type", () -> CobblestatsClientConfig.ShowPokemonType,
                    v -> CobblestatsClientConfig.ShowPokemonType = v, true),
            new ConfigEntry("show_hazards", () -> CobblestatsClientConfig.ShowHazards,
                    v -> CobblestatsClientConfig.ShowHazards = v, true),
            new ConfigEntry("show_weather", () -> CobblestatsClientConfig.ShowWeather,
                    v -> CobblestatsClientConfig.ShowWeather = v, true),
            new ConfigEntry("show_terrain", () -> CobblestatsClientConfig.ShowTerrain,
                    v -> CobblestatsClientConfig.ShowTerrain = v, true),
            new ConfigEntry("show_other", () -> CobblestatsClientConfig.ShowOther,
                    v -> CobblestatsClientConfig.ShowOther = v, true),
            new ConfigEntry("show_stats_stages", () -> CobblestatsClientConfig.ShowStatsStages,
                    v -> CobblestatsClientConfig.ShowStatsStages = v, true)
    );

    public record FontSizeEntry(String translationKey, Supplier<Float> getter, Consumer<Float> setter,
                                Integer defaults) {
    }

    public static final Set<FontSizeEntry> FontSizeSettings = Set.of(
            new FontSizeEntry("stats_font_scale", () -> CobblestatsClientConfig.StatsFontScale,
                    v -> CobblestatsClientConfig.StatsFontScale = v, 75),
            new FontSizeEntry("weather_room_font_scale", () -> CobblestatsClientConfig.WeatherRoomFontScale,
                    v -> CobblestatsClientConfig.WeatherRoomFontScale = v, 75),
            new FontSizeEntry("hazard_font_scale", () -> CobblestatsClientConfig.HazardFontScale,
                    v -> CobblestatsClientConfig.HazardFontScale = v, 75)
    );

}
