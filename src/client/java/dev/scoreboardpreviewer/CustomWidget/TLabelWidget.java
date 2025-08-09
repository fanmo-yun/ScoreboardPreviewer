package dev.scoreboardpreviewer.CustomWidget;

import io.github.thecsdev.tcdcommons.api.client.gui.TElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;

public class TLabelWidget extends TElement {
    protected MutableText labelText;
    protected float scale = 1.0f;
    protected boolean center = false;
    private Runnable onClickCallback = null;

    public TLabelWidget(int x, int y, int width, int height, MutableText text) {
        super(x, y, width, height);
        this.labelText = text;
    }

    public void setLabelText(MutableText text) {
        this.labelText = text;
    }

    public TLabelWidget setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public MutableText getLabelText() {
        return this.labelText;
    }

    public TLabelWidget setCentered(boolean center) {
        this.center = center;
        return this;
    }

    public void setOnClick(Runnable callback) {
        this.onClickCallback = callback;
    }

    private void onClick() {
        if (onClickCallback != null) {
            onClickCallback.run();
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int drawX, drawY;
        if (center) {
            int textWidth = (int) (textRenderer.getWidth(this.labelText.getString()) * scale);
            int textHeight = (int) (textRenderer.fontHeight * scale);
            drawX = getTpeX() + (getTpeWidth() - textWidth) / 2;
            drawY = getTpeY() + (getTpeHeight() - textHeight) / 2;
        } else {
            drawX = getTpeX() + 2;
            drawY = getTpeY() + (height - textRenderer.fontHeight) / 2;
        }

        matrices.push();
        matrices.translate(drawX, drawY, 0);
        matrices.scale(scale, scale, 1.0f);
        textRenderer.drawWithShadow(matrices, labelText, 0, 0, 0xFFFFFF);
        matrices.pop();
    }

    @Override
    public boolean mouseReleased(int mouseX, int mouseY, int button) {
        if (this.isHovered()) {
            onClick();
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
