package dev.scoreboardpreviewer.BoardScreen;

import dev.scoreboardpreviewer.CustomWidget.TCenterLabelWidget;
import dev.scoreboardpreviewer.CustomWidget.TLabelWidget;
import dev.scoreboardpreviewer.ScoreSource.ScoreboardData;
import dev.scoreboardpreviewer.model.ObjectiveData;
import dev.scoreboardpreviewer.utils.TextHandler;
import io.github.thecsdev.tcdcommons.api.client.gui.panel.TPanelElement;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.TScreen;
import io.github.thecsdev.tcdcommons.api.client.gui.util.Direction2D;
import io.github.thecsdev.tcdcommons.api.client.gui.widget.TButtonWidget;
import io.github.thecsdev.tcdcommons.api.client.gui.widget.TScrollBarWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BoardScreen extends TScreen {
    public static final Logger LOGGER = LoggerFactory.getLogger(BoardScreen.class);

    private static final int CONTENT_HEIGHT = 230;
    private static final int PANEL_HEIGHT = 180;
    private static final int SCROLLBAR_WIDTH = 4;
    private static final int SCROLLBAR_HEIGHT = 4;
    private static final int CLOSE_SIZE = 12;
    private static final int PANEL_START_POS = 30;
    private static final int PANEL_TOP_OFFSET = 25;

    public BoardScreen(String title) {
        super(Text.literal(title));
    }

    @Override
    protected void init() {
        layoutComponents();
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        layoutComponents();
    }

    private void exitAndClose() {
        LOGGER.info("{} just close", BoardScreen.class.getSimpleName());
        if (!ScoreboardData.isEmpty()) {
            ScoreboardData.clear();
        }
        close();
    }

    private void createNoDataMessage() {
        MutableText noDataMsg = Text.literal("暂无数据").styled(style -> style.withColor(Formatting.YELLOW));

        int textWidth = TextHandler.getTextWidth(noDataMsg, TextHandler.SCALE_1o5f);
        int textHeight = TextHandler.getTextHeight(TextHandler.SCALE_1o5f);

        int drawX = (getTpeWidth() / 2) - (textWidth / 2);
        int drawY = (getTpeHeight() / 2) - (textHeight / 2);

        addTChild(new TCenterLabelWidget(drawX, drawY, textWidth, textHeight, noDataMsg, TextHandler.SCALE_1o5f));
    }

    private void createCloseButton() {
        addTChild(new TButtonWidget(
                width - CLOSE_SIZE - 10,
                CLOSE_SIZE,
                CLOSE_SIZE,
                CLOSE_SIZE,
                Text.literal("✕").styled(style -> style.withColor(Formatting.RED)),
                btn -> exitAndClose()
        ));
    }

    private void putObjectiveData(TPanelElement panelElement) {
        List<ObjectiveData> objectivesData = ScoreboardData.getObjectives();
        int labelHeight = 12;
        int labelSpacing = 10;

        for (int i = 0; i < objectivesData.size(); i++) {
            ObjectiveData obj = objectivesData.get(i);

            MutableText labelText = Text.literal("")
                    .append(Text.literal(obj.objectiveDisplayName + "("))
                    .append(Text.literal(obj.objectiveName).formatted(Formatting.GREEN))
                    .append(Text.literal(")"));

            int y = i * (labelHeight + labelSpacing);

            TLabelWidget tLabelWidget = new TLabelWidget(0, y, TextHandler.getTextWidth(labelText, TextHandler.SCALE_1f), labelHeight, labelText);

            panelElement.addTChild(tLabelWidget);
        }
    }

    private void createPanelSection(int panelY) {
        int totalAvailableWidth = this.width - 2 * PANEL_START_POS;

        int scoreListMenuPanelWidth = totalAvailableWidth / 3 - 10;
        int spacingBetweenPanels = 20;

        int scoreBoardPanelWidth = totalAvailableWidth - scoreListMenuPanelWidth - spacingBetweenPanels - SCROLLBAR_WIDTH;

        int listPanelX = PANEL_START_POS;
        int boardPanelX = listPanelX + scoreListMenuPanelWidth + spacingBetweenPanels;

        TPanelElement scoreListMenuPanel = new TPanelElement(
                listPanelX, panelY,
                scoreListMenuPanelWidth, PANEL_HEIGHT
        );
        scoreListMenuPanel.setScrollFlags(TPanelElement.SCROLL_BOTH);

        TScrollBarWidget listMenuHorizontalScrollBar = new TScrollBarWidget(
                scoreListMenuPanel.getTpeX(),
                scoreListMenuPanel.getTpeEndY(),
                scoreListMenuPanel.getTpeWidth(),
                SCROLLBAR_HEIGHT,
                scoreListMenuPanel
        );
        listMenuHorizontalScrollBar.setSliderDirection(Direction2D.RIGHT);

        TScrollBarWidget listMenuVerticalScrollBar = new TScrollBarWidget(
                scoreListMenuPanel.getTpeEndX(),
                scoreListMenuPanel.getTpeY(),
                SCROLLBAR_WIDTH,
                scoreListMenuPanel.getTpeHeight(),
                scoreListMenuPanel
        );
        listMenuVerticalScrollBar.setSliderDirection(Direction2D.DOWN);

        TPanelElement scoreBoardPanel = new TPanelElement(
                boardPanelX, panelY,
                scoreBoardPanelWidth, PANEL_HEIGHT
        );
        scoreBoardPanel.setScrollFlags(2);

        TScrollBarWidget boardPanelScrollBar = new TScrollBarWidget(
                scoreBoardPanel.getTpeEndX(),
                scoreBoardPanel.getTpeY(),
                SCROLLBAR_WIDTH,
                scoreBoardPanel.getTpeHeight(),
                scoreBoardPanel
        );

        putObjectiveData(scoreListMenuPanel);

        addTChild(scoreListMenuPanel);
        addTChild(listMenuHorizontalScrollBar);
        addTChild(listMenuVerticalScrollBar);
        addTChild(scoreBoardPanel);
        addTChild(boardPanelScrollBar);
    }

    private void layoutComponents() {
        clearChildren();
        int contentY = (height - CONTENT_HEIGHT) / 2;

        createCloseButton();

        if (ScoreboardData.isEmpty()) {
            createNoDataMessage();
        } else {
            createPanelSection(contentY + PANEL_TOP_OFFSET);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            exitAndClose();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
