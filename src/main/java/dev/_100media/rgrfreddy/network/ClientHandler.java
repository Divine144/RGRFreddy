package dev._100media.rgrfreddy.network;

import dev._100media.rgrfreddy.client.gui.JumpscareOverlay;
import net.minecraft.Util;

public class ClientHandler {

    public static void startJumpscareAnimation() {
        JumpscareOverlay.INSTANCE.setStartTime(Util.getMillis());
        JumpscareOverlay.INSTANCE.setEnabled(true);
    }
}
