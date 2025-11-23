package io.github.flo12344.cobblestats.net.client;

import io.github.flo12344.cobblestats.common.client.net.ClientData;
import io.github.flo12344.cobblestats.common.net.payload.PokemonCountInitializerPayload;
import io.github.flo12344.cobblestats.common.utils.PlayerTeamInfo;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

public class PokemonCountInitializerHandler {
    public static void handlePokemonCountInitializer(final PokemonCountInitializerPayload payload, final ClientPlayNetworking.Context ctx) {
        PlayerTeamInfo team = new PlayerTeamInfo();
        payload.team().forEach(
                pokemonStateInit -> {
                    team.addPokemonToTeam(pokemonStateInit.id(), new PlayerTeamInfo.TeamPokemon(pokemonStateInit.ball(), pokemonStateInit.species(), pokemonStateInit.form(), pokemonStateInit.is_ko()));
                }
        );

        ClientData.pokemonCounts.put(UUID.fromString(payload.uuid()), team);
    }
}
