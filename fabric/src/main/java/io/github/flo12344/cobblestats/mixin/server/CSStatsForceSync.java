package io.github.flo12344.cobblestats.mixin.server;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.pokemon.BattlePokemon;
import io.github.flo12344.cobblestats.common.net.ServerData;
import io.github.flo12344.cobblestats.common.net.payload.ServerStatsSyncForcePayload;
import io.github.flo12344.cobblestats.common.utils.PlatformNetwork;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.battles.interpreter.instructions.BoostInstruction")
public class CSStatsForceSync {

    @Shadow
    @Final
    private BattlePokemon pokemon;

    @Shadow
    @Final
    private String statKey;

    @Shadow
    @Final
    private int stages;

    @Shadow
    @Final
    private boolean isBoost;

    @Inject(method = "postActionEffect", at = @At("TAIL"))
    void aVoid(PokemonBattle battle, CallbackInfo ci) {
        MinecraftServer server = PlatformNetwork.INSTANCE.getCurrentServer();
        for (var actor : battle.getActors()) {
            if (!ServerData.playerWithMod.contains(actor.getUuid())) {
                return;
            }
            var p = server.getPlayerList().getPlayer(actor.getUuid());
            var target_pokemon = pokemon.getName();
            String target = "";
            if (target_pokemon.getContents() instanceof TranslatableContents content) {
                if (content.getKey().contains("species")) {
                    target = content.getKey();
                } else if (content.getKey().contains("owned")) {
                    var data = content.getArgs();
                    String pokemon;
                    if (data[1] instanceof MutableComponent) {
                        if (((MutableComponent) data[1]).getContents() instanceof TranslatableContents) {
                            pokemon = ((TranslatableContents) ((MutableComponent) data[1]).getContents()).getKey();
                        } else {
                            pokemon = ((PlainTextContents.LiteralContents) ((MutableComponent) data[1]).getContents()).text();
                        }
                    } else {
                        pokemon = data[1].toString();
                    }
                    String owner;
                    if (data[0] instanceof MutableComponent) {
                        if (((MutableComponent) data[0]).getContents() instanceof TranslatableContents) {
                            owner = ((TranslatableContents) ((MutableComponent) data[0]).getContents()).getKey();
                        } else {
                            owner = ((PlainTextContents.LiteralContents) ((MutableComponent) data[0]).getContents()).text();
                        }
                    } else {
                        owner = data[0].toString();
                    }
                    target = (owner + "/" + pokemon);
                }
            }
            if (target.isEmpty()) return;
            PlatformNetwork.INSTANCE.sendToClient(p, new ServerStatsSyncForcePayload(
                    target,
                    statKey,
                    isBoost ? stages : -stages));
        }
    }
}
