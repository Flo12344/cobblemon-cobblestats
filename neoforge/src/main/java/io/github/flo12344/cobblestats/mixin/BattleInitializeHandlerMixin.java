package io.github.flo12344.cobblestats.mixin;

import io.github.flo12344.cobblestats.common.BattleStateTracker;
import io.github.flo12344.cobblestats.common.TerrainBattleState;
import com.cobblemon.mod.common.api.battles.model.actor.ActorType;
import com.cobblemon.mod.common.net.messages.client.battle.BattleInitializePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.client.net.battle.BattleInitializeHandler")
public class BattleInitializeHandlerMixin {
    @Inject(method = "handle(Lcom/cobblemon/mod/common/net/messages/client/battle/BattleInitializePacket;Lnet/minecraft/client/Minecraft;)V", at = @At("TAIL"))
    void handle(BattleInitializePacket packet, Minecraft client, CallbackInfo ci){
        TerrainBattleState.endOfBattle();
        BattleStateTracker.endOfBattle();
        if(!packet.side2.component1().getFirst().getActivePokemon().isEmpty())
        {
            packet.side2.component1().getFirst().getActivePokemon().forEach(activeBattlePokemonDTO ->
            {
                if(packet.side2.component1().getFirst().getType() == ActorType.PLAYER)
                {
                    BattleStateTracker.addPokemon( packet.side2.component1().getFirst().getDisplayName()+ "/" + ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
                }
                else {
                    BattleStateTracker.addPokemon( ((TranslatableContents) activeBattlePokemonDTO.getDisplayName().getContents()).getKey());
                }
            });
        }
    }
}
