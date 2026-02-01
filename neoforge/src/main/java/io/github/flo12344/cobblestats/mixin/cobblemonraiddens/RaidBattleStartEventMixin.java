package io.github.flo12344.cobblestats.mixin.cobblemonraiddens;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.pokemon.stats.Stats;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.battles.interpreter.instructions.BoostInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidAccessor;
import com.necro.raid.dens.common.util.IRaidBattle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;


//wrong method try modifying the bettle message
@Mixin(BoostInstruction.class)
public class RaidBattleStartEventMixin {
    @Shadow(remap = false)
    BattleMessage message;
    @Shadow(remap = false)
    BattlePokemon pokemon;

    @Shadow
    @Final
    private boolean isBoost;

    @Inject(method = "postActionEffect", at = @At("TAIL"), remap = false)
    void RaidBattleStartEvent(PokemonBattle battle, CallbackInfo ci) {
        if (!((IRaidBattle) battle).isRaidBattle()) return;
        RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();
        BattlePokemon battlePokemon = this.pokemon;
        if (battlePokemon == null || battlePokemon.getEntity() == null) return;
        else if (!((IRaidAccessor) battlePokemon.getEntity()).isRaidBoss()) return;
        if (battlePokemon.actor.getSide() == battle.getSide1()) return;
        System.out.println(message.getRawMessage());

        var severity = Stats.Companion.getSeverity(Integer.parseInt(message.argumentAt(2)));
        var rootKey = (isBoost) ? "boost" : "unboost";

        var stat = Stats.Companion.getStat(message.argumentAt(1)).getDisplayName();
        var lang = message.hasOptionalArgument("zeffect") ? battleLang(rootKey + "." + severity + ".zeffect", pokemon.getName(), stat)
                : battleLang(rootKey + "." + severity, pokemon.getName(), stat);
        raid.getPlayers().forEach(serverPlayer -> {
            var b = BattleRegistry.getBattleByParticipatingPlayer(serverPlayer);

            if (b == battle) return;
            b.broadcastChatMessage(lang);
        });
    }
}
