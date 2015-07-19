package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zhuchao.freetime.MainActivity;

/**
 * @author zhcuhao
 */
public class DownloadReceiver extends BroadcastReceiver {
    @SuppressWarnings("unused")
    private MainActivity context;
    public  final static String ACTION_UPDATE = "service_broadcast.UPDATE";
    public  final static String ACTION_FINISHED = "service_broadcast.FINISHED";
    public  final static String ACTION_FAILED="service_broadcast.FAILED";
    public DownloadReceiver(Context context){
        this.context=(MainActivity)context;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_UPDATE)){
            boolean isUpdate=intent.getBooleanExtra("StartUpdate", false);
            if(isUpdate)
                Toast.makeText(context,"Downloading",Toast.LENGTH_LONG).show();
        }else if(intent.getAction().equals(ACTION_FINISHED)){
            boolean isFinished=intent.getBooleanExtra("HadFinished", false);
            if(isFinished)
                Toast.makeText(context,"Download finished",Toast.LENGTH_SHORT).show();
        }else if(intent.getAction().equals(ACTION_FAILED)){
            boolean isFailed=intent.getBooleanExtra("Failed", false);
            if(isFailed)
                Toast.makeText(context,"Download failed",Toast.LENGTH_SHORT).show();
        }
    }
}