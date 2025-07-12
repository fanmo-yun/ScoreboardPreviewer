package dev.scoreboardpreviewer.CustomWidget;

import net.minecraft.text.MutableText;

public class TClickableLabelWidget extends TLabelWidget {
    private Runnable onClickCallBack = null;

    public TClickableLabelWidget(int x, int y, int width, int height, MutableText text) {
        super(x, y, width, height, text);
    }

    public void setOnClick(Runnable onClick) {
        this.onClickCallBack = onClick;
    }

    private void onClick() {
        if (onClickCallBack != null) {
            onClickCallBack.run();
        }
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
