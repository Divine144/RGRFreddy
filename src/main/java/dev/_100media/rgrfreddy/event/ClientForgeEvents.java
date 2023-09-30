package dev._100media.rgrfreddy.event;

import dev._100media.hundredmediaquests.network.HMQNetworkHandler;
import dev._100media.hundredmediaquests.network.packet.OpenMainTreePacket;
import dev._100media.rgrfreddy.init.EffectInit;
import dev._100media.rgrfreddy.init.MenuInit;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.client.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.MovementInputUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientForgeEvents {

    @SubscribeEvent
    public static void keyPressEvent(InputEvent.Key event) {
        if (ClientModEvents.SKILL_TREE_KEY.isDown()) {
            HMQNetworkHandler.INSTANCE.sendToServer(new OpenMainTreePacket(MenuInit.SKILL_TREE.get()));
        }
    }

    @SubscribeEvent
    public static void movementInputUpdate(MovementInputUpdateEvent event) {
        Player player = event.getEntity();
        Input input = event.getInput();
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
