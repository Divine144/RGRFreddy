package dev._100media.rgrfreddy.network.serverbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import dev._100media.rgrfreddy.util.FreddyUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record LeaveControlPacket(boolean leftControl) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                FreddyHolderAttacher.getHolder(player).ifPresent(c -> c.setLeftControl(leftControl));
                Player controlledPlayer = FreddyUtils.getControlledPlayer(player);
                if (controlledPlayer instanceof ServerPlayer serverPlayer) {
                    FreddyHolderAttacher.getHolder(serverPlayer).ifPresent(o -> o.setLeftControl(leftControl));
                }
            }
        });
    }

    @Override
    public void write(FriendlyByteBuf packetBuf) {
        packetBuf.writeBoolean(leftControl);
    }

    public static LeaveControlPacket read(FriendlyByteBuf buf) {
        return new LeaveControlPacket(buf.readBoolean());
    }

    public static void register(SimpleChannel channel, int id) {
        IPacket.register(channel, id, NetworkDirection.PLAY_TO_SERVER, LeaveControlPacket.class, LeaveControlPacket::read);
    }
}
