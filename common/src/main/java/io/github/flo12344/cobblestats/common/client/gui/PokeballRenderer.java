package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.battle.ClientBattle;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.net.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class PokeballRenderer {
    public static void Draw(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, boolean isCompact, ClientBattle battle, int infoOffsetX, int portraitDiameter) {
        var mc = Minecraft.getInstance();
        var pokeballX = HORIZONTAL_INSET;
        if (!left) {
            pokeballX = mc.getWindow().getGuiScaledWidth() - pokeballX - (isCompact ? COMPACT_TILE_WIDTH : TILE_WIDTH);
        }
        final float[] x = {pokeballX + battle.getBattleFormat().getBattleType().getSlotsPerActor() + (left ? infoOffsetX + portraitDiameter : 0)};
        final int _y_pos = 4;
        var matrix = context.pose();
        matrix.pushPose();
        matrix.translate(x[0], 4, 0);
        float ball_size = CobblestatsClientConfig.PokeballSize;
        matrix.scale(ball_size, ball_size, ball_size);
        ClientData.pokemonCounts.forEach((uuid, integer) -> {
            String pokeball = "poke_ball";
            if (activeBattlePokemon.getActor().getUuid().equals(uuid)) {
                int pos = left ? (int) (isCompact ? COMPACT_TILE_WIDTH - ball_size * ((18 + 1) * 6) : TILE_WIDTH - ball_size * ((18 + 1) * 6)) : 0;
                var team = integer.getTeamPokemons();
                Number color_shift;
                for (int i = 0; i < 6; i++) {
                    int margin = 1;
                    if (i < team.size() && team.get(i) != null && CobblestatsClientConfig.AccuratePokeballIfAvailable) {
                        pokeball = team.get(i).ball;
                        if (!team.get(i).is_ko)
                            color_shift = 1;
                        else {
                            color_shift = 0.5;
                        }
                    } else {
                        color_shift = 0;
                    }
                    ResourceLocation res = ResourceLocation.fromNamespaceAndPath("cobblemon", "textures/gui/ball/" + pokeball + ".png");

                    GuiUtilsKt.blitk(matrix, res,
                            pos, 0, 20, 18, 0, 0, 18, 44, 5, color_shift, color_shift, color_shift, 1);
                    pos += 18 + margin;
                }
            }
        });
        matrix.popPose();
    }
}
