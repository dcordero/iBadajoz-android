package com.dcordero.ibadajoz;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class BadajozBusApplication extends Application {

    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        BadajozBusApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return BadajozBusApplication.context;
    }
}
