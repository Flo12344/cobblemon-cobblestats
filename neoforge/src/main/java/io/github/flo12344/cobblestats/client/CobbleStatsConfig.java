package io.github.flo12344.cobblestats.client;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.config.CobblestatsConfigGuiHelper;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;


public class CobbleStatsConfig {
    public static Screen getConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("cobblestats.config.title"));

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("cobblestats.config.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        for (CobblestatsConfigGuiHelper.ConfigEntry entry : CobblestatsConfigGuiHelper.entries) {
            general.addEntry(entryBuilder.startBooleanToggle(Component.translatable("cobblestats.config." + entry.translationKey()), entry.getter().get())
                    .setTooltip(Component.translatable("cobblestats.config." + entry.translationKey() + ".description"))
                    .setDefaultValue(entry.defaults())
                    .setSaveConsumer(entry.setter())
                    .build());
        }

        general.addEntry(entryBuilder.startSelector(Component.translatable("cobblestats.config.stats_render_type"),
                        CobblestatsClientConfig.StatRender.values(),
                        CobblestatsClientConfig.StatsRenderType)
                .setDefaultValue(CobblestatsClientConfig.StatRender.MULTIPLIER)
                .setNameProvider(statRender -> Component.translatable("cobblestats.config.stats_render_type." + statRender.toString().toLowerCase()))
                .setSaveConsumer(statRender -> CobblestatsClientConfig.StatsRenderType = statRender)
                .build()
        );
        builder.setSavingRunnable(CobblestatsClientConfig::save);
        return builder.build();
    }
}
