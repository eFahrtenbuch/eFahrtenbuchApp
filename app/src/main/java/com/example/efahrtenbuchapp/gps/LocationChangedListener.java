package com.example.efahrtenbuchapp.gps;

import android.location.LocationListener;
import android.os.Bundle;

public interface LocationChangedListener extends LocationListener {
    public default void onStatusChanged(String provider, int status, Bundle extras) {}
    public default void onProviderEnabled(String provider){/*Nothing to do*/}
    public default void onProviderDisabled(String provider){/*Nothing to do*/}
}
