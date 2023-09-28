package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class HitPlayersGoal extends BasicQuestGoal {

    public HitPlayersGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.hit_players_goal";
    }
}
