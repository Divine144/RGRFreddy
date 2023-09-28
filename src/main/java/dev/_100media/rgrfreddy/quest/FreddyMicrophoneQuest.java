package dev._100media.rgrfreddy.quest;

import dev._100media.hundredmediaquests.goal.QuestGoal;
import dev._100media.hundredmediaquests.quest.Quest;
import dev._100media.hundredmediaquests.quest.QuestType;
import dev._100media.hundredmediaquests.reward.ItemQuestReward;
import dev._100media.hundredmediaquests.reward.QuestReward;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.quest.goal.AquireGoldenItemGoal;
import dev._100media.rgrfreddy.quest.goal.AquireMusicDiscGoal;
import dev._100media.rgrfreddy.quest.goal.HitPlayersGoal;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FreddyMicrophoneQuest extends Quest {

    public FreddyMicrophoneQuest(QuestType<?> type) {
        super(type);
    }

    @Override
    protected List<QuestGoal> initializeGoals() {
        List<QuestGoal> goals = new ArrayList<>();
        goals.add(new AquireGoldenItemGoal(1));
        goals.add(new AquireMusicDiscGoal(1));
        goals.add(new HitPlayersGoal(30));
        return goals;
    }

    @Override
    protected List<QuestReward> initializeRewards() {
        List<QuestReward> rewards = new ArrayList<>();
        rewards.add(new ItemQuestReward(new ItemStack(ItemInit.FREDDY_MICROPHONE.get())));
        return rewards;
    }
}
