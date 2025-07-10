package dev.scoreboardpreviewer;

import dev.scoreboardpreviewer.DataSync.DataSync;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreboardPreviewer implements ModInitializer {
	public static final String MOD_ID = "scoreboardpreviewer";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing ScoreboardPreviewer");
		DataSync.register();
	}
}