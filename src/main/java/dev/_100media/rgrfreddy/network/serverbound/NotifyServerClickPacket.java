package dev._100media.rgrfreddy.network.serverbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.NotifyClientClickPacket;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class NotifyServerClickPacket implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Player controlledPlayer = FreddyUtils.getControlledPlayer(player);
                if (controlledPlayer instanceof ServerPlayer serverPlayer) {
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new NotifyClientClickPacket());
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {

    }

    public static NotifyServerClickPacket read(FriendlyByteBuf buf) {
        return new NotifyServerClickPacket();
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, NotifyServerClickPacket.class, NotifyServerClickPacket::read);
    }
}
