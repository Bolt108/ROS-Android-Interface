package com.schneewittchen.rosandroid;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapxus.map.mapxusmap.api.map.MapxusMapContext;

public class App extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MapxusMapContext.init(getApplicationContext());
        Mapbox.getInstance(getApplicationContext(), "/Iy3eV0lc");
    }
}
