package io.github.flo12344.cobblestats.net;

import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.net.payload.FaintOrRevivePayload;
import io.github.flo12344.cobblestats.common.net.payload.PokemonCountInitializerPayload;
import io.github.flo12344.cobblestats.net.client.FaintOrReviveHandler;
import io.github.flo12344.cobblestats.net.client.PokemonCountInitializerHandler;
import io.github.flo12344.cobblestats.net.server.ServerJoinHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "cobblestats", bus = EventBusSubscriber.Bus.MOD)
public class CobbleStatsNetworkingRegistration {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0").optional();
        //server
        registrar.playToServer(JoinServerPayload.TYPE, JoinServerPayload.STREAM_CODEC, ServerJoinHandler::onPlayerLogin);
        
        //client
        registrar.playToClient(PokemonCountInitializerPayload.TYPE, PokemonCountInitializerPayload.STREAM_CODEC, PokemonCountInitializerHandler::handlePokemonCountInitializer);
        registrar.playToClient(FaintOrRevivePayload.TYPE, FaintOrRevivePayload.STREAM_CODEC, FaintOrReviveHandler::handle);
    }
}
