package dev._100media.rgrfreddy.quest;

import dev._100media.hundredmediaquests.goal.QuestGoal;
import dev._100media.hundredmediaquests.quest.Quest;
import dev._100media.hundredmediaquests.quest.QuestType;
import dev._100media.hundredmediaquests.reward.ItemQuestReward;
import dev._100media.hundredmediaquests.reward.QuestReward;
import dev._100media.rgrfreddy.init.BlockInit;
import dev._100media.rgrfreddy.quest.goal.ControlPlayerToLavaGoal;
import dev._100media.rgrfreddy.quest.goal.HitWardenGoal;
import dev._100media.rgrfreddy.quest.goal.JumpscareHuntersGoal;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ToyBoxTrapQuest extends Quest {

    public ToyBoxTrapQuest(QuestType<?> type) {
        super(type);
    }

    @Override
    protected List<QuestGoal> initializeGoals() {
        List<QuestGoal> goals = new ArrayList<>();
        goals.add(new ControlPlayerToLavaGoal(1));
        goals.add(new JumpscareHuntersGoal(3));
        goals.add(new HitWardenGoal(10));
        return goals;
    }

    @Override
    protected List<QuestReward> initializeRewards() {
        List<QuestReward> rewards = new ArrayList<>();
        rewards.add(new ItemQuestReward(new ItemStack(BlockInit.TOY_BOX_TRAP_BLOCK.get().asItem())));
        return rewards;
    }
}
