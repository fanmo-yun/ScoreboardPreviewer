package dev.scoreboardpreviewer;

import dev.scoreboardpreviewer.ScoreSource.ScoreSource;
import dev.scoreboardpreviewer.utils.KeyBindingHandler;
import net.fabricmc.api.ClientModInitializer;

public class ScoreboardPreviewerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ScoreSource.register();
		KeyBindingHandler.registerOpenStatsKey();
	}
}