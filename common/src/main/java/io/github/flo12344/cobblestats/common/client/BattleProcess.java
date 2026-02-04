package io.github.flo12344.cobblestats.common.client;

import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.api.gui.GuiUtilsKt;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.TypeIcon;
import com.cobblemon.mod.common.net.messages.client.battle.BattleInitializePacket;
import io.github.flo12344.cobblestats.common.client.net.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

public class BattleProcess{


  @SuppressWarnings("unused")
  public static void drawUI(GuiGraphics context, ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, boolean isHovered, boolean isCompact){
    var mc = Minecraft.getInstance();

    var battlePokemon = activeBattlePokemon.getBattlePokemon();
    if(battlePokemon == null){
      return;
    }
    var battle = CobblemonClient.INSTANCE.getBattle();
    if(battle == null){
      return;
    }
    int playerNumberOffset = (Character.getNumericValue(activeBattlePokemon.getActorShowdownId().charAt(1)) - 1) / 2 * 10;

    float original_Y = VERTICAL_INSET + rank * (isCompact ? COMPACT_VERTICAL_SPACING : VERTICAL_SPACING) + (left ? playerNumberOffset : (battle.getBattleFormat().getBattleType().getActorsPerSide() - 1) * 10 - playerNumberOffset);

    float original_X = activeBattlePokemon.getXDisplacement();

    var portraitOffsetY = isCompact ? COMPACT_PORTRAIT_OFFSET_Y : PORTRAIT_OFFSET_Y;
    var portraitDiameter = isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER;
    var infoOffsetX = isCompact ? COMPACT_INFO_OFFSET_X : INFO_OFFSET_X;


    original_X += left ? infoOffsetX + portraitDiameter : 0;
    original_Y += portraitOffsetY + portraitDiameter * .75F;

    if(CobblestatsClientConfig.ShowPokemonType){
      var formName = battlePokemon.getProperties().getForm();
      var currentForm = battlePokemon.getSpecies().getForms().stream()
              .filter(form -> form.getName()
                      .replace("-", "")
                      .toLowerCase()
                      .equals(formName))
              .findFirst()
              .orElse(battlePokemon.getSpecies().getStandardForm());
      var primaryType = currentForm.getPrimaryType();
      var secondaryType = currentForm.getSecondaryType();
      var x = (left ? (float) portraitDiameter / 2 : mc.getWindow().getGuiScaledWidth() - (float) portraitDiameter / 2);
      var matrix = context.pose();
      matrix.pushPose();
      matrix.translate(x, original_Y, 1000);
      float ball_size = 0.45f;
      matrix.scale(ball_size, ball_size, ball_size);
      TypeIcon icon = new TypeIcon(0, 0,
              primaryType,
              secondaryType,
              true, false,
              18, 4, 1.0f);
      icon.render(context);
      matrix.popPose();
    }

    String key = getKey(activeBattlePokemon);

    original_X += 5;
    var stats = BattleStateTracker.getChangedStats(key);
    var maxX = isCompact ? (COMPACT_TILE_WIDTH - COMPACT_PORTRAIT_DIAMETER - COMPACT_PORTRAIT_OFFSET_Y * 2) : (TILE_WIDTH - PORTRAIT_DIAMETER - PORTRAIT_OFFSET_Y * 2) - 1F;
    final float[] x = {original_X};
    final float[] finalOriginal_X = {original_X};
    assert activeBattlePokemon.getBattlePokemon() != null;
    if(activeBattlePokemon.getBattlePokemon().getStatus() != null){
      if(left){
        x[0] += 35;
      }else{
        finalOriginal_X[0] -= 35;
      }
    }
    final float scale = 0.5f;
    final float[] y = {original_Y};
    stats.forEach(s -> {
      if(x[0] + ((int) (mc.font.width(s) * scale)) > finalOriginal_X[0] + maxX){
        x[0] = finalOriginal_X[0];
        y[0] += mc.font.lineHeight * scale + 3;
      }
      x[0] = drawStats(context, mc.font, s, (int) x[0], (int) y[0]) + 3;
    });


    if(left){
      final int[] y_pos = {VERTICAL_INSET + 30};
      if(!((String) TerrainBattleState.getTerrainState()[0]).isEmpty() && CobblestatsClientConfig.ShowTerrain){
        var terrain = TerrainBattleState.getTerrainState();
        String text = ((String) terrain[0]);
        if(((Integer) terrain[1]) < 0){
          text += " " + (((Integer) terrain[1]) + 3);
        }else{
          text += " " + terrain[1] + " or " + (((Integer) terrain[1]) + 3);

        }
        int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));

        drawStats(context, mc.font, text, x_pos, y_pos[0]);
        y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
      }

