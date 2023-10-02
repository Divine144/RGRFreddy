package dev._100media.rgrfreddy.event;

import dev._100media.hundredmediaquests.network.HMQNetworkHandler;
import dev._100media.hundredmediaquests.network.packet.OpenMainTreePacket;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.init.EffectInit;
import dev._100media.rgrfreddy.init.MenuInit;
import dev._100media.rgrfreddy.network.ClientHandler;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.serverbound.NotifyServerControlPacket;
import dev._100media.rgrfreddy.util.ControllingPlayerCameraManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void keyPressEvent(InputEvent.Key event) {
        if (ClientModEvents.SKILL_TREE_KEY.isDown()) {
            HMQNetworkHandler.INSTANCE.sendToServer(new OpenMainTreePacket(MenuInit.SKILL_TREE.get()));
        }
        if (ClientModEvents.SWITCH_CONTROL_KEY.isDown()) {
            Player player = ClientHandler.getPlayer();
            if (player instanceof LocalPlayer localPlayer) {
                var holder = FreddyHolderAttacher.getHolderUnwrap(localPlayer);
                UUID controlledPlayerUUID = holder.getControlledPlayer();
                if (holder != null && controlledPlayerUUID != null && holder.getControlTicks() > 0) {
                    Player controlledPlayer = player.level().getPlayerByUUID(controlledPlayerUUID);
                    if (controlledPlayer instanceof RemotePlayer controlledLocal) {
                        if (ControllingPlayerCameraManager.controlledPlayer == null) {
                            ControllingPlayerCameraManager.add(controlledLocal);
                            return;
                        }
                    }
                    ControllingPlayerCameraManager.remove();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onInputEvent(InputEvent.MouseButton.Pre event) {
        Player player = ClientHandler.getPlayer();
        if (player instanceof LocalPlayer localPlayer) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(localPlayer);
            if (holder != null) {
                UUID controllingPlayerUUID = holder.getControllingPlayer();
                if (controllingPlayerUUID != null) {
                    Player controllingPlayer = player.level().getPlayerByUUID(controllingPlayerUUID);
                    if (controllingPlayer instanceof RemotePlayer) {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void screenShake(ViewportEvent.ComputeCameraAngles event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            var holder = FreddyHolderAttacher.getHolderUnwrap(player);
            if (holder != null && holder.getFearTicks() > 0) {
                event.setYaw(event.getYaw() + (float) (Math.sin((double) player.tickCount * 3.25D) * Math.PI * (double) 0.4F));
            }
        }
    }

    @SubscribeEvent
    public static void movementInputUpdate(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();
        var holder = FreddyHolderAttacher.getHolderUnwrap(player);
        if (holder != null) {
            UUID controlledPlayerUUID = holder.getControlledPlayer();
            UUID controllingPlayerUUID = holder.getControllingPlayer();
            if (controlledPlayerUUID != null) {
                Player controlledPlayer = player.level().getPlayerByUUID(controlledPlayerUUID);
                if (controlledPlayer instanceof RemotePlayer) {
                    NetworkHandler.INSTANCE.sendToServer(new NotifyServerControlPacket(
                            input.up, input.down, input.left, input.right,
                            input.shiftKeyDown, input.leftImpulse, input.forwardImpulse
                    ));
                }
            }
            else if (controllingPlayerUUID != null) {
                Player controllingPlayer = player.level().getPlayerByUUID(controllingPlayerUUID);
                if (controllingPlayer instanceof RemotePlayer) {

                }
            }
        }
        if (player.hasEffect(EffectInit.NETTED.get())) {
            input.up = false;
            input.down = false;
            input.left = false;
            input.right = false;
            input.jumping = false;
            input.shiftKeyDown = false;
            input.leftImpulse = 0;
            input.forwardImpulse = 0;
        }
    }

    @SubscribeEvent
    public static void clone(ClientPlayerNetworkEvent.Clone event) {
        if (event.getNewPlayer().isLocalPlayer()) {
            ClientHandler.resetAttack();
        }
    }
}
