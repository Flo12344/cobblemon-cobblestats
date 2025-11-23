package io.github.flo12344.cobblestats.mixin.server;

import com.cobblemon.mod.common.api.battles.interpreter.BattleMessage;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.api.battles.model.actor.BattleActor;
import io.github.flo12344.cobblestats.common.net.ServerData;
import io.github.flo12344.cobblestats.common.net.payload.FaintOrRevivePayload;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(targets = "com.cobblemon.mod.common.battles.interpreter.instructions.HealInstruction")
public class CSHealInstructionMixin {
    @Shadow
    private BattleMessage privateMessage;
    @Shadow
    private BattleActor actor;

    @Inject(method = "invoke", at = @At("TAIL"))
    public void invoke(PokemonBattle battle, CallbackInfo ci) {
        if (privateMessage.getRawMessage().endsWith("revive")) {
            var uuid = UUID.fromString(privateMessage.argumentAt(0).split(":")[1].trim());

            final int[] id = {0};
            actor.getPokemonList().forEach(battlePokemon -> {
                if (battlePokemon.getUuid().equals(uuid)) {
                    id[0] = ServerData.startTeams.get(actor.getUuid()).indexOf(battlePokemon);
                }
            });
            List<UUID> players = new ArrayList<>();
            battle.getActors().forEach(serverPlayer -> {
                players.add(serverPlayer.getUuid());
            });
            MinecraftServer server = PlatformNetwork.INSTANCE.getCurrentServer();
            players.forEach(target -> {
                if (ServerData.playerWithMod.contains(target)) {
                    PlatformNetwork.INSTANCE.sendToClient(server.getPlayerList().getPlayer(target), new FaintOrRevivePayload(actor.getUuid().toString(), id[0], false));
                }
            });
        }
    }
}
