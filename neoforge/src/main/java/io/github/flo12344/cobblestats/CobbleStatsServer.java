package io.github.flo12344.cobblestats;

import com.cobblemon.mod.common.mixin.invoker.BooleanRuleInvoker;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import io.github.flo12344.cobblestats.net.NeoForgeNetworkManager;
import net.minecraft.world.level.GameRules;
import net.neoforged.fml.common.Mod;

@Mod(value = "cobblestats")
public class CobbleStatsServer {
    public CobbleStatsServer() {
        PlatformNetwork.INSTANCE = new NeoForgeNetworkManager();
    }

    public static final GameRules.Key<GameRules.BooleanValue> ACCURATE_POKEBALL = GameRules.register("accuratePokeball", GameRules.Category.MISC, BooleanRuleInvoker.cobblemon$create(true));
}
