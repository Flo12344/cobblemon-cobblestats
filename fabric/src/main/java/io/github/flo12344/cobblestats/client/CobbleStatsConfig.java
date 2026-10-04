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

        var visibility = entryBuilder.startSubCategory(Component.translatable("cobblestats.config.category.visibility"));
        for (CobblestatsConfigGuiHelper.ConfigEntry entry : CobblestatsConfigGuiHelper.VisiblitySettings) {
            visibility.add(entryBuilder.startBooleanToggle(Component.translatable("cobblestats.config." + entry.translationKey()), entry.getter().get())
                    .setTooltip(Component.translatable("cobblestats.config." + entry.translationKey() + ".description"))
                    .setDefaultValue(entry.defaults())
                    .setSaveConsumer(entry.setter())
                    .build());
        }
        general.addEntry(visibility.build());

        var visual = entryBuilder.startSubCategory(Component.translatable("cobblestats.config.category.visual"));
        visual.add(entryBuilder.startSelector(Component.translatable("cobblestats.config.stats_render_type"),
                        CobblestatsClientConfig.StatRender.values(),
                        CobblestatsClientConfig.StatsRenderType)
                .setDefaultValue(CobblestatsClientConfig.StatRender.MULTIPLIER)
                .setNameProvider(statRender -> Component.translatable("cobblestats.config.stats_render_type." + statRender.toString().toLowerCase()))
                .setSaveConsumer(statRender -> CobblestatsClientConfig.StatsRenderType = statRender)
                .build()
        );


        visual.add(entryBuilder.startIntSlider(Component.translatable("cobblestats.config.pokeball_size"),
                        (int) (CobblestatsClientConfig.PokeballSize * 100), 25, 250)
                .setDefaultValue(50)
                .setSaveConsumer(integer -> CobblestatsClientConfig.PokeballSize = integer / 100f)
                .build());

        visual.add(entryBuilder.startIntSlider(Component.translatable("cobblestats.config.type_size"),
                        (int) (CobblestatsClientConfig.TypeSize * 100), 25, 250)
                .setDefaultValue(50)
                .setSaveConsumer(integer -> CobblestatsClientConfig.TypeSize = integer / 100f)
                .build());
        general.addEntry(visual.build());

        var sub = entryBuilder.startSubCategory(Component.translatable("cobblestats.config.category.font"));
        for (var entry : CobblestatsConfigGuiHelper.FontSizeSettings) {
            sub.add(entryBuilder.startIntSlider(Component.translatable("cobblestats.config." + entry.translationKey()), (int) (entry.getter().get() * 100), 50, 150)
                    .setDefaultValue(entry.defaults())
                    .setSaveConsumer(integer -> entry.setter().accept(integer / 100f))
                    .build());
        }
        general.addEntry(sub.build());

        builder.setSavingRunnable(CobblestatsClientConfig::save);
        return builder.build();
    }
}
