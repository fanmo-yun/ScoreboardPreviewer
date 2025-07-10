package dev.scoreboardpreviewer.CustomWidget;

import io.github.thecsdev.tcdcommons.api.client.gui.TElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TCenterLabelWidget extends TElement {
    private final Text labelText;
    private final float scale;

    public TCenterLabelWidget(int x, int y, int width, int height, Text text, float scale) {
        super(x, y, width, height);
        this.labelText = text;
        this.scale = scale;
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
