package br.com.fabiano.snapdark.ifkeys.utils.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;

/**
 * Created by Fabiano on 16/09/2017.
 */

public class StaticValues {
    private ContentResolver contentResolver;
    private Handler durationHandler;
    private OkHttpClient okHttpClient;
    private WifiManager.WifiLock wifiLock;
    private static StaticValues instance;


    public synchronized static StaticValues getInstance() {
        if (instance == null) {
            instance = new StaticValues ();
        }
        return instance;
    }

    public ContentResolver getContentResolver(Context activity){

        if (contentResolver == null){
            contentResolver = activity.getContentResolver();
        }
        return contentResolver;
    }

    public Handler getDurationHandler() {
        if (durationHandler == null){
             durationHandler = new Handler();
        }
        return durationHandler;
    }


    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
    public WifiManager.WifiLock getWifiLock(Context context){
        if(wifiLock == null){
            wifiLock = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                    .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
            wifiLock.acquire();
        }
        return wifiLock;
    }
}
