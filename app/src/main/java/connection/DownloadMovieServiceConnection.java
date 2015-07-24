package connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.zhuchao.freetime.MainActivity;

import service.DownloadMovieService;

/**
 * Created by zhuchao on 7/23/15.
 */
public class DownloadMovieServiceConnection implements ServiceConnection {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MainActivity.downloadMovieService=((DownloadMovieService.DownloadMovieServiceBinder)service).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        MainActivity.downloadMovieService=null;
    }
}
