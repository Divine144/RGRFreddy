package dev._100media.rgrfreddy.network;

import dev._100media.capabilitysyncer.core.GlobalLevelCapability;
import dev._100media.capabilitysyncer.network.SimpleLevelCapabilityStatusPacket;
import dev._100media.rgrfreddy.RGRFreddy;
import dev._100media.rgrfreddy.cap.FreddyHolderAttacher;
import com.google.common.collect.ImmutableList;
import dev._100media.capabilitysyncer.network.SimpleEntityCapabilityStatusPacket;
import dev._100media.rgrfreddy.cap.GlobalHolderAttacher;
import dev._100media.rgrfreddy.network.clientbound.PlayJumpscarePacket;
import dev._100media.rgrfreddy.network.clientbound.UnboundControlsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;
import java.util.function.BiConsumer;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(RGRFreddy.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;

    public static void register() {
        List<BiConsumer<SimpleChannel, Integer>> packets = ImmutableList.<BiConsumer<SimpleChannel, Integer>>builder()
                .add(SimpleEntityCapabilityStatusPacket::register)
                .add(SimpleLevelCapabilityStatusPacket::register)
                .add(PlayJumpscarePacket::register)
                .add(UnboundControlsPacket::register)
                .build();
        SimpleEntityCapabilityStatusPacket.registerRetriever(FreddyHolderAttacher.LOCATION, FreddyHolderAttacher::getHolderUnwrap);
        SimpleLevelCapabilityStatusPacket.registerRetriever(GlobalHolderAttacher.EXAMPLE_GLOBAL_LEVEL_CAPABILITY_RL, GlobalHolderAttacher::getGlobalLevelCapabilityUnwrap);
        packets.forEach(consumer -> consumer.accept(INSTANCE, getNextId()));
    }

    private static int getNextId() {
        return nextId++;
    }
}