package service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AutoDownLoadIntentService extends IntentService {
    final static String TAG = "AutoIntentService";

    public AutoDownLoadIntentService() {
        super("AutoDownLoadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
           Log.i(TAG,"begin onHandleIntent() in "+ this);
           try {
               Thread.sleep(10*1000);
           }catch (InterruptedException e){
               e.printStackTrace();
           }
           Log.i(TAG,"end onHandleIntent() in" + this);
        }
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,this+" is destroy");
    }
}
