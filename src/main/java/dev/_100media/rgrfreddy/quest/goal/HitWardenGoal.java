package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class HitWardenGoal extends BasicQuestGoal {

    public HitWardenGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.punch_warden_goal";
    }
}
