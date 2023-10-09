package dev._100media.rgrfreddy.network.serverbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.network.NetworkHandler;
import dev._100media.rgrfreddy.network.clientbound.NotifyClientMousePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public record NotifyServerMousePacket(float xRot, float yRot) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                var holder = FreddyHolderAttacher.getHolderUnwrap(player);
                if (holder != null && holder.getControlledPlayer() instanceof ServerPlayer controlledServerPlayer) {
                    NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> controlledServerPlayer),
                            new NotifyClientMousePacket(xRot, yRot));
                }
            }
        });
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeFloat(xRot);
        packetBuf.writeFloat(yRot);
    }

    public static NotifyServerMousePacket read(FriendlyByteBuf buf) {
        return new NotifyServerMousePacket(buf.readFloat(), buf.readFloat());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, NotifyServerMousePacket.class, NotifyServerMousePacket::read);
    }
}
