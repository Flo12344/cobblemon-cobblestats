package io.github.flo12344.cobblestats.common;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.Objects;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;
import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.VERTICAL_INSET;

public class BattleProcess {
    public static void drawUI(GuiGraphics context,ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, boolean isHovered, boolean isCompact)
    {
        var mc = Minecraft.getInstance();

        var battlePokemon = activeBattlePokemon.getBattlePokemon();
        if(battlePokemon == null) { return; }
        var battle = CobblemonClient.INSTANCE.getBattle();
        if(battle == null) { return; }

        int playerNumberOffset = (Character.getNumericValue(activeBattlePokemon.getActorShowdownId().charAt(1)) - 1) / 2 * 10;

        float original_Y = VERTICAL_INSET + rank * (isCompact? COMPACT_VERTICAL_SPACING : VERTICAL_SPACING) + (left? playerNumberOffset : (battle.getBattleFormat().getBattleType().getActorsPerSide() - 1) * 10 - playerNumberOffset);

        float original_X = activeBattlePokemon.getXDisplacement();

        var portraitOffsetY = isCompact? COMPACT_PORTRAIT_OFFSET_Y : PORTRAIT_OFFSET_Y;
        var portraitDiameter = isCompact? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER;
        var infoOffsetX = isCompact?  COMPACT_INFO_OFFSET_X : INFO_OFFSET_X;


        original_X += left ? infoOffsetX + portraitDiameter : 0;
        original_Y += portraitOffsetY + portraitDiameter * .75F;

        var hue = activeBattlePokemon.getHue();
        float r = ((hue >> 16) & 0b11111111) / 255F;
        float g = ((hue >> 8) & 0b11111111) / 255F;
        float b = (hue & 0b11111111) / 255F;

        String key;
        var value = activeBattlePokemon.getBattlePokemon().getDisplayName().getContents();
        if(value instanceof TranslatableContents)
        {
            key = ((TranslatableContents) value).getKey();
        }
        else {
            key = ((PlainTextContents.LiteralContents) value).text();
        }

        if(activeBattlePokemon.getBattlePokemon().getActor().getDisplayName().getContents() instanceof PlainTextContents.LiteralContents(String text1))
        {
            key = text1 + "/" + key;
        }
        original_X += 5;
        var stats = BattleStateTracker.getChangedStats(key);
        var maxX = isCompact?(COMPACT_TILE_WIDTH - COMPACT_PORTRAIT_DIAMETER-COMPACT_PORTRAIT_OFFSET_Y*2) :(TILE_WIDTH-PORTRAIT_DIAMETER-PORTRAIT_OFFSET_Y*2) -1F;
        final float[] x = {original_X};
        final float[] finalOriginal_X = {original_X};
        if(activeBattlePokemon.getBattlePokemon().getStatus() != null)
        {
            if(left)
            {
                x[0] += 35;
            }
            else
            {
                finalOriginal_X[0] -= 35;
            }
        }
        final float scale = 0.5f;
        final float[] y = {original_Y};
        stats.forEach(s -> {
            if(x[0] + ((int) (mc.font.width(s) * scale)) > finalOriginal_X[0] + maxX){
                x[0] = finalOriginal_X[0];
                y[0] += mc.font.lineHeight * scale +3;
            }
            x[0] = drawStats(context, mc.font, s, (int) x[0], (int) y[0], scale, 0xFFFFFF) + 3;
        });

        final int[] _y = {(int) (original_Y + (isCompact? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER))};
        TerrainBattleState.getHazardStates(left).forEach((s, integer) -> {
            String text = s;
            if (integer > 1)
                text += " " + integer;
            int _x = VERTICAL_INSET;
            if (!left) {
                _x = mc.getWindow().getGuiScaledWidth() - _x - ((int) (mc.font.width(text) * scale));
            }
            drawStats(context, mc.font, text, _x, _y[0], scale,0xFFFFFF);
            _y[0] += (int) (mc.font.lineHeight * scale)+3;
        });

        if(left)
        {
            final int[] y_pos = {VERTICAL_INSET};
            if(!((String) TerrainBattleState.getTerrainState()[0]).isEmpty())
            {
                var terrain = TerrainBattleState.getTerrainState();
                String text = ((String) terrain[0]);
                if(((Integer) terrain[1]) < 0)
                {
                    text += " " + (((Integer) terrain[1]) + 3);
                }
                else {
                    text += " " + terrain[1] + " or " + (((Integer) terrain[1]) + 3);

                }
                int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));

                drawStats(context, mc.font, text, (int)x_pos, y_pos[0], scale, 0xFFFFFF);
                y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
            }

