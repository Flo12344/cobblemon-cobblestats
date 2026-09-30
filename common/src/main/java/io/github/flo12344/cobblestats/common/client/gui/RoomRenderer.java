package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class RoomRenderer {
    public static void draw(GuiGraphics ctx, int[] y_pos) {
        float scale = CobblestatsClientConfig.OtherFontScale;
        var mc = Minecraft.getInstance();
        TerrainBattleState.getRoomState().forEach((s, integer) -> {
            String text = s + " " + integer;
            int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));
            RenderUtils.drawRectText(ctx, mc.font, text, x_pos, y_pos[0], scale);
            y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
        });
    }
}
