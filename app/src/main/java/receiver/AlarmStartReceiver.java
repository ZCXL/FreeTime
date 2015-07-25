package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import service.AutoDownLoadIntentService;


/**
 * Created by zhuchao on 7/21/15.
 */
public class AlarmStartReceiver extends BroadcastReceiver {
    private int count=0;
    //static final String action_alarm="android.intent.action.ACTION_TIME_TICK";
    static final String action_alarm="receiver.alarm.action";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_alarm)){

                //Toast.makeText(context,String.valueOf(count),Toast.LENGTH_LONG).show();
                Intent intent1=new Intent();
                intent1.setClass(context, AutoDownLoadIntentService.class);
                //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //启动service
                //多次调用startService并不会启动多个service.而是多次调用onStart
                context.startService(intent1);

        }
    }
}
