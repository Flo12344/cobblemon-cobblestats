package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import io.github.flo12344.cobblestats.common.client.net.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.jetbrains.annotations.NotNull;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class CobbleStatsBattleOverlay {
    public static void RenderBefore(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, boolean isHovered, boolean isCompact) {
        var mc = Minecraft.getInstance();

        var battlePokemon = activeBattlePokemon.getBattlePokemon();
        if (battlePokemon == null) {
            return;
        }
        var battle = CobblemonClient.INSTANCE.getBattle();
        if (battle == null) {
            return;
        }
        int playerNumberOffset = (Character.getNumericValue(activeBattlePokemon.getActorShowdownId().charAt(1)) - 1) / 2 * 10;

        float original_Y = VERTICAL_INSET + rank * (isCompact ? COMPACT_VERTICAL_SPACING : VERTICAL_SPACING) + (left ? playerNumberOffset : (battle.getBattleFormat().getBattleType().getActorsPerSide() - 1) * 10 - playerNumberOffset);
        float original_X = activeBattlePokemon.getXDisplacement();

        var portraitOffsetY = isCompact ? COMPACT_PORTRAIT_OFFSET_Y : PORTRAIT_OFFSET_Y;
        var portraitDiameter = isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER;
        var infoOffsetX = isCompact ? COMPACT_INFO_OFFSET_X : INFO_OFFSET_X;
        var titleWidth = isCompact ? COMPACT_TILE_WIDTH : TILE_WIDTH;

        original_X += left ? infoOffsetX + portraitDiameter : 0;
        original_Y += portraitOffsetY + portraitDiameter * .75F;

        String key = getKey(activeBattlePokemon);

        original_X += 5;
        StatsRenderer.draw(context, activeBattlePokemon, left, isCompact, key, original_X, original_Y);
    }


    public static void RenderAfter(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, boolean isHovered, boolean isCompact) {
        var mc = Minecraft.getInstance();

        var battlePokemon = activeBattlePokemon.getBattlePokemon();
        if (battlePokemon == null) {
            return;
        }
        var battle = CobblemonClient.INSTANCE.getBattle();
        if (battle == null) {
            return;
        }
        int playerNumberOffset = (Character.getNumericValue(activeBattlePokemon.getActorShowdownId().charAt(1)) - 1) / 2 * 10;

        float original_Y = VERTICAL_INSET + rank * (isCompact ? COMPACT_VERTICAL_SPACING : VERTICAL_SPACING) + (left ? playerNumberOffset : (battle.getBattleFormat().getBattleType().getActorsPerSide() - 1) * 10 - playerNumberOffset);

        var portraitOffsetY = isCompact ? COMPACT_PORTRAIT_OFFSET_Y : PORTRAIT_OFFSET_Y;
        var portraitDiameter = isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER;


        original_Y += portraitOffsetY + portraitDiameter * .75F;

        if (CobblestatsClientConfig.ShowPokemonType) {
            TypeRenderer.draw(context, left, battlePokemon, (float) portraitDiameter, original_Y);
        }


        if (left) {
            final int[] y_pos = {VERTICAL_INSET + 30};
            if (!((String) TerrainBattleState.getTerrainState()[0]).isEmpty() && CobblestatsClientConfig.ShowTerrain) {
                TerrainRenderer.draw(context, y_pos);
            }

            RoomRenderer.draw(context, y_pos);


            if (!((String) TerrainBattleState.getWeatherState()[0]).isEmpty() && CobblestatsClientConfig.ShowWeather) {
                WeatherRenderer.draw(context, y_pos);
            }
        }
        if (CobblestatsClientConfig.ShowHazards)
            HazardRenderer.draw(context, left, isCompact, mc);
        if (!ClientData.SERVER_COMPAT)
            return;
        if (!left && activeBattlePokemon.getActor().getType() == ActorType.WILD && !CobblestatsClientConfig.ForceHidePokeball)
            return;

        var infoOffsetX = isCompact ? COMPACT_INFO_OFFSET_X : INFO_OFFSET_X;
        PokeballRenderer.Draw(context, activeBattlePokemon, left, isCompact, battle, infoOffsetX, portraitDiameter);
    }

    private static @NotNull String getKey(ActiveClientBattlePokemon activeBattlePokemon) {
        String key;
        assert activeBattlePokemon.getBattlePokemon() != null;
        var value = activeBattlePokemon.getBattlePokemon().getDisplayName().getContents();
        if (value instanceof TranslatableContents) {
            key = ((TranslatableContents) value).getKey();
        } else {
            key = ((PlainTextContents.LiteralContents) value).text();
        }

        if (activeBattlePokemon.getBattlePokemon().getActor().getDisplayName().getContents() instanceof PlainTextContents.LiteralContents(
                String text1
        )) {
            key = text1 + "/" + key;
        } else if (activeBattlePokemon.getBattlePokemon().getActor().getDisplayName().getContents() instanceof TranslatableContents translatableContents) {
            if (!translatableContents.getKey().contains(".") || translatableContents.getKey().contains("trainer"))
                key = translatableContents.getKey() + "/" + key;
        }
        return key;
    }
}
