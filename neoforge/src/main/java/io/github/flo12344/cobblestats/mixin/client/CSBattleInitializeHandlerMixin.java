package io.github.flo12344.cobblestats.mixin.client;

import com.cobblemon.mod.common.api.net.NetworkPacket;
import com.cobblemon.mod.common.net.messages.client.battle.BattleInitializePacket;
import io.github.flo12344.cobblestats.common.client.BattleProcess;
import io.github.flo12344.cobblestats.common.client.BattleStateTracker;
import io.github.flo12344.cobblestats.common.client.TerrainBattleState;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.client.net.battle.BattleInitializeHandler")
public class CSBattleInitializeHandlerMixin {
    @Inject(method = "handle*", at = @At("TAIL"))
    void handle(NetworkPacket packet, Minecraft client, CallbackInfo ci) {
        if (packet instanceof BattleInitializePacket _packet) {
            TerrainBattleState.endOfBattle();
            BattleStateTracker.endOfBattle();
            BattleProcess.checkSide(_packet.side1);
            BattleProcess.checkSide(_packet.side2);
        }
    }
}
