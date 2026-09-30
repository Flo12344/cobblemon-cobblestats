package io.github.flo12344.cobblestats.common.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class RenderUtils {
    private static final int BG = 0x888D8D8D;
    private static final int BORDER = 0xFF2F2F2F;

    public static int drawRectText(GuiGraphics ctx, Font font, String text, int x, int y, float size) {
        int margin = 1;
        ctx.fill(x - margin, y - margin, x + (int) (font.width(text) * size) + margin, y + (int) (font.lineHeight * size) + margin, BG);
        ctx.renderOutline(x - margin * 2, y - margin * 2, (int) (font.width(text) * size) + margin * 4, (int) (font.lineHeight * size) + margin * 4, BORDER);

        ctx.pose().pushPose();
        ctx.pose().translate(x, y + 1, 0);
        ctx.pose().scale(size, size, 0);
        ctx.drawString(font, text, 0, 0, 16777215, true);
        ctx.pose().popPose();
        return x + (int) (font.width(text) * size) + margin;
    }
}
