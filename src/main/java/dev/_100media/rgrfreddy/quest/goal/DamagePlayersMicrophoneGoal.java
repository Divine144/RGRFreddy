package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.BasicQuestGoal;

public class DamagePlayersMicrophoneGoal extends BasicQuestGoal {

    public DamagePlayersMicrophoneGoal(double target) {
        super(target);
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.hit_players_microphone_goal";
    }
}
