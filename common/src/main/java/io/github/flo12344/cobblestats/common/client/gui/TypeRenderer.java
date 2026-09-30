package io.github.flo12344.cobblestats.common.client.gui;

import com.cobblemon.mod.common.client.battle.ClientBattlePokemon;
import com.cobblemon.mod.common.client.gui.TypeIcon;
import io.github.flo12344.cobblestats.common.client.CobblestatsClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class TypeRenderer {
    public static void draw(GuiGraphics context, boolean left, ClientBattlePokemon battlePokemon, float portraitDiameter, float original_Y) {
        var formName = battlePokemon.getProperties().getForm();
        var currentForm = battlePokemon.getSpecies().getForms().stream()
                .filter(form -> form.getName()
                        .replace("-", "")
                        .toLowerCase()
                        .equals(formName))
                .findFirst()
                .orElse(battlePokemon.getSpecies().getStandardForm());
        var primaryType = currentForm.getPrimaryType();
        var secondaryType = currentForm.getSecondaryType();
        var x = (left ? portraitDiameter / 2 : Minecraft.getInstance().getWindow().getGuiScaledWidth() - portraitDiameter / 2);
        var matrix = context.pose();
        matrix.pushPose();
        matrix.translate(x, original_Y, 1000);
        float type_size = CobblestatsClientConfig.TypeSize;
        matrix.scale(type_size, type_size, type_size);
        TypeIcon icon = new TypeIcon(0, 0,
                primaryType,
                secondaryType,
                true, false,
                18, 4, 1.0f);
        icon.render(context);
        matrix.popPose();
    }
}
