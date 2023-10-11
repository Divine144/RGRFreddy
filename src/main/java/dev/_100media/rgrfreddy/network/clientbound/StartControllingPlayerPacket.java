package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class StartControllingPlayerPacket implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(ClientHandler::addControlledPlayer);
        context.setPacketHandled(true);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
    }

    public static StartControllingPlayerPacket read(FriendlyByteBuf buf) {
        return new StartControllingPlayerPacket();
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, StartControllingPlayerPacket.class, StartControllingPlayerPacket::read);
    }
}