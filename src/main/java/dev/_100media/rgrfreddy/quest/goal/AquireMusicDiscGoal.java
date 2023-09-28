package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class AquireMusicDiscGoal extends BasicQuestGoal {

    public AquireMusicDiscGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.get_music_disc_goal";
    }
}
