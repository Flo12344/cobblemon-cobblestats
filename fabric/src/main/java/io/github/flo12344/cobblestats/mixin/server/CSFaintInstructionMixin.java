package io.github.flo12344.cobblestats.mixin.server;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import io.github.flo12344.cobblestats.common.net.ServerData;
import io.github.flo12344.cobblestats.common.net.payload.FaintOrRevivePayload;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(com.cobblemon.mod.common.battles.interpreter.instructions.FaintInstruction.class)
public abstract class CSFaintInstructionMixin {
    @Final
    @Shadow
    private BattlePokemon faintingPokemon;

    @Inject(method = "invoke*", at = @At("TAIL"), remap = false)
    public void invoke(PokemonBattle battle, CallbackInfo ci) {
        final int[] id = {0};
        faintingPokemon.getActor().getPokemonList().forEach(battlePokemon -> {
            id[0] = ServerData.startTeams.get(battlePokemon.getActor().getUuid()).indexOf(faintingPokemon);
        });
        List<UUID> players = new ArrayList<>();
        battle.getActors().forEach(serverPlayer -> {
            players.add(serverPlayer.getUuid());
        });
        MinecraftServer server = PlatformNetwork.INSTANCE.getCurrentServer();
        players.forEach(target -> {
            if (ServerData.playerWithMod.contains(target)) {
                PlatformNetwork.INSTANCE.sendToClient(server.getPlayerList().getPlayer(target), new FaintOrRevivePayload(faintingPokemon.getActor().getUuid().toString(), id[0], true));
            }
        });
    }


}
