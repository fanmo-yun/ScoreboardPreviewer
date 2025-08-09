package dev.scoreboardpreviewer.utils;

import dev.scoreboardpreviewer.ScoreboardPreviewer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyBindingHandler {
    private static KeyBinding openStatsKey;
    public static final Logger LOGGER = LoggerFactory.getLogger(ScoreboardPreviewer.MOD_ID);

    private static void requestScoreboardData() {
        LOGGER.info("Request ScoreboardData");
        ClientPlayNetworking.send(
                new Identifier(ScoreboardPreviewer.MOD_ID, "request_scoreboard"),
                PacketByteBufs.create()
        );
    }

    public static void registerOpenStatsKey() {
        LOGGER.info("Registering Open Stats Key");
        openStatsKey = new KeyBinding(
                "key." + ScoreboardPreviewer.MOD_ID + ".open_board",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category." + ScoreboardPreviewer.MOD_ID
        );

        KeyBindingHelper.registerKeyBinding(openStatsKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openStatsKey.wasPressed()) {
                LOGGER.info("Open stats key pressed");
                requestScoreboardData();
            }
        });
    }
}
