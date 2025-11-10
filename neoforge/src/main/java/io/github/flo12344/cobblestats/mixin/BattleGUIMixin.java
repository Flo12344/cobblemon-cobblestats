package io.github.flo12344.cobblestats.mixin;

import io.github.flo12344.cobblestats.common.BattleProcess;
import io.github.flo12344.cobblestats.common.BattleStateTracker;
import io.github.flo12344.cobblestats.common.TerrainBattleState;
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.battle.ActiveClientBattlePokemon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.cobblemon.mod.common.client.gui.battle.BattleOverlay.*;

@Mixin(targets = "com.cobblemon.mod.common.client.gui.battle.BattleOverlay")
public class BattleGUIMixin {
    @Inject(method = "drawTile", at = @At("TAIL"))
    private void drawTile(GuiGraphics context, float tickDelta, ActiveClientBattlePokemon activeBattlePokemon, boolean left, int rank, PokedexEntryProgress dexState, boolean hasCommand, boolean isHovered, boolean isCompact, CallbackInfo ci)
    {
        BattleProcess.drawUI(context,activeBattlePokemon,left,rank,isHovered, isCompact);
    }



}
