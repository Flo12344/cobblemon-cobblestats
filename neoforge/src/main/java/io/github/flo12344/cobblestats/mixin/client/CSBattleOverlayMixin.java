package io.github.flo12344.cobblestats.mixin.client;

import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import io.github.flo12344.cobblestats.common.client.BattleProcess;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.cobblemon.mod.common.client.gui.battle.BattleOverlay")
public class CSBattleOverlayMixin {
    @Inject(method = "drawTile", at = @At("TAIL"))
    private void drawTile(GuiGraphics context, float tickDelta, ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, PokedexEntryProgress dexState, boolean hasCommand, boolean isHovered, boolean isCompact, CallbackInfo ci) {
        BattleProcess.drawUI(context, activeBattlePokemon, left, rank, isHovered, isCompact);
    }
}
