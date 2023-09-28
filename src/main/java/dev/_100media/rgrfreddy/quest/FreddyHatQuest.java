package dev._100media.rgrfreddy.quest;

import dev._100media.hundredmediaquests.goal.KillSpecificTypeGoal;
import dev._100media.hundredmediaquests.goal.QuestGoal;
import dev._100media.hundredmediaquests.quest.Quest;
import dev._100media.hundredmediaquests.quest.QuestType;
import dev._100media.hundredmediaquests.reward.ItemQuestReward;
import dev._100media.hundredmediaquests.reward.QuestReward;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.quest.goal.StayNearHunterGoal;
import dev._100media.rgrfreddy.quest.goal.TameEntityGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FreddyHatQuest extends Quest {

    public FreddyHatQuest(QuestType<?> type) {
        super(type);
    }

    @Override
    protected List<QuestGoal> initializeGoals() {
        List<QuestGoal> goals = new ArrayList<>();
        goals.add(new TameEntityGoal(EntityType.WOLF, 3));
        goals.add(new StayNearHunterGoal(120));
        goals.add(new KillSpecificTypeGoal(32, EntityType.COW));
        return goals;
    }

    @Override
    protected List<QuestReward> initializeRewards() {
        List<QuestReward> rewards = new ArrayList<>();
        rewards.add(new ItemQuestReward(new ItemStack(ItemInit.FREDDY_HAT.get())));
        return rewards;
    }
}
