package io.github.flo12344.cobblestats.mixin.server;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleSide;
import io.github.flo12344.cobblestats.common.net.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonBattle.class)
public class CSBattleEndMixin {
    @Shadow
    BattleSide side1;
    @Shadow
    BattleSide side2;

    @Inject(method = "end", at = @At("HEAD"), remap = false)
    void end(CallbackInfo ci) {
        for (int i = 0; i < side1.getActors().length; i++) {
            ServerData.startTeams.remove(side1.getActors()[i].getUuid());
        }
        for (int i = 0; i < side2.getActors().length; i++) {
            ServerData.startTeams.remove(side2.getActors()[i].getUuid());
        }
    }
}
