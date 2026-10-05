package io.github.flo12344.cobblestats;

import io.github.flo12344.cobblestats.common.client.net.ClientData;
import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import io.github.flo12344.cobblestats.net.CobbleStatsNetworkingRegistration;
import io.github.flo12344.cobblestats.net.FabricNetworkManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;

public class CobbleStatsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CobbleStatsNetworkingRegistration.registerClient();

        FabricLoader.getInstance().getModContainer("cobblemonxpbar").ifPresent(modContainer -> {
            ClientData.XPBAR = true;
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPacketListener clientPacketListener = minecraft.getConnection();
            if (clientPacketListener == null || !clientPacketListener.getConnection().isConnected()) {
                return;
            }

            if (ClientPlayNetworking.canSend(JoinServerPayload.TYPE)) {
                var payload = new JoinServerPayload(client.player.getUUID().toString());
                PlatformNetwork.INSTANCE.sendToServer(payload);
                ClientData.SERVER_COMPAT = true;
            } else {
                ClientData.SERVER_COMPAT = false;
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientData.SERVER_COMPAT = false;
            ClientData.clearData();
        });

        PlatformNetwork.INSTANCE = new FabricNetworkManager();
    }
}
