package io.github.flo12344.cobblestats.net.client;

import io.github.flo12344.cobblestats.common.client.net.ClientData;
import io.github.flo12344.cobblestats.common.net.payload.PokemonCountInitializerPayload;
import io.github.flo12344.cobblestats.common.utils.PlayerTeamInfo;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public class PokemonCountInitializerHandler {
    public static void handlePokemonCountInitializer(final PokemonCountInitializerPayload payload, final IPayloadContext ctx) {
        ctx.enqueueWork(
                () ->
                {
                    PlayerTeamInfo team = new PlayerTeamInfo();
                    payload.team().forEach(
                            pokemonStateInit -> {
                                team.addPokemonToTeam(pokemonStateInit.id(), new PlayerTeamInfo.TeamPokemon(pokemonStateInit.ball(), pokemonStateInit.species(), pokemonStateInit.form(), pokemonStateInit.is_ko()));
//                                System.out.println(pokemonStateInit.species() + " : " + pokemonStateInit.ball());
                            }
                    );

                    ClientData.pokemonCounts.put(UUID.fromString(payload.uuid()), team);
                }
        );
    }
}
