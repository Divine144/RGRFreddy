package dev._100media.rgrfreddy.client.animatable;

import dev._100media.hundredmediageckolib.client.animatable.MotionAttackAnimatable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class FreddyAnimatable extends MotionAttackAnimatable {

    protected static final RawAnimation FORTNITE_DANCE = RawAnimation.begin().thenLoop("dance");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenLoop("attack");
    protected static final RawAnimation CROUCH = RawAnimation.begin().thenLoop("crouch");
    public static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("sit");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    @Override
    protected PlayState attackAnimationEvent(AnimationState<? extends MotionAttackAnimatable> state) {
        AnimationController<?> controller = state.getController();
        if (state.getData(DataTickets.ENTITY) instanceof AbstractClientPlayer player) {
            controller.transitionLength(0);
            if (player.swingTime > 0) {
                controller.setAnimation(ATTACK);
                return PlayState.CONTINUE;
            }
            motionAnimationEvent(state);
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected PlayState motionAnimationEvent(AnimationState<? extends MotionAttackAnimatable> state) {
        AnimationController<?> controller = state.getController();
        if (state.getData(DataTickets.ENTITY) instanceof AbstractClientPlayer player) {
            controller.transitionLength(0);
            if (player.getVehicle() != null) {
                controller.setAnimation(SIT);
            }
            else if (player.isShiftKeyDown()) {
                controller.setAnimation(CROUCH);
            }
            else if (player.xo != player.getX() || player.zo != player.getZ()) {
                controller.setAnimation(player.isSprinting() && !player.isCrouching() ? RUN : WALK);
            }
            else {
                controller.setAnimation(IDLE);
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public double getTick(Object entity) {
        return super.getTick(entity);
    }
}
