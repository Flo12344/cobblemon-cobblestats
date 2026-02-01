package io.github.flo12344.cobblestats.mixin.cobblemonraiddens;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.battles.interpreter.instructions.ClearBoostInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidAccessor;
import com.necro.raid.dens.common.util.IRaidBattle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;


@Mixin(ClearBoostInstruction.class)
public class RaidClearBoostInstructionMixin {
    @Shadow(remap = false)
    BattleMessage message;

    @Inject(method = "invoke", at = @At("TAIL"), remap = false)
    void RaidBoostInstructionMixin(PokemonBattle battle, CallbackInfo ci) {
        if (!((IRaidBattle) battle).isRaidBattle()) return;
        RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();
        BattlePokemon battlePokemon = message.battlePokemon(0, battle);
        if (battlePokemon == null || battlePokemon.getEntity() == null) return;
        else if (!((IRaidAccessor) battlePokemon.getEntity()).isRaidBoss()) return;
        if (battlePokemon.actor.getSide() == battle.getSide1()) return;
        System.out.println(message.getRawMessage());

        var pokemonName = battlePokemon.getName();
        var lang = battleLang("clearboost", pokemonName);
        raid.getPlayers().forEach(serverPlayer -> {
            var b = BattleRegistry.getBattleByParticipatingPlayer(serverPlayer);
            if (b == battle) return;
            b.broadcastChatMessage(lang);
        });
    }
}
