package receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by zhuchao on 7/21/15.
 */
public class BootStartReceiver extends BroadcastReceiver {
    private static String TAG="BootStartReceiver";
    static final String action_boot="android.intent.action.BOOT_COMPLETED";
    static final String action_alarm="receiver.alarm.action";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_boot)){
            //启动完成
            Intent intent1=new Intent(context, AlarmStartReceiver.class);
            intent1.setAction(action_alarm);
            //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent sender = PendingIntent.getBroadcast(context,0,intent1,0);
            // 从开机到现在的毫秒数（手机睡眠(sleep)的时间也包括在内）
            Long firstTime = SystemClock.elapsedRealtime();
            Log.i(TAG,"--firstTime--"+firstTime.toString());
            AlarmManager am = (AlarmManager)context.getSystemService(context.ALARM_SERVICE);
            //10秒一个周期，不停的发送广播
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,firstTime,10*1000,sender);
        }
    }
}
