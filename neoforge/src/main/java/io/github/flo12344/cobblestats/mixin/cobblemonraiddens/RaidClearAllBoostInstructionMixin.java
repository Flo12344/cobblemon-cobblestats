package io.github.flo12344.cobblestats.mixin.cobblemonraiddens;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.battles.interpreter.instructions.ClearAllBoostInstruction;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidBattle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.util.LocalizationUtilsKt.battleLang;

@Mixin(ClearAllBoostInstruction.class)
public class RaidClearAllBoostInstructionMixin {

    @Inject(method = "invoke", at = @At("TAIL"), remap = false)
    void RaidClearAllBoostInstruction(PokemonBattle battle, CallbackInfo ci) {
        if (!((IRaidBattle) battle).isRaidBattle()) return;
        RaidInstance raid = ((IRaidBattle) battle).getRaidBattle();

        raid.getPlayers().forEach(serverPlayer -> {
            var b = BattleRegistry.getBattleByParticipatingPlayer(serverPlayer);
            if (b == battle) return;
            b.broadcastChatMessage(battleLang("clearallboost"));
        });
    }
}

