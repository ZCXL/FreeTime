package service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by zhuchao on 7/14/15.
 */
public class DownloadService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void startDownload(String url){

    }
}
