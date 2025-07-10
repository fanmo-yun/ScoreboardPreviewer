package dev.scoreboardpreviewer.DataSync;

import com.google.gson.Gson;
import dev.scoreboardpreviewer.model.ObjectiveData;
import dev.scoreboardpreviewer.model.PlayerScore;
import dev.scoreboardpreviewer.ScoreboardPreviewer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DataSync {
    public static final Logger LOGGER = LoggerFactory.getLogger(DataSync.class);

    public static void register() {
        LOGGER.info("Registering DataSync");
        ServerPlayNetworking.registerGlobalReceiver(new Identifier(ScoreboardPreviewer.MOD_ID, "request_scoreboard"),
                (server, player, handler, buf, responseSender) -> server.execute(() -> {
                    Scoreboard scoreboard = Objects.requireNonNull(player.getServer()).getScoreboard();
                    Collection<ScoreboardObjective> objectives = scoreboard.getObjectives();

                    List<ObjectiveData> list = new ArrayList<>();

                    for (ScoreboardObjective obj : objectives) {
                        ObjectiveData od = new ObjectiveData(
                                obj.getName(),
                                obj.getDisplayName().getString(),
                                obj.getCriterion().getName()
                        );

                        scoreboard.getAllPlayerScores(obj).stream()
                                .map(score -> new PlayerScore(score.getPlayerName(), score.getScore()))
                                .sorted((a, b) -> {
                                    boolean specialA = !Character.isLetterOrDigit(a.Name.charAt(0));
                                    boolean specialB = !Character.isLetterOrDigit(b.Name.charAt(0));
                                    if (specialA && !specialB) return -1;
                                    if (!specialA && specialB) return 1;
                                    int scoreCmp = Integer.compare(b.Score, a.Score);
                                    if (scoreCmp != 0) return scoreCmp;
                                    return a.Name.compareTo(b.Name);
                                })
                                .forEach(od.data::add);

                        list.add(od);
                    }

                    String json = new Gson().toJson(list);
                    PacketByteBuf out = PacketByteBufs.create();
                    out.writeString(json);
                    ServerPlayNetworking.send(player, new Identifier(ScoreboardPreviewer.MOD_ID, "sync_scoreboard"), out);
                }));
    }
}
