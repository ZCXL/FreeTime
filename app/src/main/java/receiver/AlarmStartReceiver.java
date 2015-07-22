package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhuchao.freetime.Welcome;

/**
 * Created by zhuchao on 7/21/15.
 */
public class AlarmStartReceiver extends BroadcastReceiver {
    private int count=0;
    static final String action_alarm="android.intent.action.ACTION_TIME_TICK";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(action_alarm)){
            if(count==0){
                //Toast.makeText(context,String.valueOf(count),Toast.LENGTH_LONG).show();
                Intent intent1=new Intent(context, Welcome.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count++;
                context.startActivity(intent1);
            }
        }
    }
}
