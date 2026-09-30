package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class HazardRenderer {
    public static void draw(GuiGraphics context, boolean left, boolean isCompact, Minecraft mc) {
        final int[] _y = {(VERTICAL_INSET + ((isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER) * 4))};
        TerrainBattleState.getHazardStates(left).forEach((s, integer) -> {
            String text = s;
            if (integer > 1)
                text += " " + integer;
            int _x = VERTICAL_INSET;
            if (!left) {
                _x = mc.getWindow().getGuiScaledWidth() - _x - ((int) (mc.font.width(text) * (float) 0.5));
            }
            RenderUtils.drawRectText(context, mc.font, text, _x, _y[0], CobblestatsClientConfig.OtherFontScale);
            _y[0] += (int) (mc.font.lineHeight * (float) 0.5) + 3;
        });
    }
}
