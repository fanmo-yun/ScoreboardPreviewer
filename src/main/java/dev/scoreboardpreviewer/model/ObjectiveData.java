package dev.scoreboardpreviewer.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveData {
    public final String objectiveName;
    public final String objectiveDisplayName;
    public final String objectiveCriteriaName;
    public List<PlayerScore> data;

    public ObjectiveData(String objectiveName, String objectiveDisplayName, String objectiveCriteriaName) {
        this.objectiveName = objectiveName;
        this.objectiveDisplayName = objectiveDisplayName;
        this.objectiveCriteriaName = objectiveCriteriaName;
        this.data = new ArrayList<>();
    }
}
