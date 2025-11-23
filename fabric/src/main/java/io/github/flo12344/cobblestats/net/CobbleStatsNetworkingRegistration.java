package io.github.flo12344.cobblestats.net;

import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.net.payload.FaintOrRevivePayload;
import io.github.flo12344.cobblestats.common.net.payload.PokemonCountInitializerPayload;
import io.github.flo12344.cobblestats.net.client.FaintOrReviveHandler;
import io.github.flo12344.cobblestats.net.client.PokemonCountInitializerHandler;
import io.github.flo12344.cobblestats.net.server.ServerJoinHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class CobbleStatsNetworkingRegistration {
    public static void register() {
        PayloadTypeRegistry.playC2S().register(JoinServerPayload.TYPE, JoinServerPayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(JoinServerPayload.TYPE, JoinServerPayload.STREAM_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(JoinServerPayload.TYPE, ServerJoinHandler::onPlayerLogin);

        PayloadTypeRegistry.playS2C().register(FaintOrRevivePayload.TYPE, FaintOrRevivePayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(FaintOrRevivePayload.TYPE, FaintOrRevivePayload.STREAM_CODEC);
        PayloadTypeRegistry.playS2C().register(PokemonCountInitializerPayload.TYPE, PokemonCountInitializerPayload.STREAM_CODEC);
        PayloadTypeRegistry.playC2S().register(PokemonCountInitializerPayload.TYPE, PokemonCountInitializerPayload.STREAM_CODEC);
    }

    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(PokemonCountInitializerPayload.TYPE, PokemonCountInitializerHandler::handlePokemonCountInitializer);
        ClientPlayNetworking.registerGlobalReceiver(FaintOrRevivePayload.TYPE, FaintOrReviveHandler::handle);
    }

}
