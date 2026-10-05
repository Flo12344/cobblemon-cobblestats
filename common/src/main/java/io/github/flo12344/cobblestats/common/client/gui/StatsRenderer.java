package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import io.github.flo12344.cobblestats.common.client.BattleStateTracker;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.net.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class StatsRenderer {
    public static final ResourceLocation BADGE =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge");
    public static final ResourceLocation BADGE_INVERTED =
            ResourceLocation.fromNamespaceAndPath("cobblestats", "badge_inverted");

    public static void draw(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, boolean isCompact, String key, int rank, float original_Y) {
        var mc = Minecraft.getInstance();
        var stats = BattleStateTracker.getChangedStats(key);

        var battlePokemon = activeBattlePokemon.getBattlePokemon();
        var battle = CobblemonClient.INSTANCE.getBattle();
        var slotCount = battle.getBattleFormat().getBattleType().getSlotsPerActor();

        int maxX;
        var x = HORIZONTAL_INSET + (slotCount - rank - 1) * HORIZONTAL_SPACING;
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
        if (ClientData.XPBAR && left) {
            y += CobbleStatsBattleOverlay.XpBarYOffset;
            x -= CobbleStatsBattleOverlay.XpBarXOffset;
        }
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
            RenderUtils.drawBadge(context, left ? BADGE : BADGE_INVERTED, s, x, (int) y, 4, 1, 8, 4, i++, CobblestatsClientConfig.StatsFontScale);
//            drawBadge(context, mc.font, s, (int) x, (int) y, i++, left);
            if (left)
                x += (textWidth + (i > 0 ? 5 : 1));
        }
    }
}
