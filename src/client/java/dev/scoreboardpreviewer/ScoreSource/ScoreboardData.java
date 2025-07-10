package dev.scoreboardpreviewer.ScoreSource;

import dev.scoreboardpreviewer.model.ObjectiveData;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardData {
    private static List<ObjectiveData> objectives = new ArrayList<>();

    public static void setObjectives(List<ObjectiveData> list) {
        objectives = list;
    }

    public static List<ObjectiveData> getObjectives() {
        return objectives;
    }

    public static boolean isEmpty() {
        return objectives.isEmpty();
    }

    public static void clear() {
        objectives.clear();
    }
}
