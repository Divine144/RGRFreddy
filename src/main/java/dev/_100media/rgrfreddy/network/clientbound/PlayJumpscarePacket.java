package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record PlayJumpscarePacket(int evolutionStage) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientHandler.startJumpscareAnimation(evolutionStage));
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeInt(evolutionStage);
    }

    public static PlayJumpscarePacket read(FriendlyByteBuf buf) {
        return new PlayJumpscarePacket(buf.readInt());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, PlayJumpscarePacket.class, PlayJumpscarePacket::read);
    }
}
