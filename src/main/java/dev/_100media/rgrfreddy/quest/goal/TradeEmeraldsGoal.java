package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class TradeEmeraldsGoal extends BasicQuestGoal {

    public TradeEmeraldsGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.trade_emeralds_goal";
    }
}
