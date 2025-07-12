package dev.scoreboardpreviewer.CustomWidget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

public class TCenterLabelWidget extends TLabelWidget {
    public TCenterLabelWidget(int x, int y, int width, int height, MutableText text) {
        super(x, y, width, height, text);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        matrices.push();
        matrices.translate(getTpeX(), getTpeY(), 0);
        matrices.scale(scale, scale, 1.0f);
        textRenderer.drawWithShadow(matrices, labelText, 0, 0, 0xFFFFFF);
        matrices.pop();
    }
}
