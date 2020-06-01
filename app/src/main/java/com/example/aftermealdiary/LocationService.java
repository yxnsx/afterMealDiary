package com.example.aftermealdiary;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;

import androidx.annotation.Nullable;

import com.google.android.gms.location.LocationResult;

import java.util.List;


public class LocationService extends IntentService {

    private static final String ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatespendingintent.action" +
                    ".PROCESS_UPDATES";


    public LocationService(String name) { // 파라미터로 전달되는 name은 워커 스레드의 이름일 뿐이며 디버깅 시에만 유용하게 사용됨
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);

                if (result != null) {
                    List<Location> locations = result.getLocations();
                }
            }
        }
    }
}
