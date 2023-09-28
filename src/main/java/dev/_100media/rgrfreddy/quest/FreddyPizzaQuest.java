package dev._100media.rgrfreddy.quest;

import dev._100media.hundredmediaquests.goal.KillSpecificTypeGoal;
import dev._100media.hundredmediaquests.goal.QuestGoal;
import dev._100media.hundredmediaquests.quest.Quest;
import dev._100media.hundredmediaquests.quest.QuestType;
import dev._100media.hundredmediaquests.reward.ItemQuestReward;
import dev._100media.hundredmediaquests.reward.QuestReward;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.quest.goal.DamagePlayersMicrophoneGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FreddyPizzaQuest extends Quest {

    public FreddyPizzaQuest(QuestType<?> type) {
        super(type);
    }

    @Override
    protected List<QuestGoal> initializeGoals() {
        List<QuestGoal> goals = new ArrayList<>();
        goals.add(new KillSpecificTypeGoal(1, EntityType.ENDER_DRAGON));
        goals.add(new DamagePlayersMicrophoneGoal(100));
        goals.add(new KillSpecificTypeGoal(1, EntityType.WITHER));
        return goals;
    }

    @Override
    protected List<QuestReward> initializeRewards() {
        List<QuestReward> rewards = new ArrayList<>();
        rewards.add(new ItemQuestReward(new ItemStack(ItemInit.FREDDY_PIZZA.get())));
        return rewards;
    }
}
