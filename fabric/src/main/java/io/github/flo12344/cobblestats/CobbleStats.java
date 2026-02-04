package io.github.flo12344.cobblestats;


import com.cobblemon.mod.common.mixin.invoker.BooleanRuleInvoker;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import io.github.flo12344.cobblestats.common.net.ServerData;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import io.github.flo12344.cobblestats.net.CobbleStatsNetworkingRegistration;
import io.github.flo12344.cobblestats.net.FabricNetworkManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class CobbleStats implements ModInitializer{
  public static GameRules.Key<GameRules.BooleanValue> ACCURATE_POKEBALL;

  @Override
  public void onInitialize(){
    CobblestatsClientConfig.load();
    PlatformNetwork.INSTANCE = new FabricNetworkManager();
    ServerLifecycleEvents.SERVER_STARTED.register(server -> {
      FabricNetworkManager.CURRENT_SERVER = server;
    });
    ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
      FabricNetworkManager.CURRENT_SERVER = null;
      ServerData.clear();
    });
    CobbleStatsNetworkingRegistration.register();

    ACCURATE_POKEBALL = GameRuleRegistry.register("accuratePokeball", GameRules.Category.MISC, BooleanRuleInvoker.cobblemon$create(true));
  }
}
