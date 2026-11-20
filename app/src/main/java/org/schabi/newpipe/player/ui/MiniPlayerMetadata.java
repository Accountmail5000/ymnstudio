package org.schabi.newpipe.player.ui;

import android.graphics.Bitmap;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.Nullable;

final class MiniPlayerMetadata {
    private MiniPlayerMetadata() {
    }

    static CharSequence titleOrDefault(@Nullable final MediaMetadataCompat metadata,
                                       final CharSequence defaultTitle) {
        final CharSequence displayTitle = getNonEmptyText(
                metadata, MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE);
        if (displayTitle != null) {
            return displayTitle;
        }

        final CharSequence title = getNonEmptyText(
                metadata, MediaMetadataCompat.METADATA_KEY_TITLE);
        return title == null ? defaultTitle : title;
    }

    @Nullable
    static CharSequence creator(@Nullable final MediaMetadataCompat metadata) {
        final CharSequence displaySubtitle = getNonEmptyText(
                metadata, MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE);
        if (displaySubtitle != null) {
            return displaySubtitle;
        }

        final CharSequence artist = getNonEmptyText(
                metadata, MediaMetadataCompat.METADATA_KEY_ARTIST);
        return artist != null ? artist : getNonEmptyText(
                metadata, MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST);
    }

    @Nullable
    static Bitmap artwork(@Nullable final MediaMetadataCompat metadata) {
        if (metadata == null) {
            return null;
        }
        final Bitmap albumArt = metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART);
        return albumArt != null ? albumArt
                : metadata.getBitmap(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON);
    }

    @Nullable
    private static CharSequence getNonEmptyText(@Nullable final MediaMetadataCompat metadata,
                                               final String key) {
        if (metadata == null) {
            return null;
        }
        final CharSequence value = metadata.getText(key);
        return value == null || value.toString().trim().isEmpty() ? null : value;
    }
}
