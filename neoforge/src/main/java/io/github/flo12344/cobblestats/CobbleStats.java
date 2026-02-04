package io.github.flo12344.cobblestats;

import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.client.net.ClientData;
import io.github.flo12344.cobblestats.common.client.net.payload.JoinServerPayload;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import io.github.flo12344.cobblestats.net.NeoForgeNetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

@Mod(value = "cobblestats", dist = Dist.CLIENT)
public class CobbleStats{
  public CobbleStats(){
    CobblestatsClientConfig.load();
    NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, loggingIn -> {
      Minecraft minecraft = Minecraft.getInstance();
      ClientPacketListener netHandler = minecraft.getConnection();
      if(netHandler == null || !netHandler.getConnection().isConnected()){
        return;
      }
      if(netHandler.hasChannel(JoinServerPayload.TYPE)){
        var payload = new JoinServerPayload(minecraft.player.getUUID().toString());
        PacketDistributor.sendToServer(payload);
        ClientData.SERVER_COMPAT = true;
      }else{
        ClientData.SERVER_COMPAT = false;
      }
    });
    NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingOut.class, loggingOut -> {
      ClientData.SERVER_COMPAT = false;
      ClientData.clearData();
    });
    PlatformNetwork.INSTANCE = new NeoForgeNetworkManager();
  }


}
