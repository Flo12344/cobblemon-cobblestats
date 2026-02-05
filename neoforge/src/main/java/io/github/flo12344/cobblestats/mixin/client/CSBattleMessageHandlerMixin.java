package io.github.flo12344.cobblestats.mixin.client;

import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import io.github.flo12344.cobblestats.common.client.BattleProcess;
import io.github.flo12344.cobblestats.common.client.PokemonBattleState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.client.net.battle.BattleMessageHandler")
public class CSBattleMessageHandlerMixin{
  private static String current_atk = "";
  private String current_pkm = "";
  private PokemonBattleState tmp_stat_holder = null;

  @Inject(method = "handle*", at = @At("TAIL"))
  private void handle(BattleMessagePacket packet, Minecraft client, CallbackInfo ci){
    if(packet instanceof BattleMessagePacket _packet){

      _packet.getMessages().forEach(component -> {

        var ret = BattleProcess.processBattleData(((TranslatableContents) component.getContents()), current_atk, current_pkm, tmp_stat_holder);

        current_atk = ((String) ret[0]);
        current_pkm = ((String) ret[1]);
        tmp_stat_holder = ((PokemonBattleState) ret[2]);
      });
    }
  }
}
