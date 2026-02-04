package io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.interpreter.instructions.StartInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidAccessor;
import com.necro.raid.dens.common.util.IRaidBattle;
import io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.CompatUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;


@Mixin(StartInstruction.class)
public class RaidStartInstructionMixin{
  @Final
  @Shadow(remap = false)
  private BattleMessage message;

  @Inject(method = "invoke", at = @At("TAIL"), remap = false)
  void RaidStartInstruction(PokemonBattle battle, CallbackInfo ci){
    if(!((IRaidBattle) battle).isRaidBattle())
      return;
    RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();
    BattlePokemon battlePokemon = message.battlePokemon(0, battle);
    if(battlePokemon == null || battlePokemon.getEntity() == null)
      return;
    else if(!((IRaidAccessor) battlePokemon.getEntity()).isRaidBoss())
      return;

    var effectID = message.effectAt(1).getId();
    var optionalEffect = message.effect("from");
    var optionalPokemon = message.battlePokemonFromOptional(battle, "of");
    var optionalPokemonName = optionalPokemon.getName();
    var extraEffect = message.effectAt(2) == null ? message.effectAt(2).getTypelessData() : Component.literal("UNKOWN");

    MutableComponent lang;
    if(optionalEffect != null && optionalEffect.getId().equals("reflecttype")){
      lang = battleLang("start.reflecttype", battlePokemon.getName(), optionalPokemonName);
    }else{
      lang = switch(effectID){
        case "perish3" -> null;
        case "perish2", "perish1", "perish0",
             "stockpile1", "stockpile2", "stockpile3" ->
                battleLang("start.${effectID.dropLast(1)}", battlePokemon.getName(), Integer.parseInt(effectID.substring(effectID.length())));
        case "dynamax" -> battleLang("start.${message.effectAt(2)?.id ?: effectID}", battlePokemon.getName());
        case "curse" ->
                battleLang("start.curse", message.battlePokemonFromOptional(battle, "of").getName(), battlePokemon.getName());
        default -> battleLang("start.$effectID", battlePokemon.getName(), extraEffect);
      };
      if(lang == null)
        return;
    }
    CompatUtils.broadCastToOthers(raid, battle, lang);

  }
}
