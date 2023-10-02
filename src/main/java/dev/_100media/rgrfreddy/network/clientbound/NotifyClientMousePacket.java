package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record NotifyClientMousePacket(float xRot, float yRot) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ClientHandler.syncPlayerMouseControlled(xRot, yRot);
        });
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeFloat(xRot);
        packetBuf.writeFloat(yRot);
    }

    public static NotifyClientMousePacket read(FriendlyByteBuf buf) {
        return new NotifyClientMousePacket(buf.readFloat(), buf.readFloat());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, NotifyClientMousePacket.class, NotifyClientMousePacket::read);
    }
}