            TerrainBattleState.getRoomState().forEach((s, integer) -> {
                String text = s + " " + integer;
                int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));
                drawStats(context, mc.font, text, (int)x_pos, y_pos[0], scale, 0xFFFFFF);
                y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
            });

            if(!((String) TerrainBattleState.getWeatherState()[0]).isEmpty())
            {
                var weather = TerrainBattleState.getWeatherState();
                String text = ((String) weather[0]);
                if(((Integer) weather[1]) < 0)
                {
                    text += " " + (((Integer) weather[1])+ 3);
                }
                else {
                    text += " " + weather[1] + " or " + (((Integer) weather[1]) + 3);

                }
                int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));
                drawStats(context, mc.font, text, (int)x_pos, y_pos[0], scale, 0xFFFFFF);
                y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
            }
        }
    }

    public static Object[] processBattleData(BattleMessagePacket messagePacket, String current_atk,String current_pkm,PokemonBattleState tmp_stat_holder) {
        String[] MainActionSplit = ((TranslatableContents) messagePacket.getMessages().getFirst().getContents()).getKey().split("\\.");
        if(MainActionSplit.length < 3 || !Objects.equals(MainActionSplit[1], "battle"))
            return new Object[]{current_atk,current_pkm,tmp_stat_holder};

        Object[] object_args = ((TranslatableContents) messagePacket.getMessages().getFirst().getContents()).getArgs();
        switch (MainActionSplit[2])
        {
            case "unboost":
            case "boost":
                if(MainActionSplit.length > 3 && Objects.equals(MainActionSplit[3], "cap"))
                    return new Object[]{current_atk,current_pkm,tmp_stat_holder};

                BattleStateTracker.changeStats(
                        getPkm(object_args),
                        ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2],
                        MainActionSplit[3],
                        Objects.equals(MainActionSplit[2], "boost"));
                break;

            case "used_move_on":
            case "used_move":
                current_pkm = getPkm(object_args);
                current_atk =((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2];
                break;

            case "switch":
                String switchedTo;
                if(MainActionSplit[3].equals("self"))
                {
                    String pokemon;
                    if(object_args[0] instanceof MutableComponent){
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
                    }
                    else
                    {
                        pokemon = (String) object_args[0];
                    }
                    String owner = Minecraft.getInstance().player.getDisplayName().getString();
                    switchedTo = owner +"/"+pokemon;
                }
                else {
                    String pokemon;
                    if(object_args[1] instanceof MutableComponent){
                        pokemon = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey();
                    }
                    else
                    {
                        pokemon = (String) object_args[1];
                    }
                    String owner = object_args[0].toString();
                    switchedTo = owner +"/"+pokemon;
                }
                BattleStateTracker.addPokemon(switchedTo);

                if(Objects.equals(current_atk, "batonpass") && tmp_stat_holder != null){
                    BattleStateTracker.applyStats(switchedTo, tmp_stat_holder);
                    tmp_stat_holder = null;
                    current_atk = "";
                }
                break;

            case "start":
                switch (MainActionSplit[3])
                {
                    case "typechange":
                        BattleStateTracker.getPokemon(getPkm(object_args)).overrideType(object_args[1].toString());
                        break;
                    case "typeadd":
                        BattleStateTracker.getPokemon(getPkm(object_args)).addType(object_args[1].toString());
                        break;
                    default:
                        BattleStateTracker.getPokemon(getPkm(object_args)).addExtraEffect(MainActionSplit[3]);
                        break;
                }
                break;
            case "end":
                BattleStateTracker.getPokemon(getPkm(object_args)).removeExtraEffect(MainActionSplit[3]);
                break;

            case "fail":
                current_atk = "";
                break;

            case "setboost":
                BattleStateTracker.changeStats(getPkm(object_args),"attack","max",true);
                break;
            case "fieldstart":
                if(MainActionSplit[3].contains("terrain"))
                    TerrainBattleState.setTerrain(MainActionSplit[3]);
                else
                    TerrainBattleState.addRoom(MainActionSplit[3]);
                break;
            case "fieldend":
                if(MainActionSplit[3].contains("terrain"))
                    TerrainBattleState.setTerrain("");
                else
                    TerrainBattleState.removeRoom(MainActionSplit[3]);
                break;
            case "sidestart":
                if(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"))
                {
                    TerrainBattleState.addHazard(true, MainActionSplit[3]);
                }
                else {
                    TerrainBattleState.addHazard(false, MainActionSplit[3]);
                }
                break;
            case "sideend":
                if(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"))
                {
                    TerrainBattleState.removeHazard(true, MainActionSplit[3]);
                }
                else {
                    TerrainBattleState.removeHazard(false, MainActionSplit[3]);
                }
                break;
            case "weather":
                if(Objects.equals(MainActionSplit[4], "start"))
                {
                    TerrainBattleState.setWeather(MainActionSplit[3]);
                }
                else if(Objects.equals(MainActionSplit[4], "end"))
                {
                    TerrainBattleState.setWeather("");
                }
                break;
            case "turn":
                TerrainBattleState.updateRoom();
                TerrainBattleState.updateTerrain();
                TerrainBattleState.updateWeather();
                break;

            default:
                break;
        }
        if(Objects.equals(current_atk, "batonpass") && tmp_stat_holder == null)
        {
            tmp_stat_holder = BattleStateTracker.getStatsForHold(current_pkm);
        }

        return new Object[]{current_atk,current_pkm,tmp_stat_holder};
    }

    private static String getPkm(Object[] target){
        if(((TranslatableContents) ((MutableComponent) target[0]).getContents()).getKey().contains("species"))
        {
            return  ((TranslatableContents) ((MutableComponent) target[0]).getContents()).getKey();
        }
        else {
            var data = ((TranslatableContents) ((MutableComponent) target[0]).getContents()).getArgs();
            String pokemon;
            if(data[1] instanceof MutableComponent)
                pokemon = ((TranslatableContents) ((MutableComponent) data[1]).getContents()).getKey();
            else
                pokemon = data[1].toString();
            return  data[0] + "/" + pokemon;
        }
    }

    private static final int BG = 0x888D8D8D;
    private static final int BORDER = 0xFF2F2F2F;
    private static int drawStats(GuiGraphics ctx, Font font, String text, int x, int y, float scale, int color){
        int margin = 1;
        ctx.fill(x -margin, y -margin, x + (int)(font.width(text)* scale)+margin, y + (int)(font.lineHeight * scale)+margin, BG);
        ctx.renderOutline(x -margin*2, y -margin*2, (int)(font.width(text)* scale)+margin*4, (int)(font.lineHeight * scale)+margin*4, BORDER);

        ctx.pose().pushPose();
        ctx.pose().translate(x,y,0);
        ctx.pose().scale(scale,scale,0);
        ctx.drawString(font,text, 0, 0, color, true);
        ctx.pose().popPose();
        return x + (int)(font.width(text)* scale)+margin;
    }
}
