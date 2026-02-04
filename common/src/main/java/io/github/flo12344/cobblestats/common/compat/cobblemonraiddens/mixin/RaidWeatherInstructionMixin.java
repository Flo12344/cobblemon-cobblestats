package io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.interpreter.instructions.WeatherInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidAccessor;
import com.necro.raid.dens.common.util.IRaidBattle;
import io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.CompatUtils;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;


@Mixin(WeatherInstruction.class)
public class RaidWeatherInstructionMixin{
  @Shadow(remap = false)
  BattleMessage message;

  @Inject(method = "invoke", at = @At("TAIL"), remap = false)
  void RaidBoostInstructionMixin(PokemonBattle battle, CallbackInfo ci){
    if(!((IRaidBattle) battle).isRaidBattle())
      return;
    RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();
    var weather = message.effectAt(0).getId();
    BattlePokemon battlePokemon = message.battlePokemonFromOptional(battle, "of");
    if(battlePokemon == null || battlePokemon.getEntity() == null)
      return;
    else if(!((IRaidAccessor) battlePokemon.getEntity()).isRaidBoss())
      return;

    MutableComponent lang;
    if(message.hasOptionalArgument("upkeep")){
      lang = battleLang("weather." + weather + ".upkeep");
    }else if(weather != "none"){
      lang = battleLang("weather." + weather + ".start");
    }else if(battle.getContextManager().get(BattleContext.Type.WEATHER).iterator().hasNext())
      lang = battleLang("weather." + battle.getContextManager().get(BattleContext.Type.WEATHER).iterator().next().getId() + ".start");
    else
      return;
    CompatUtils.broadCastToOthers(raid, battle, lang);

  }
}
