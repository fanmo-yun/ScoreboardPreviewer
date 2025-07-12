package dev.scoreboardpreviewer.CustomWidget;

import io.github.thecsdev.tcdcommons.api.client.gui.TElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

public class TLabelWidget extends TElement {
    protected MutableText labelText;
    protected float scale = 1.0f;

    public TLabelWidget(int x, int y, int width, int height, MutableText text) {
        super(x, y, width, height);
        this.labelText = text;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setLabelText(MutableText text) {
        this.labelText = text;
    }

    public MutableText getLabelText() {
        return this.labelText;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int X = getTpeX() + 2;
        int Y = getTpeY() + (height - textRenderer.fontHeight) / 2;

        matrices.push();
        matrices.translate(X, Y, 0);
        matrices.scale(scale, scale, 1.0f);
        textRenderer.drawWithShadow(matrices, labelText, 0, 0, 0xFFFFFF);
        matrices.pop();
    }
}
