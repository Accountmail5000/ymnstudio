package org.schabi.newpipe.player.ui;

public final class MiniPlayerDismissal {
    private MiniPlayerDismissal() {
    }

    private static boolean dismissed;

    public static void dismiss() {
        dismissed = true;
    }

    public static void restoreForNewPlayback() {
        dismissed = false;
    }

    public static boolean isDismissed() {
        return dismissed;
    }
}
