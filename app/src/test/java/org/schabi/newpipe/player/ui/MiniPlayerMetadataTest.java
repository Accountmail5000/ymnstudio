package org.schabi.newpipe.player.ui;

import static org.junit.Assert.assertEquals;

import android.support.v4.media.MediaMetadataCompat;

import org.junit.Test;

public class MiniPlayerMetadataTest {
    @Test
    public void displayTitleTakesPrecedenceOverMediaTitle() {
        final MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, "Display title")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "Media title")
                .build();

        assertEquals("Display title", MiniPlayerMetadata.titleOrDefault(metadata, "Now playing"));
    }

    @Test
    public void missingTitleUsesFallbackInsteadOfLeavingPlayerBlank() {
        assertEquals("Now playing", MiniPlayerMetadata.titleOrDefault(null, "Now playing"));
    }

    @Test
    public void displaySubtitleTakesPrecedenceOverArtist() {
        final MediaMetadataCompat metadata = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, "Display creator")
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "Artist")
                .build();

        assertEquals("Display creator", MiniPlayerMetadata.creator(metadata));
    }
}
