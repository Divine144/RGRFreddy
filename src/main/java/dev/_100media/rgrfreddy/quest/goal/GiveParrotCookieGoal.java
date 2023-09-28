package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class GiveParrotCookieGoal extends BasicQuestGoal {

    public GiveParrotCookieGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.give_parrot_cookie_goal";
    }
}
