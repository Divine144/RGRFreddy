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

public record NotifyServerControlPacket(boolean up, boolean down, boolean left, boolean right, boolean shift, float leftImpulse, float forwardImpulse) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null) {
                    UUID controlledPlayerUUID = holder.getControlledPlayer();
                    if (controlledPlayerUUID != null) {
                        Player controlledPlayer = player.level().getPlayerByUUID(controlledPlayerUUID);
                        if (controlledPlayer instanceof ServerPlayer controlledServerPlayer) {
                            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> controlledServerPlayer),
                                    new NotifyClientControlPacket(up, down, left, right, shift, leftImpulse, forwardImpulse));
                        }
                    }
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
        packetBuf.writeBoolean(shift);
        packetBuf.writeFloat(leftImpulse);
        packetBuf.writeFloat(forwardImpulse);
    }

    public static NotifyServerControlPacket read(FriendlyByteBuf buf) {
        return new NotifyServerControlPacket(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readFloat());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, NotifyServerControlPacket.class, NotifyServerControlPacket::read);
    }
}
