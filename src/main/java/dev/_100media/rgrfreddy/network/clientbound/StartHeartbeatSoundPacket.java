package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record StartHeartbeatSoundPacket(int entityId) implements IPacket {
    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> ClientHandler.startHeartbeatSound(this.entityId));
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeVarInt(this.entityId);
    }

    public static StartHeartbeatSoundPacket read(FriendlyByteBuf buf) {
        return new StartHeartbeatSoundPacket(buf.readVarInt());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_CLIENT, StartHeartbeatSoundPacket.class, StartHeartbeatSoundPacket::read);
    }
}