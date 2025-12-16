package io.github.flo12344.cobblestats.mixin.server;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.interpreter.instructions.InitializeInstruction;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import io.github.flo12344.cobblestats.CobbleStats;
import io.github.flo12344.cobblestats.common.net.ServerData;
import io.github.flo12344.cobblestats.common.net.payload.PokemonCountInitializerPayload;
import io.github.flo12344.cobblestats.common.net.payload.utils.PokemonStateInit;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mixin(value = InitializeInstruction.class)
public class CSInitializeInstructionMixin {
    @Inject(method = "invoke*", at = @At("TAIL"), remap = false)
    void invoke(PokemonBattle battle, CallbackInfo ci) {
        HashMap<UUID, List<BattlePokemon>> players = new HashMap<UUID, List<BattlePokemon>>();
        battle.getActors().forEach(serverPlayer -> {
            players.put(serverPlayer.getUuid(), serverPlayer.getPokemonList());
            ServerData.startTeams.put(serverPlayer.getUuid(), serverPlayer.getPokemonList().stream().toList());
        });

        MinecraftServer server = PlatformNetwork.INSTANCE.getCurrentServer();

        players.forEach((uuid, battlePokemons) ->
        {
            if (ServerData.playerWithMod.contains(uuid)) {
                var p = server.getPlayerList().getPlayer(uuid);
                players.forEach((uuid1, battlePokemons1) ->
                {
                    List<PokemonStateInit> team = new ArrayList<>();
                    for (int i = 0; i < battlePokemons1.size(); i++) {
                        var mon = battlePokemons1.get(i);
                        if (mon != null) {
                            var _mon = mon.getOriginalPokemon();
                            var ball_to_send = "poke_ball";
                            if (server.getGameRules().getBoolean(CobbleStats.ACCURATE_POKEBALL))
                                ball_to_send = _mon.getCaughtBall().getModel2d().getPath();
                            team.add(
                                    new PokemonStateInit(
                                            ServerData.startTeams.get(uuid1).indexOf(mon),
                                            "",// _mon.getSpecies().toString(),
                                            "", //_mon.getForm().toString(),
                                            ball_to_send,
                                            mon.getHealth() <= 0));
                        }
                    }

                    PlatformNetwork.INSTANCE.sendToClient(p, new PokemonCountInitializerPayload(uuid1.toString(), team));
                });
            }
        });
    }

}
