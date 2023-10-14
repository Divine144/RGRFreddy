package dev._100media.rgrfreddy.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.util.thread.BlockableEventLoop;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(BlockableEventLoop.class)
public abstract class BlockableEventLoopMixin implements Executor {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    public abstract String name();

    @Overwrite
    public CompletableFuture<Void> submitAsync(Runnable task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                task.run();
            } catch (Throwable t) {
                // Log since this is usually swallowed
                LOGGER.error(LogUtils.FATAL_MARKER, "Error executing task on {}", this.name(), t);
                // Rethrow so it is caught by the completable future chain (just in case someone cares?)
                throw t;
            }
            return null;
        }, this);
    }
}