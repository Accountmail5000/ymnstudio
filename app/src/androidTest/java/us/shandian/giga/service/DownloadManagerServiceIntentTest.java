package us.shandian.giga.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.os.Parcel;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import org.schabi.newpipe.extractor.MediaFormat;

import us.shandian.giga.get.MissionRecoveryInfo;

@RunWith(AndroidJUnit4.class)
public class DownloadManagerServiceIntentTest {
    @Test
    public void recoveryInfoSurvivesServiceIntentParceling() {
        final ArrayList<MissionRecoveryInfo> recoveryInfo =
                new ArrayList<>();
        recoveryInfo.add(new MissionRecoveryInfo((MediaFormat) null));
        final Intent intent = new Intent();
        DownloadManagerService.putRecoveryInfo(intent, recoveryInfo);

        final Parcel parcel = Parcel.obtain();
        final Intent restoredIntent;
        try {
            intent.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            restoredIntent = Intent.CREATOR.createFromParcel(parcel);
            restoredIntent.setExtrasClassLoader(MissionRecoveryInfo.class.getClassLoader());
        } finally {
            parcel.recycle();
        }

        final ArrayList<MissionRecoveryInfo> restored =
                DownloadManagerService.getRecoveryInfo(restoredIntent);
        assertNotNull(restored);
        assertEquals(1, restored.size());
    }
}
