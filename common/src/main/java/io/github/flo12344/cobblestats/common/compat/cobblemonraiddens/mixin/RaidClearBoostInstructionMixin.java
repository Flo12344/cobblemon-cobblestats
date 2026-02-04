package io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.interpreter.instructions.ClearBoostInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidAccessor;
import com.necro.raid.dens.common.util.IRaidBattle;
import io.github.flo12344.cobblestats.common.compat.cobblemonraiddens.CompatUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;


@Mixin(ClearBoostInstruction.class)
public class RaidClearBoostInstructionMixin{
  @Final
  @Shadow(remap = false)
  private BattleMessage message;

  @Inject(method = "invoke", at = @At("TAIL"), remap = false)
  void RaidBoostInstructionMixin(PokemonBattle battle, CallbackInfo ci){
    if(!((IRaidBattle) battle).isRaidBattle())
      return;
    RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();
    BattlePokemon battlePokemon = message.battlePokemon(0, battle);
    if(battlePokemon == null || battlePokemon.getEntity() == null)
      return;
    else if(!((IRaidAccessor) battlePokemon.getEntity()).isRaidBoss())
      return;

    var pokemonName = battlePokemon.getName();
    var lang = battleLang("clearboost", pokemonName);
    CompatUtils.broadCastToOthers(raid, battle, lang);

  }
}
