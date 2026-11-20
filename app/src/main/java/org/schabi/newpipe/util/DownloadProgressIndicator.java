// SPDX-License-Identifier: GPL-3.0-or-later
package org.schabi.newpipe.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.schabi.newpipe.R;

import us.shandian.giga.service.DownloadManagerService;
import us.shandian.giga.service.DownloadManagerService.DownloadManagerBinder;
import us.shandian.giga.service.DownloadProgressCalculator;

public final class DownloadProgressIndicator implements DefaultLifecycleObserver {
    private static final long INITIAL_BIND_DELAY_MS = 750;
    private static final long POLL_INTERVAL_MS = 1000;

    private final Activity activity;
    private final BottomNavigationView navigationView;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean started;
    private boolean bound;
    private DownloadManagerBinder binder;

    private final Runnable bindService = this::bindDownloadManager;

    private void bindDownloadManager() {
        if (started && !bound) {
            bound = activity.bindService(
                    new Intent(activity, DownloadManagerService.class),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
            );
            if (!bound) {
                clearBadge();
            }
        }
    }

    private final Runnable updateProgress = new Runnable() {
        @Override
        public void run() {
            if (!started) {
                return;
            }
            if (binder != null) {
                final int progress = binder.getDownloadManager().getActiveDownloadProgressPercent();
                if (progress == DownloadProgressCalculator.NO_ACTIVE_DOWNLOADS) {
                    clearBadge();
                } else {
                    final BadgeDrawable badge =
                            navigationView.getOrCreateBadge(R.id.main_navigation_downloads);
                    badge.setMaxCharacterCount(4);
                    badge.setNumber(progress);
                    badge.setContentDescriptionQuantityStrings(
                            R.plurals.download_progress_percentage);
                }
            }
            handler.postDelayed(this, POLL_INTERVAL_MS);
        }
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            binder = (DownloadManagerBinder) service;
            handler.removeCallbacks(updateProgress);
            updateProgress.run();
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            binder = null;
            clearBadge();
        }
    };

    public DownloadProgressIndicator(
            final Activity activity,
            final BottomNavigationView navigationView) {
        this.activity = activity;
        this.navigationView = navigationView;
    }

    @Override
    public void onStart(final LifecycleOwner owner) {
        started = true;
        handler.postDelayed(bindService, INITIAL_BIND_DELAY_MS);
    }

    @Override
    public void onStop(final LifecycleOwner owner) {
        started = false;
        handler.removeCallbacks(bindService);
        handler.removeCallbacks(updateProgress);
        binder = null;
        if (bound) {
            activity.unbindService(serviceConnection);
            bound = false;
        }
        clearBadge();
    }

    private void clearBadge() {
        navigationView.removeBadge(R.id.main_navigation_downloads);
    }
}
