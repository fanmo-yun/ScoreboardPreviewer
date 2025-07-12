package dev.scoreboardpreviewer.CustomWidget;

import io.github.thecsdev.tcdcommons.api.client.gui.panel.TPanelElement;

public class TPanelWidget extends TPanelElement {
    public TPanelWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, double deltaX, double deltaY, int button) {
        return false;
    }
}
