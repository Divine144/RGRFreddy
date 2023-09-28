package dev._100media.rgrfreddy.quest.goal;

import dev._100media.hundredmediaquests.goal.QuestGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class StayNearHunterGoal implements QuestGoal {
    private final double target;
    private double progress;

    public StayNearHunterGoal(double target) {
        this.target = target;
    }

    @Override
    public boolean addProgress(double amount) {
        double previousProgress = progress;
        progress += amount;
        if (progress > target) {
            progress = target;
        }
        if (progress < 0) {
            progress = 0;
        }
        return progress != previousProgress;
    }

    @Override
    public double getProgress() {
        return progress;
    }

    @Override
    public double getTargetAmount() {
        return target;
    }

    @Override
    public boolean isGoalMet() {
        return progress >= target;
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("progress", progress);
        return tag;
    }

    @Override
    public void load(CompoundTag tag) {
        progress = tag.getDouble("progress");
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(getDescriptionId());
    }

    @Override
    public Component getDescription() {
        return Component.translatable(getDescriptionId() + ".description", (int)getTargetAmount());
    }

    @Override
    public Component getProgressIndicator() {
        MutableComponent component = Component.literal((int)getProgress() + "/" + (int)getTargetAmount()).withStyle(Style.EMPTY.withBold(true));
        if (isGoalMet()) {
            component.withStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF00FF00)));
        }
        return component;
    }

    @Override
    public String getDescriptionId() {
        return "quest.goal.rgrfreddy.stay_near_hunter_goal";
    }

    public void resetProgress() {
        this.progress = 0;
    }
}
