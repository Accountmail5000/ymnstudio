package org.schabi.newpipe.player.ui;

import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

public final class MiniPlayerState {
    private final boolean visible;
    private final boolean playing;

    private MiniPlayerState(final boolean visible, final boolean playing) {
        this.visible = visible;
        this.playing = playing;
    }

    @NonNull
    public static MiniPlayerState from(final boolean connected,
                                       final CharSequence title,
                                       final int playbackState) {
        final boolean hasTitle = title != null && title.toString().trim().length() > 0;
        final boolean active = playbackState != PlaybackStateCompat.STATE_NONE
                && playbackState != PlaybackStateCompat.STATE_STOPPED
                && playbackState != PlaybackStateCompat.STATE_ERROR;
        final boolean isPlaying = playbackState == PlaybackStateCompat.STATE_PLAYING
                || playbackState == PlaybackStateCompat.STATE_BUFFERING
                || playbackState == PlaybackStateCompat.STATE_CONNECTING
                || playbackState == PlaybackStateCompat.STATE_FAST_FORWARDING
                || playbackState == PlaybackStateCompat.STATE_REWINDING;
        return new MiniPlayerState(connected && hasTitle && active, isPlaying);
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isPlaying() {
        return playing;
    }
}
