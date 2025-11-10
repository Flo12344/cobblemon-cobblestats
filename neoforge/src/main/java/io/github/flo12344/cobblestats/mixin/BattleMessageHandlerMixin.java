package io.github.flo12344.cobblestats.mixin;

import io.github.flo12344.cobblestats.common.BattleProcess;
import io.github.flo12344.cobblestats.common.BattleStateTracker;
import io.github.flo12344.cobblestats.common.PokemonBattleState;
import io.github.flo12344.cobblestats.common.TerrainBattleState;
import com.cobblemon.mod.common.net.messages.client.battle.BattleMessagePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(targets = "com.cobblemon.mod.common.client.net.battle.BattleMessageHandler")
public class BattleMessageHandlerMixin {
    private static String current_atk = "";
    private String current_pkm = "";
    private PokemonBattleState tmp_stat_holder = null;

    @Inject(method = "handle(Lcom/cobblemon/mod/common/net/messages/client/battle/BattleMessagePacket;Lnet/minecraft/client/Minecraft;)V" , at = @At("TAIL"))
    private void handle(BattleMessagePacket packet, Minecraft client, CallbackInfo ci){
       var ret = BattleProcess.processBattleData(packet, current_atk,current_pkm,tmp_stat_holder);

        current_atk = ((String) ret[0]);
        current_pkm = ((String) ret[1]);
        tmp_stat_holder = ((PokemonBattleState) ret[2]);
    }
}
