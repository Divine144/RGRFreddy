package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class AquireGoldenItemGoal extends BasicQuestGoal {

    public AquireGoldenItemGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.get_enchanted_golden_item_goal";
    }
}
