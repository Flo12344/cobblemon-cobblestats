package io.github.flo12344.cobblestats.mixin.cobblemonraiddens;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import com.necro.raid.dens.common.raids.RaidInstance;
import com.necro.raid.dens.common.util.IRaidBattle;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PokemonBattle.class)
public abstract class RaidBroadcastMixin{

  @Inject(method = "broadcastChatMessage", at = @At("HEAD"), remap = false)
  void broadcastChatMessage(Component component, CallbackInfo ci){
    if(!((IRaidBattle) this).isRaidBattle())
      return;
    RaidInstance raid = ((IRaidBattle) this).getRaidBattle();
    raid.getPlayers().forEach(serverPlayer -> {
      var b = BattleRegistry.getBattleByParticipatingPlayer(serverPlayer);

      if(b == ((PokemonBattle) (Object) this) || b == null)
        return;
      b.getChatLog().add(component);
      b.sendSpectatorUpdate(new BattleMessagePacket(component));
      b.getActors().forEach(it -> it.sendMessage(component));
    });

  }
}
