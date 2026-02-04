package io.github.flo12344.cobblestats.common.compat.cobblemonraiddens;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.necro.raid.dens.common.raids.RaidInstance;
import net.minecraft.network.chat.MutableComponent;

public class CompatUtils{

  public static void broadCastToOthers(RaidInstance raid, PokemonBattle battle, MutableComponent lang){
    raid.getPlayers().forEach(serverPlayer -> {
      var b = BattleRegistry.getBattleByParticipatingPlayer(serverPlayer);

      if(b == battle || b == null)
        return;
      b.broadcastChatMessage(lang);
    });
  }
}
