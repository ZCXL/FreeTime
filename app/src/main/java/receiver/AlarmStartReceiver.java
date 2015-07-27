package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import service.AutoDownLoadIntentService;


/**
 * Created by zhuchao on 7/21/15.
 */
public class AlarmStartReceiver extends BroadcastReceiver {
    //static final String action_alarm="android.intent.action.ACTION_TIME_TICK";
    static final String action_alarm="receiver.alarm.action";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_alarm)){
            Intent AutoIntent=new Intent();
            AutoIntent.setClass(context, AutoDownLoadIntentService.class);
            //启动service
            //多次调用startService并不会启动多个service.而是多次调用onStart
            context.startService(AutoIntent);
        }
    }
}
