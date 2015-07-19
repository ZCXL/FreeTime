package connection;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.zhuchao.freetime.MainActivity;

import service.DownloadService;

public class DownloadServiceConnection implements ServiceConnection {
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		// TODO Auto-generated method stub
		MainActivity.downloadService=((DownloadService.DownloadServiceBinder)service).getService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		// TODO Auto-generated method stub
       MainActivity.downloadService=null;
	}

}
