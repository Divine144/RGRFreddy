package dev._100media.rgrfreddy.network.clientbound;

import dev._100media.capabilitysyncer.network.IPacket;
import dev._100media.rgrfreddy.init.SoundInit;
import dev._100media.rgrfreddy.network.ClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

public record PlayJumpscarePacket(int evolutionStage) implements IPacket {

    @Override
    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                SoundEvent sound = switch (evolutionStage) {
                    case 1 -> SoundInit.JUMP_TWO.get();
                    case 2 -> SoundInit.JUMP_THREE.get();
                    case 3 -> SoundInit.JUMP_FOUR.get();
                    case 4 -> SoundInit.JUMP_FIVE.get();
                    default -> SoundInit.JUMP_ONE.get();
                };
                player.level().playSound(player, player.blockPosition(), sound, SoundSource.PLAYERS, 0.65f, 1f);
            }
            ClientHandler.startJumpscareAnimation(evolutionStage);
        });
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
