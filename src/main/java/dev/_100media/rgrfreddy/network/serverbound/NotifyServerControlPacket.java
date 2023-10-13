package dev._100media.rgrfreddy.network.serverbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.NotifyClientControlPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.UUID;

public record NotifyServerControlPacket(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean shift, float leftImpulse, float forwardImpulse, boolean sprint) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null && holder.getControlledPlayer() instanceof ServerPlayer controlledServerPlayer) {
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> controlledServerPlayer),
                            new NotifyClientControlPacket(up, down, left, right, jump, shift, leftImpulse, forwardImpulse, sprint));
                }
            }
        });
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeBoolean(up);
        packetBuf.writeBoolean(down);
        packetBuf.writeBoolean(left);
        packetBuf.writeBoolean(right);
        packetBuf.writeBoolean(jump);
        packetBuf.writeBoolean(shift);
        packetBuf.writeFloat(leftImpulse);
        packetBuf.writeFloat(forwardImpulse);
        packetBuf.writeBoolean(sprint);
    }

    public static NotifyServerControlPacket read(FriendlyByteBuf buf) {
        return new NotifyServerControlPacket(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readFloat(),
                buf.readBoolean());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, NotifyServerControlPacket.class, NotifyServerControlPacket::read);
    }
}