      TerrainBattleState.getRoomState().forEach((s, integer) -> {
        String text = s + " " + integer;
        int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));
        drawStats(context, mc.font, text, x_pos, y_pos[0]);
        y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
      });


      if(!((String) TerrainBattleState.getWeatherState()[0]).isEmpty() && CobblestatsClientConfig.ShowWeather){
        var weather = TerrainBattleState.getWeatherState();
        String text = ((String) weather[0]);
        if(((Integer) weather[1]) >= -9){
          if(((Integer) weather[1]) < 0){
            text += " " + (((Integer) weather[1]) + 3);
          }else{
            text += " " + weather[1] + " or " + (((Integer) weather[1]) + 3);
          }
        }
        int x_pos = mc.getWindow().getGuiScaledWidth() / 2 - ((int) ((float) mc.font.width(text) / 2 * scale));
        drawStats(context, mc.font, text, x_pos, y_pos[0]);
        y_pos[0] += (int) (mc.font.lineHeight * scale) + 3;
      }
    }
    if(CobblestatsClientConfig.ShowHazards)
      renderHazard(context, left, isCompact, mc);
    if(!ClientData.SERVER_COMPAT)
      return;
    if(!left && activeBattlePokemon.getActor().getType() == ActorType.WILD && !CobblestatsClientConfig.ForceHidePokeball)
      return;
    var pokeballX = HORIZONTAL_INSET;
    if(!left){
      pokeballX = mc.getWindow().getGuiScaledWidth() - pokeballX - (isCompact ? COMPACT_TILE_WIDTH : TILE_WIDTH);
    }
    x[0] = pokeballX + battle.getBattleFormat().getBattleType().getSlotsPerActor() + (left ? infoOffsetX + portraitDiameter : 0);
    final int _y_pos = 4;
    var matrix = context.pose();
    matrix.pushPose();
    matrix.translate(x[0], 4, 0);
    float ball_size = 0.45f;
    matrix.scale(ball_size, ball_size, ball_size);
    ClientData.pokemonCounts.forEach((uuid, integer) -> {
      String pokeball = "poke_ball";
      if(activeBattlePokemon.getActor().getUuid().equals(uuid)){
        int pos = left ? (int) (isCompact ? COMPACT_TILE_WIDTH - ball_size * ((18 + 1) * 6) : TILE_WIDTH - ball_size * ((18 + 1) * 6)) : 0;
        var team = integer.getTeamPokemons();
        Number color_shift;
        for(int i = 0; i < 6; i++){
          int margin = 1;
          if(i < team.size() && team.get(i) != null && CobblestatsClientConfig.AccuratePokeballIfAvailable){
            pokeball = team.get(i).ball;
            if(!team.get(i).is_ko)
              color_shift = 1;
            else{
              color_shift = 0.5;
            }
          }else{
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

  private static @NotNull String getKey(ActiveClientBattlePokemon activeBattlePokemon){
    String key;
    assert activeBattlePokemon.getBattlePokemon() != null;
    var value = activeBattlePokemon.getBattlePokemon().getDisplayName().getContents();
    if(value instanceof TranslatableContents){
      key = ((TranslatableContents) value).getKey();
    }else{
      key = ((PlainTextContents.LiteralContents) value).text();
    }

    if(activeBattlePokemon.getBattlePokemon().getActor().getDisplayName().getContents() instanceof PlainTextContents.LiteralContents(
            String text1
    )){
      key = text1 + "/" + key;
    }else if(activeBattlePokemon.getBattlePokemon().getActor().getDisplayName().getContents() instanceof TranslatableContents translatableContents){
      if(!translatableContents.getKey().contains(".") || translatableContents.getKey().contains("trainer"))
        key = translatableContents.getKey() + "/" + key;
    }
    return key;
  }

  private static void renderHazard(GuiGraphics context, boolean left, boolean isCompact, Minecraft mc){
    final int[] _y = {(VERTICAL_INSET + ((isCompact ? COMPACT_PORTRAIT_DIAMETER : PORTRAIT_DIAMETER) * 4))};
    TerrainBattleState.getHazardStates(left).forEach((s, integer) -> {
      String text = s;
      if(integer > 1)
        text += " " + integer;
      int _x = VERTICAL_INSET;
      if(!left){
        _x = mc.getWindow().getGuiScaledWidth() - _x - ((int) (mc.font.width(text) * (float) 0.5));
      }
      drawStats(context, mc.font, text, _x, _y[0]);
      _y[0] += (int) (mc.font.lineHeight * (float) 0.5) + 3;
    });
  }

  public static Object[] processBattleData(TranslatableContents messagePacket, String current_atk, String current_pkm, PokemonBattleState tmp_stat_holder){
    String[] MainActionSplit = messagePacket.getKey().split("\\.");

    if(MainActionSplit.length < 3)
      return new Object[]{current_atk, current_pkm, tmp_stat_holder};
    if(Objects.equals(MainActionSplit[1], "status")){
      if(Objects.equals(MainActionSplit[2], "sleep") && Objects.equals(MainActionSplit[3], "cure")){
        BattleStateTracker.getPokemon(current_pkm).removeExtraEffect("yawn");
      }
    }

    if(!Objects.equals(MainActionSplit[1], "battle"))
      return new Object[]{current_atk, current_pkm, tmp_stat_holder};

    Object[] object_args = messagePacket.getArgs();
    switch(MainActionSplit[2]){
      case "unboost":
      case "boost":
        if(MainActionSplit.length > 3 && Objects.equals(MainActionSplit[3], "cap"))
          return new Object[]{current_atk, current_pkm, tmp_stat_holder};

        BattleStateTracker.changeStats(
                getPkm(object_args),
                ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2],
                MainActionSplit[3],
                Objects.equals(MainActionSplit[2], "boost"));
        break;
      case "clearallboost":
        BattleStateTracker.clearAllBoosts(current_pkm, false);
        break;
      case "clearallnegativeboost":
        BattleStateTracker.clearAllBoosts(current_pkm, true);
        break;

      case "used_move_on":
      case "used_move":
        current_pkm = getPkm(object_args);
        current_atk = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey().split("\\.")[2];
        break;

      case "switch":
        String switchedTo;
        if(MainActionSplit[3].equals("self")){
          String pokemon;
          if(object_args[0] instanceof MutableComponent){
            pokemon = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
          }else{
            pokemon = (String) object_args[0];
          }
          assert Minecraft.getInstance().player != null;
          String owner = Minecraft.getInstance().player.getDisplayName().getString();
          switchedTo = owner + "/" + pokemon;
        }else{
          String pokemon;
          if(object_args[1] instanceof MutableComponent){
            pokemon = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey();
          }else{
            pokemon = (String) object_args[1];
          }
          String owner;
          if(object_args[0] instanceof MutableComponent){
            owner = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
          }else{
            owner = object_args[0].toString();
          }
          switchedTo = owner + "/" + pokemon;
        }
        BattleStateTracker.addPokemon(switchedTo);

        if(Objects.equals(current_atk, "batonpass") && tmp_stat_holder != null){
          BattleStateTracker.applyStats(switchedTo, tmp_stat_holder);
          tmp_stat_holder = null;
          current_atk = "";
        }
        break;
      case "withdraw":
        String toRemove;
        if(MainActionSplit[3].equals("self")){
          String pokemon;
          if(object_args[0] instanceof MutableComponent){
            pokemon = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
          }else{
            pokemon = (String) object_args[0];
          }
          assert Minecraft.getInstance().player != null;
          String owner = Minecraft.getInstance().player.getDisplayName().getString();
          toRemove = owner + "/" + pokemon;
        }else{
          String pokemon;
          if(object_args[1] instanceof MutableComponent){
            pokemon = ((TranslatableContents) ((MutableComponent) object_args[1]).getContents()).getKey();
          }else{
            pokemon = (String) object_args[1];
          }
          String owner;
          if(object_args[0] instanceof MutableComponent){
            owner = ((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey();
          }else{
            owner = object_args[0].toString();
          }
          toRemove = owner + "/" + pokemon;
        }
        BattleStateTracker.removePokemon(toRemove);
        break;

      case "start":
        switch(MainActionSplit[3]){
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
        BattleStateTracker.changeStats(getPkm(object_args), "attack", "max", true);
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
        if(MainActionSplit.length == 5){
          TerrainBattleState.addHazard(!MainActionSplit[3].equals("opponent"), MainActionSplit[4]);
        }else{
          TerrainBattleState.addHazard(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"), MainActionSplit[3]);
        }
        break;
      case "sideend":
        if(MainActionSplit.length == 5){
          TerrainBattleState.removeHazard(!MainActionSplit[3].equals("opponent"), MainActionSplit[4]);
        }else{
          TerrainBattleState.removeHazard(((TranslatableContents) ((MutableComponent) object_args[0]).getContents()).getKey().contains("ally"), MainActionSplit[3]);
        }
        break;
      case "weather":
        if(Objects.equals(MainActionSplit[4], "start")){
          TerrainBattleState.setWeather(MainActionSplit[3]);
        }else if(Objects.equals(MainActionSplit[4], "end")){
          TerrainBattleState.setWeather("");
        }
        break;
      case "turn":
        TerrainBattleState.updateRoom();
        TerrainBattleState.updateTerrain();
        TerrainBattleState.updateWeather();
        break;
      case "fainted":
        BattleStateTracker.removePokemon(getPkm(object_args));
        break;
      case "ability":
        if(Objects.equals(MainActionSplit[3], "airlock")){
          TerrainBattleState.setWeather("");
        }
        break;

      default:
//                System.out.println(messagePacket.toString());
        break;
    }
    if(Objects.equals(current_atk, "batonpass") && tmp_stat_holder == null){
      tmp_stat_holder = BattleStateTracker.getStatsForHold(current_pkm);
    }

    return new Object[]{current_atk, current_pkm, tmp_stat_holder};
  }

  private static String getPkm(Object[] target){
    if(((TranslatableContents) ((MutableComponent) target[0]).getContents()).getKey().contains("species")){
      return ((TranslatableContents) ((MutableComponent) target[0]).getContents()).getKey();
    }else{
      var data = ((TranslatableContents) ((MutableComponent) target[0]).getContents()).getArgs();
      String pokemon;
      if(data[1] instanceof MutableComponent)
        pokemon = ((TranslatableContents) ((MutableComponent) data[1]).getContents()).getKey();
      else
        pokemon = data[1].toString();
      String owner;
      if(data[0] instanceof MutableComponent)
        owner = ((TranslatableContents) ((MutableComponent) data[0]).getContents()).getKey();
      else
        owner = data[0].toString();
      return owner + "/" + pokemon;
    }
  }

  private static final int BG = 0x888D8D8D;
  private static final int BORDER = 0xFF2F2F2F;

  private static int drawStats(GuiGraphics ctx, Font font, String text, int x, int y){
    int margin = 1;
    ctx.fill(x - margin, y - margin, x + (int) (font.width(text) * (float) 0.5) + margin, y + (int) (font.lineHeight * (float) 0.5) + margin, BG);
    ctx.renderOutline(x - margin * 2, y - margin * 2, (int) (font.width(text) * (float) 0.5) + margin * 4, (int) (font.lineHeight * (float) 0.5) + margin * 4, BORDER);

    ctx.pose().pushPose();
    ctx.pose().translate(x, y, 0);
    ctx.pose().scale((float) 0.5, (float) 0.5, 0);
    ctx.drawString(font, text, 0, 0, 16777215, true);
    ctx.pose().popPose();
    return x + (int) (font.width(text) * (float) 0.5) + margin;
  }

  public static void checkSide(BattleInitializePacket.BattleSideDTO side){
    if(!side.component1().getFirst().getActivePokemon().isEmpty()){
      side.component1().getFirst().getActivePokemon().forEach(activeBattlePokemonDTO ->
      {
        if(activeBattlePokemonDTO == null)
          return;
        if(side.component1().getFirst().getType() == ActorType.PLAYER){
          BattleStateTracker.addPokemon(side.component1().getFirst().getDisplayName().getString() + "/" + ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
        }else if(side.component1().getFirst().getType() == ActorType.NPC){
          BattleStateTracker.addPokemon(side.component1().getFirst().getDisplayName() + "/" + ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
        }else{
          BattleStateTracker.addPokemon(((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
        }
      });
    }
  }
}
