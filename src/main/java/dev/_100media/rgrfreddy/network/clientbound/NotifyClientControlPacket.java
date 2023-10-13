package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record NotifyClientControlPacket(boolean up, boolean down, boolean left, boolean right, boolean jump, boolean shift, float leftImpulse, float forwardImpulse, boolean sprint) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ClientHandler.syncPlayerInputToControlled(up, down, left, right, jump, shift, leftImpulse, forwardImpulse, sprint);
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

    public static NotifyClientControlPacket read(FriendlyByteBuf buf) {
        return new NotifyClientControlPacket(buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readFloat(), buf.readFloat(),
                buf.readBoolean());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, NotifyClientControlPacket.class, NotifyClientControlPacket::read);
    }
}
