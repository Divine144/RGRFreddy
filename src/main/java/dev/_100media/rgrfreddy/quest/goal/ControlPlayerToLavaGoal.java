package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class ControlPlayerToLavaGoal extends BasicQuestGoal {

    public ControlPlayerToLavaGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.control_player_to_lava_goal";
    }
}
