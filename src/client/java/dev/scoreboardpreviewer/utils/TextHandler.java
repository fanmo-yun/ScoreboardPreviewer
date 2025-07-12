package dev.scoreboardpreviewer.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public class TextHandler {
    public static float SCALE_1o5f = 1.5f;
    public static float SCALE_1f = 1.0f;
    private static final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

    public static int getTextWidth(@NotNull Text text, float scale) {
        return (int)(textRenderer.getWidth(text.getString()) * scale);
    }

    public static int getTextHeight(float scale) {
        return (int)(textRenderer.fontHeight * scale);
    }
}
