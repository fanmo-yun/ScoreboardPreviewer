package dev.scoreboardpreviewer.ScoreSource;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.scoreboardpreviewer.BoardScreen.BoardScreen;
import dev.scoreboardpreviewer.ScoreboardPreviewer;
import dev.scoreboardpreviewer.model.ObjectiveData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.util.List;

public class ScoreSource {
    public static final Logger LOGGER = LoggerFactory.getLogger(ScoreSource.class);

    public static void register() {
        System.out.println("Registering ScoreSource");
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ScoreboardPreviewer.MOD_ID, "sync_scoreboard"),
                (client, handler, buf, responseSender) -> {
                    String resp = buf.readString();

                    client.execute(() -> {
                        Gson jsonParser = new Gson();
                        Type listType = new TypeToken<List<ObjectiveData>>(){}.getType();
                        ScoreboardData.setObjectives(jsonParser.fromJson(resp, listType));
                        client.setScreen(new BoardScreen("Scoreboard Previewer"));
                    });
                });
    }
}
