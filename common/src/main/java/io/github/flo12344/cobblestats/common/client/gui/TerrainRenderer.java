package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class TerrainRenderer {

    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "terrain_badge");

    public static void draw(GuiGraphics context, int[] y_pos) {
        final float scale = CobblestatsClientConfig.HazardFontScale;
        var terrain = TerrainBattleState.getTerrainState();
        String text = ((String) terrain[0]);
        if (((Integer) terrain[1]) < 0) {
            text += " " + (((Integer) terrain[1]) + 3);
        } else {
            text += " " + terrain[1] + " or " + (((Integer) terrain[1]) + 3);

        }
        var mc = Minecraft.getInstance();
        int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale) - 8);
        RenderUtils.drawBadge(context, BADGE, text, x_pos, y_pos[0], 8, 1, 16, 6, 1, scale);
        y_pos[0] += (int) (mc.font.lineHeight * scale) + 5;
    }
}
