package org.schabi.newpipe.player.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.support.v4.media.session.PlaybackStateCompat;

import org.junit.Test;

public class MiniPlayerStateTest {
    @Test
    public void activeTrackIsVisibleAndReflectsPlayPauseState() {
        final MiniPlayerState playing = MiniPlayerState.from(
                true, "Video title", PlaybackStateCompat.STATE_PLAYING);
        final MiniPlayerState paused = MiniPlayerState.from(
                true, "Video title", PlaybackStateCompat.STATE_PAUSED);

        assertTrue(playing.isVisible());
        assertTrue(playing.isPlaying());
        assertTrue(paused.isVisible());
        assertFalse(paused.isPlaying());
    }

    @Test
    public void stoppedDisconnectedOrUntitledPlaybackIsHidden() {
        assertFalse(MiniPlayerState.from(
                true, "Video title", PlaybackStateCompat.STATE_STOPPED).isVisible());
        assertFalse(MiniPlayerState.from(
                false, "Video title", PlaybackStateCompat.STATE_PLAYING).isVisible());
        assertFalse(MiniPlayerState.from(
                true, null, PlaybackStateCompat.STATE_PLAYING).isVisible());
    }
}
