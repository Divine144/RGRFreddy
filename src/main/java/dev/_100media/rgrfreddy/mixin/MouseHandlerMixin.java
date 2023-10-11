package dev._100media.rgrfreddy.mixin;

import com.mojang.blaze3d.Blaze3D;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.serverbound.NotifyServerMousePacket;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.SmoothDouble;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {

    @Shadow private double lastMouseEventTime;

    @Shadow public abstract boolean isMouseGrabbed();

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private SmoothDouble smoothTurnX;

    @Shadow @Final private SmoothDouble smoothTurnY;

    @Shadow private double accumulatedDX;

    @Shadow private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void turnPlayer(CallbackInfo ci) {
        LocalPlayer instance = Minecraft.getInstance().player;
        if (instance != null) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(instance);
            if (holder != null) {
                UUID controllingPlayer = holder.getControllingPlayer();
                UUID controlledPlayer = holder.getControlledPlayer();
                if (controllingPlayer != null) {
                    Player player = instance.level().getPlayerByUUID(controllingPlayer);
                    if (player != null && !FreddyUtils.hasLeftControl(player)) {
                        ci.cancel();
                    }
                }
                else if (controlledPlayer != null) {
                    Player player = instance.level().getPlayerByUUID(controlledPlayer);
                    if (player != null && !FreddyUtils.hasLeftControl(player)) {
                        double d0 = Blaze3D.getTime();
                        double d1 = d0 - this.lastMouseEventTime;
                        this.lastMouseEventTime = d0;
                        if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
                            double d4 = this.minecraft.options.sensitivity().get() * (double) 0.6F + (double) 0.2F;
                            double d5 = d4 * d4 * d4;
                            double d6 = d5 * 8.0D;
                            double d2;
                            double d3;
                            if (this.minecraft.options.smoothCamera) {
                                double d7 = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * d6, d1 * d6);
                                double d8 = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * d6, d1 * d6);
                                d2 = d7;
                                d3 = d8;
                            }
                            else if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.isScoping()) {
                                this.smoothTurnX.reset();
                                this.smoothTurnY.reset();
                                d2 = this.accumulatedDX * d5;
                                d3 = this.accumulatedDY * d5;
                            }
                            else {
                                this.smoothTurnX.reset();
                                this.smoothTurnY.reset();
                                d2 = this.accumulatedDX * d6;
                                d3 = this.accumulatedDY * d6;
                            }
                            this.accumulatedDX = 0.0D;
                            this.accumulatedDY = 0.0D;
                            int i = 1;
                            if (this.minecraft.options.invertYMouse().get()) {
                                i = -1;
                            }
                            this.minecraft.getTutorial().onMouse(d2, d3);
                            if (this.minecraft.player != null) {
                                NetworkHandler.INSTANCE.sendToServer(new NotifyServerMousePacket((float) d2, (float) (d3 * i)));
                                ci.cancel();
                            }
                        }
                    }
                }
            }
        }
    }
}
