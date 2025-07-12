package dev.scoreboardpreviewer.BoardScreen;

import dev.scoreboardpreviewer.CustomWidget.TCenterLabelWidget;
import dev.scoreboardpreviewer.CustomWidget.TClickableLabelWidget;
import dev.scoreboardpreviewer.CustomWidget.TPanelWidget;
import dev.scoreboardpreviewer.ScoreSource.ScoreboardData;
import dev.scoreboardpreviewer.model.ObjectiveData;
import dev.scoreboardpreviewer.model.PlayerScore;
import dev.scoreboardpreviewer.utils.TextHandler;
import io.github.thecsdev.tcdcommons.api.client.gui.screen.TScreen;
import io.github.thecsdev.tcdcommons.api.client.gui.util.Direction2D;
import io.github.thecsdev.tcdcommons.api.client.gui.widget.TButtonWidget;
import io.github.thecsdev.tcdcommons.api.client.gui.widget.TScrollBarWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
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
    private static final int LABEL_HEIGHT = 20;
    private List<ObjectiveData> objectivesData = ScoreboardData.getObjectives();
    private TClickableLabelWidget currentSelectedLabel = null;

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
        objectivesData = ScoreboardData.getObjectives();
        currentSelectedLabel = null;
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

        TCenterLabelWidget noMsgLabel = new TCenterLabelWidget(drawX, drawY, textWidth, textHeight, noDataMsg);
        noMsgLabel.setScale(TextHandler.SCALE_1o5f);

        addTChild(noMsgLabel);
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

    private void createPanelSection(int panelY) {
        int totalAvailableWidth = this.width - 2 * PANEL_START_POS;

        int scoreListMenuPanelWidth = totalAvailableWidth / 3 - 10;
        int spacingBetweenPanels = 20;

        int scoreBoardPanelWidth = totalAvailableWidth - scoreListMenuPanelWidth - spacingBetweenPanels - SCROLLBAR_WIDTH;

        int listPanelX = PANEL_START_POS;
        int boardPanelX = listPanelX + scoreListMenuPanelWidth + spacingBetweenPanels;

        TPanelWidget scoreListMenuPanel = new TPanelWidget(
                listPanelX, panelY,
                scoreListMenuPanelWidth, PANEL_HEIGHT
        );
        scoreListMenuPanel.setScrollFlags(TPanelWidget.SCROLL_BOTH);
        scoreListMenuPanel.setScrollPadding(0);

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

        TPanelWidget scoreBoardPanel = new TPanelWidget(
                boardPanelX, panelY,
                scoreBoardPanelWidth, PANEL_HEIGHT
        );
        scoreBoardPanel.setScrollFlags(TPanelWidget.SCROLL_VERTICAL);
        scoreBoardPanel.setScrollPadding(0);

        TScrollBarWidget boardPanelScrollBar = new TScrollBarWidget(
                scoreBoardPanel.getTpeEndX(),
                scoreBoardPanel.getTpeY(),
                SCROLLBAR_WIDTH,
                scoreBoardPanel.getTpeHeight(),
                scoreBoardPanel
        );

        putObjectiveData(scoreListMenuPanel, scoreBoardPanel);

        addTChild(scoreListMenuPanel);
        addTChild(listMenuHorizontalScrollBar);
        addTChild(listMenuVerticalScrollBar);
        addTChild(scoreBoardPanel);
        addTChild(boardPanelScrollBar);
    }

    private void changeLabelColorAndListScore(TClickableLabelWidget label, TPanelWidget boardPanel, String objectiveName, String objectiveDisplayName) {
        if (currentSelectedLabel != null && currentSelectedLabel != label) {
            MutableText oldText = currentSelectedLabel.getLabelText().copy()
                    .setStyle(currentSelectedLabel.getLabelText().getStyle().withColor(Formatting.WHITE));
            currentSelectedLabel.setLabelText(oldText);
        }

        MutableText newText = label.getLabelText().copy()
                .setStyle(label.getLabelText().getStyle().withColor(Formatting.YELLOW));
        label.setLabelText(newText);
        currentSelectedLabel = label;

        addAndListScore(objectiveDisplayName, boardPanel, objectiveName);

        MinecraftClient.getInstance().getSoundManager().play(
                PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0f, 1.0f)
        );
    }

    private void addAndListScore(String objectiveDisplayName, TPanelWidget boardPanel, String objectiveName) {
        boardPanel.clearTChildren();
        int labelWid = boardPanel.getTpeWidth() / 2;

        boardPanel.addTChild(new TCenterLabelWidget(0, 0, boardPanel.getTpeWidth(), LABEL_HEIGHT, Text.literal(objectiveDisplayName)));
        boardPanel.addTChild(new TCenterLabelWidget(0, LABEL_HEIGHT, labelWid, LABEL_HEIGHT, Text.literal("名称")));
        boardPanel.addTChild(new TCenterLabelWidget(labelWid, LABEL_HEIGHT, labelWid, LABEL_HEIGHT, Text.literal("分数")));

        for (ObjectiveData objectiveData : this.objectivesData) {
            if (objectiveData.objectiveName.equals(objectiveName)) {
                List<PlayerScore> playerScores = objectiveData.data;
                for (int i = 0; i < playerScores.size(); i++) {
                    PlayerScore playerScore = playerScores.get(i);
                    int y = LABEL_HEIGHT * (i + 2);

                    if (i == 0) {
                        boardPanel.addTChild(new TCenterLabelWidget(0, y, labelWid, LABEL_HEIGHT, Text.literal(playerScore.Name).styled(style -> style.withColor(0xFFD700))));
                        boardPanel.addTChild(new TCenterLabelWidget(labelWid, y, labelWid, LABEL_HEIGHT, Text.literal(String.valueOf(playerScore.Score)).styled(style -> style.withColor(0xFFD700))));
                    } else if (i == 1) {
                        boardPanel.addTChild(new TCenterLabelWidget(0, y, labelWid, LABEL_HEIGHT, Text.literal(playerScore.Name).styled(style -> style.withColor(0xC0C0C0))));
                        boardPanel.addTChild(new TCenterLabelWidget(labelWid, y, labelWid, LABEL_HEIGHT, Text.literal(String.valueOf(playerScore.Score)).styled(style -> style.withColor(0xC0C0C0))));
                    } else if (i == 2) {
                        boardPanel.addTChild(new TCenterLabelWidget(0, y, labelWid, LABEL_HEIGHT, Text.literal(playerScore.Name).styled(style -> style.withColor(0xB87333))));
                        boardPanel.addTChild(new TCenterLabelWidget(labelWid, y, labelWid, LABEL_HEIGHT, Text.literal(String.valueOf(playerScore.Score)).styled(style -> style.withColor(0xB87333))));
                    } else {
                        boardPanel.addTChild(new TCenterLabelWidget(0, y, labelWid, LABEL_HEIGHT, Text.literal(playerScore.Name)));
                        boardPanel.addTChild(new TCenterLabelWidget(labelWid, y, labelWid, LABEL_HEIGHT, Text.literal(String.valueOf(playerScore.Score))));
                    }
                }
                return;
            }
        }
    }

    private void putObjectiveData(TPanelWidget menuPanel, TPanelWidget boardPanel) {
        for (int i = 0; i < this.objectivesData.size(); i++) {
            ObjectiveData obj = this.objectivesData.get(i);

            MutableText labelText = Text.literal(obj.objectiveDisplayName + "(" + obj.objectiveName + ")");

            TClickableLabelWidget tLabelWidget = new TClickableLabelWidget(0, i * LABEL_HEIGHT, TextHandler.getTextWidth(labelText, TextHandler.SCALE_1f) + 4, LABEL_HEIGHT, labelText);
            tLabelWidget.setOnClick(() -> changeLabelColorAndListScore(tLabelWidget, boardPanel, obj.objectiveName, obj.objectiveDisplayName));
            tLabelWidget.setScale(TextHandler.SCALE_1f);

            menuPanel.addTChild(tLabelWidget);
        }
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
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
