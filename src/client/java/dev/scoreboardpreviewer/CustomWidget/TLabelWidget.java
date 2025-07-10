package dev.scoreboardpreviewer.CustomWidget;

import io.github.thecsdev.tcdcommons.api.client.gui.TElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TLabelWidget extends TElement {
    private final Text labelText;
    protected float scale = 1.0f;

    public TLabelWidget(int x, int y, int width, int height, Text text) {
        super(x, y, width, height);
        this.labelText = text;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        matrices.push();
        int x = getTpeX() + 2;
        int y = getTpeY() + (height - textRenderer.fontHeight) / 2;
        matrices.translate(x, y, 0);
        matrices.scale(scale, scale, 1.0f);
        textRenderer.drawWithShadow(matrices, labelText, 0, 0, 0xFFFFFF);
        matrices.pop();
    }
}
