package dev._100media.rgrfreddy.quest;

import dev._100media.hundredmediaquests.goal.QuestGoal;
import dev._100media.hundredmediaquests.quest.Quest;
import dev._100media.hundredmediaquests.quest.QuestType;
import dev._100media.hundredmediaquests.reward.ItemQuestReward;
import dev._100media.hundredmediaquests.reward.QuestReward;
import dev._100media.rgrfreddy.init.ItemInit;
import dev._100media.rgrfreddy.quest.goal.CraftDiscGoal;
import dev._100media.rgrfreddy.quest.goal.GiveParrotCookieGoal;
import dev._100media.rgrfreddy.quest.goal.TradeEmeraldsGoal;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToyArmyQuest extends Quest {

    public ToyArmyQuest(QuestType<?> type) {
        super(type);
    }

    @Override
    protected List<QuestGoal> initializeGoals() {
        List<QuestGoal> goals = new ArrayList<>();
        goals.add(new CraftDiscGoal(1));
        goals.add(new TradeEmeraldsGoal(50));
        goals.add(new GiveParrotCookieGoal(1));
        return goals;
    }

    @Override
    protected List<QuestReward> initializeRewards() {
        List<QuestReward> rewards = new ArrayList<>();
        rewards.add(new ItemQuestReward(new ItemStack(ItemInit.TOY_ARMY_ITEM.get())));
        return rewards;
    }
}