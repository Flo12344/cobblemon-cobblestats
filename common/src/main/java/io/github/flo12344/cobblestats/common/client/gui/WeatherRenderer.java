package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class WeatherRenderer {

    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "weather_badge");

    public static void draw(GuiGraphics context, int[] y_pos) {
        final float scale = CobblestatsClientConfig.WeatherRoomFontScale;
        var weather = TerrainBattleState.getWeatherState();
        String text = ((String) weather[0]);
        if (((Integer) weather[1]) >= -9) {
            if (((Integer) weather[1]) < 0) {
                text += " " + (((Integer) weather[1]) + 3);
            } else {
                text += " " + weather[1] + " or " + (((Integer) weather[1]) + 3);
            }
        }
        var mc = Minecraft.getInstance();
        int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale) - 8);
        RenderUtils.drawBadge(context, BADGE, text, x_pos, y_pos[0], 8, 2, 16, 8, 1, scale);
        y_pos[0] += (int) (mc.font.lineHeight * scale) + 5;
    }
}
