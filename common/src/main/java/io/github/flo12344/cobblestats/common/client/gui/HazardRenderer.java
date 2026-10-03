package io.github.flo12344.cobblestats.common.client.gui;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class HazardRenderer {
    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "hazard_badge");

    public static final ResourceLocation BADGE_INVERTED =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "hazard_badge_inverted");

    public static void draw(GuiGraphics context, boolean left, boolean isCompact) {
        var font = Minecraft.getInstance().font;
        final int[] _y = {(VERTICAL_INSET + ((isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER) * 4))};
        float scale = CobblestatsClientConfig.OtherFontScale;
        for (var entry : TerrainBattleState.getHazardStates(left).entrySet()) {
            String text = entry.getKey();
            int integer = entry.getValue();
            if (integer > 1)
                text += " " + integer;
            int _x = VERTICAL_INSET;
            if (!left) {
                _x = Minecraft.getInstance().getWindow().getGuiScaledWidth() - _x - ((int) (font.width(text) * scale)) - 8;
            }
            if (left)
                RenderUtils.drawBadge(context, BADGE, text, _x, (int) _y[0], 4, 2, 12, 7, 1, scale);
            else
                RenderUtils.drawBadge(context, BADGE_INVERTED, text, _x, (int) _y[0], 8, 2, 12, 7, 1, scale);

            _y[0] += (int) (font.lineHeight * scale) + 10;
        }
    }
}
