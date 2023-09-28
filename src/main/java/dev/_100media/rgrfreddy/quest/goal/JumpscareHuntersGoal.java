package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class JumpscareHuntersGoal extends BasicQuestGoal {

    public JumpscareHuntersGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.jumpscare_players_goal";
    }
}
