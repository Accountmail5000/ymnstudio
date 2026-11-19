/*
 * SPDX-FileCopyrightText: 2026 YmnTube contributors
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package org.schabi.newpipe.download;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;

import org.schabi.newpipe.R;

public class DownloadPlayerActivity extends AppCompatActivity {
    private static final String TAG = "DownloadPlayerActivity";

    private Uri mediaUri;
    private PlayerView playerView;
    private long playbackPosition;
    @Nullable
    private ExoPlayer player;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_player);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra(Intent.EXTRA_TITLE));
        }

        mediaUri = getIntent().getData();
        if (mediaUri == null) {
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        playerView = findViewById(R.id.download_player_view);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mediaUri != null) {
            initializePlayer();
        }
    }

    @Override
    protected void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializePlayer() {
        if (player != null) {
            return;
        }

        try {
            player = new ExoPlayer.Builder(this).build();
            playerView.setPlayer(player);
            player.addListener(new Player.Listener() {
                @Override
                public void onPlayerError(final PlaybackException error) {
                    Log.e(TAG, "Unable to play downloaded media", error);
                    Toast.makeText(DownloadPlayerActivity.this, R.string.general_error,
                            Toast.LENGTH_LONG).show();
                }
            });
            final String title = getIntent().getStringExtra(Intent.EXTRA_TITLE);
            final MediaItem mediaItem = new MediaItem.Builder()
                    .setUri(mediaUri)
                    .setMimeType(getIntent().getType())
                    .setMediaMetadata(new MediaMetadata.Builder().setTitle(title).build())
                    .build();
            player.setMediaItem(mediaItem);
            if (playbackPosition > 0) {
                player.seekTo(playbackPosition);
            }
            player.prepare();
            player.play();
        } catch (final RuntimeException e) {
            Log.e(TAG, "Unable to initialize downloaded media player", e);
            Toast.makeText(this, R.string.general_error, Toast.LENGTH_LONG).show();
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            playerView.setPlayer(null);
            player.release();
            player = null;
        }
    }
}
