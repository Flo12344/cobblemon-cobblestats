package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class RoomRenderer {
    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "room_badge");

    public static void draw(GuiGraphics ctx, int[] y_pos) {
        float scale = CobblestatsClientConfig.WeatherRoomFontScale;
        var mc = Minecraft.getInstance();
        TerrainBattleState.getRoomState().forEach((s, integer) -> {
            String text = s + " " + integer;
            int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale) - 8);
            RenderUtils.drawBadge(ctx, BADGE, text, x_pos, y_pos[0], 8, 1, 16, 6, 1);
            y_pos[0] += (int) (mc.font.lineHeight * scale) + 5;
        });
    }
}
