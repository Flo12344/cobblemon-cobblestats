package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import io.github.flo12344.cobblestats.common.client.BattleStateTracker;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class StatsRenderer {
    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge");
    public static final ResourceLocation BADGE_INVERTED =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge-inverted");

    public static void draw(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, boolean isCompact, String key, float original_X, float original_Y) {
        var mc = Minecraft.getInstance();
        var stats = BattleStateTracker.getChangedStats(key);

        var battlePokemon = activeBattlePokemon.getBattlePokemon();
        var battle = CobblemonClient.INSTANCE.getBattle();
        var slotCount = battle.getBattleFormat().getBattleType().getSlotsPerActor();

        int maxX;
        var x = HORIZONTAL_INSET + (slotCount - 1 - 1) * HORIZONTAL_SPACING;
        int infoStart = isCompact ?
                COMPACT_PORTRAIT_DIAMETER + COMPACT_PORTRAIT_OFFSET_X * 3 :
                PORTRAIT_DIAMETER + PORTRAIT_OFFSET_X * 3;
        int maxStatLength = isCompact ?
                COMPACT_TILE_WIDTH - COMPACT_PORTRAIT_DIAMETER :
                TILE_WIDTH - PORTRAIT_DIAMETER;
        if (!left) {
            x = mc.getWindow().getGuiScaledWidth() - x - infoStart;
            maxX = x - maxStatLength;
        } else {
            x = x + infoStart;
            maxX = x + maxStatLength;
        }
        int ox = x + (left ? -9 : 9);
        if (activeBattlePokemon.getBattlePokemon() == null)
            return;
        if (activeBattlePokemon.getBattlePokemon().getStatus() != null) {
            if (left) {
                x += 35;
            } else {
                x -= 35;
            }
        }
        float y = original_Y;
        int i = 0;
        float scale = CobblestatsClientConfig.StatsFontScale;
        for (var s : stats) {
            int textWidth = (int) (mc.font.width(s) * scale);
            if (left && x + textWidth > maxX) {
                x = ox;
                y += mc.font.lineHeight * scale + 3;
            }

            if (!left) {
                if (x < maxX) {
                    x = ox;
                    y += mc.font.lineHeight * scale + 3;
                }
                x -= (textWidth + (i > 0 ? 4 : 0));
            }
            drawBadge(context, mc.font, s, (int) x, (int) y, i++, left);
            if (left)
                x += (textWidth + (i > 0 ? 5 : 1));
        }
    }

    private static void drawBadge(GuiGraphics ctx, Font font, String text, int x, int y, int depth, boolean left) {
        float size = CobblestatsClientConfig.StatsFontScale;
        int textWidth = (int) (font.width(text) * size);
        int textHeight = (int) (font.lineHeight * size);

        ctx.pose().pushPose();
        ctx.pose().translate(0, 0, -depth);

        ctx.blitSprite(left ? BADGE : BADGE_INVERTED, x - 4, y - 1, textWidth + 8, textHeight + 4);

        ctx.pose().popPose();

        ctx.pose().pushPose();
        ctx.pose().translate(x, y + 1, 0);
        ctx.pose().pushPose();
        ctx.pose().scale(size, size, 0);
        ctx.drawString(font, text, 0, 0, 16777215, true);
        ctx.pose().popPose();
        ctx.pose().popPose();
    }
}
