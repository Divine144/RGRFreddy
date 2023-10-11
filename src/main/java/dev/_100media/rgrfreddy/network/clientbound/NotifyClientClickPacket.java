package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public class NotifyClientClickPacket implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(ClientHandler::handleClick);
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {

    }

    public static NotifyClientClickPacket read(FriendlyByteBuf buf) {
        return new NotifyClientClickPacket();
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, NotifyClientClickPacket.class, NotifyClientClickPacket::read);
    }
}
