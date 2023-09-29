package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record UnboundControlsPacket(boolean resetAttack) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            if (resetAttack) {
                ClientHandler.resetAttack();
            }
            else ClientHandler.unboundControls();
        });
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeBoolean(resetAttack);
    }

    public static UnboundControlsPacket read(FriendlyByteBuf buf) {
        return new UnboundControlsPacket(buf.readBoolean());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, UnboundControlsPacket.class, UnboundControlsPacket::read);
    }
}
