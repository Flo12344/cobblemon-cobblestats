package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import io.github.flo12344.cobblestats.common.client.BattleStateTracker;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.COMPACT_PORTRAIT_DIAMETER;
import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.PORTRAIT_DIAMETER;

public class StatsRenderer {
    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge");
    public static final ResourceLocation BADGE_INVERTED =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge-inverted");

    public static void draw(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, boolean isCompact, String key, float original_X, float original_Y) {
        var mc = Minecraft.getInstance();
        var stats = BattleStateTracker.getChangedStats(key);
        float maxX;
        float x = original_X;
        if (left) {
            maxX = isCompact ? (BattleOverlay.COMPACT_TILE_WIDTH - BattleOverlay.COMPACT_PORTRAIT_DIAMETER - BattleOverlay.COMPACT_PORTRAIT_OFFSET_Y * 2) : (BattleOverlay.TILE_WIDTH - BattleOverlay.PORTRAIT_DIAMETER - BattleOverlay.PORTRAIT_OFFSET_Y * 2) - 1F;
        } else {
            x += isCompact ?
                    (BattleOverlay.COMPACT_TILE_WIDTH - BattleOverlay.COMPACT_PORTRAIT_DIAMETER - BattleOverlay.COMPACT_PORTRAIT_OFFSET_Y * 2 - 32) :
                    (BattleOverlay.TILE_WIDTH - (BattleOverlay.PORTRAIT_DIAMETER + BattleOverlay.PORTRAIT_OFFSET_Y) * 2) - 1;
            maxX = isCompact ? (original_X - COMPACT_PORTRAIT_DIAMETER) : (original_X - PORTRAIT_DIAMETER);
        }
        float finalOriginal_X = original_X;
        if (activeBattlePokemon.getBattlePokemon() == null)
            return;
        if (activeBattlePokemon.getBattlePokemon().getStatus() != null) {
            if (left) {
                x += 35;
            } else {
                finalOriginal_X -= 35;
            }
        }
        float y = original_Y;
        int i = 0;

        for (var s : stats) {
            float scale = CobblestatsClientConfig.StatsFontScale;
            if (x + ((int) (mc.font.width(s) * scale)) > finalOriginal_X + maxX) {
                x = finalOriginal_X;
                y += mc.font.lineHeight * scale + 3;
            }
            x = drawBadge(context, mc.font, s, (int) x, (int) y, scale, i++, left) + 4;
        }
    }

    private static int drawBadge(GuiGraphics ctx, Font font, String text, int x, int y, float size, int depth, boolean left) {
        int margin = 1;

        int textWidth = (int) (font.width(text) * size);
        ctx.pose().pushPose();
        ctx.pose().translate(0, 0, -depth);
        if (left)
            ctx.blitSprite(BADGE, x - 5, y - 1, textWidth + 9, (int) (font.lineHeight * size) + 4);
        else {
//            x -= textWidth + 9;
            ctx.blitSprite(BADGE_INVERTED, x - 5, y - 1, textWidth + 9, (int) (font.lineHeight * size) + 4);
        }
        ctx.pose().popPose();

        ctx.pose().pushPose();
        ctx.pose().translate(x, y + 1, 0);
        ctx.pose().scale(size, size, 0);
        ctx.drawString(font, text, 0, 0, 16777215, true);
        ctx.pose().popPose();
        return x + (int) (font.width(text) * size) + margin;
    }
}
