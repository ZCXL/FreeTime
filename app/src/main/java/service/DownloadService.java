package service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import function.ImageProcess;
import function.Network;
import function.NetworkFunction;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import com.zhuchao.freetime.MainActivity;
import com.zhuchao.freetime.R;

public class DownloadService extends Service {

    private  IBinder binder;
    private String downloadUrl;
    private NotificationManager notificationManager;
    private Notification notification;
    private RemoteViews remoteView;
    private int fileSize;
    private int downloadFileSize;
    private int DownloadState;
    private final int MUST_REFRESH=0;
    private final int CAN_REFRESH=1;
    private final static String ACTION_UPDATE = "service_broadcast.UPDATE";
    private final static String ACTION_FINISHED = "service_broadcast.FINISHED";
    private final static String ACTION_FAILED="service_broadcast.FAILED";
    private static final int DOWNLOAD_STATUS_UPDATE = 0;
    private static final int DOWNLOAD_STATUS_SUCCESS = 1;
    private static final int DOWNLOAD_STATUS_FAILED = 2;
    private static final String TAG="DownloadService";
    Handler handler=new Handler(){
        @SuppressLint("SdCardPath")
        @SuppressWarnings("deprecation")
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case DOWNLOAD_STATUS_UPDATE:
                    notification.icon= R.drawable.logo;
                    notification.when=System.currentTimeMillis();
                    notification.tickerText="Start download...";
                    notification.flags=Notification.FLAG_ONGOING_EVENT;

                    remoteView.setImageViewResource(R.id.ivIcon, R.drawable.logo);
                    remoteView.setTextViewText(R.id.tvName, "FreeTime");
                    int progress=Integer.parseInt(String.valueOf(msg.obj));
                    remoteView.setProgressBar(R.id.pbProgress,100,(progress*100)/fileSize,false);
                    remoteView.setTextViewText(R.id.tvProgress,String.valueOf((progress*100)/fileSize)+"%");
                    // Log.d(TAG,String.valueOf());

                    notification.contentView=remoteView;
                    notificationManager.notify(0, notification);
                    break;
                case DOWNLOAD_STATUS_SUCCESS:
                    notifyFinished(true);
                    notification.flags=Notification.FLAG_AUTO_CANCEL;
                    notification.contentView=null;
                    Intent intent=new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File("/sdcard/FreeTime/" + downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1))), "application/vnd.android.package-archive");
                    PendingIntent pendingIntent=PendingIntent.getActivity(DownloadService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setLatestEventInfo(DownloadService.this,"Download", "Download successfully!", pendingIntent);
                    notificationManager.notify(0, notification);

                    break;
                case DOWNLOAD_STATUS_FAILED:
                    notification.flags=Notification.FLAG_AUTO_CANCEL;
                    notification.contentView=null;
                    Intent intent1=new Intent(DownloadService.this,MainActivity.class);
                    PendingIntent pendingIntent1=PendingIntent.getActivity(DownloadService.this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    notification.setLatestEventInfo(DownloadService.this,"Download", "Download failed", pendingIntent1);
                    notificationManager.notify(0, notification);
                    break;
            }
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return binder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        binder=new DownloadServiceBinder();
        notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notification=new Notification();
        remoteView=new RemoteViews(this.getPackageName(), R.layout.remote_view_layout);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        binder=null;
        notificationManager=null;
        notification=null;
        remoteView=null;
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return START_STICKY;
    }
    public class DownloadServiceBinder extends Binder{
        public DownloadService getService(){
            return DownloadService.this;
        }
    }
    public void addDownloadTask(String url,int flag){
        downloadUrl=url;
        startDownloadTask(flag);
    }
    private void startDownloadTask(int flag){
        if(flag==CAN_REFRESH){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(Network.checkNetWorkState(DownloadService.this)){
                        try {
                            notifyUpdate(true);
                            DownloadState=DOWNLOAD_STATUS_UPDATE;
                            NetworkFunction.FileInfo fileInfo= NetworkFunction.GetFile(downloadUrl);
                            InputStream inputStream=fileInfo.inputStream;
                            Log.d(TAG,"Start to download");
                            fileSize=fileInfo.fileSize;
                            if(fileSize<0){notifyFailed(true);throw new RuntimeException("get file size error");}
                            if(inputStream==null){notifyFailed(true);throw new RuntimeException("get input stream error");}
                            if(ImageProcess.isSDExit()){
                                String pathName="sdcard/FreeTime/";
                                File path=new File(pathName);
                                File file=new File(pathName+downloadUrl.substring(downloadUrl.lastIndexOf("/")+1));
                                if(!path.exists())
                                    path.mkdir();
                                if(!file.exists())
                                    file.createNewFile();
                                FileOutputStream fos=new FileOutputStream(file);
                                byte buf[]=new byte[1024];
                                downloadFileSize=0;
                                int onePercent=fileSize/100;
                                int nowPercent=onePercent;
                                while(downloadFileSize<fileSize&&DownloadState==DOWNLOAD_STATUS_UPDATE){
                                    if(downloadFileSize>nowPercent){
                                        Message message=handler.obtainMessage(DOWNLOAD_STATUS_UPDATE,downloadFileSize);
                                        handler.sendMessage(message);
                                        nowPercent+=onePercent;
                                    }
                                    //Log.d(TAG,String.valueOf(downloadFileSize));
                                    int numRead=inputStream.read(buf);
                                    if(numRead==-1){
                                        break;
                                    }
                                    fos.write(buf,0,numRead);
                                    downloadFileSize+=numRead;
                                }
                                fos.close();
                                inputStream.close();
                                notifyFinished(true);
                                Message message=new Message();
                                message.what=DOWNLOAD_STATUS_SUCCESS;;
                                handler.sendMessage(message);
                            }else{
                                notifyFailed(true);
                                throw new RuntimeException("download error");
                            }
                        } catch (Exception e) {
                            Log.d(TAG,"error:"+e.toString());
                            notifyFailed(true);
                            Message message=new Message();
                            message.what=DOWNLOAD_STATUS_FAILED;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }else if(flag==MUST_REFRESH){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(Network.checkNetWorkState(DownloadService.this)){
                        Log.d(TAG,"Start to download");
                        try {
                            DownloadState=DOWNLOAD_STATUS_UPDATE;
                            NetworkFunction.FileInfo fileInfo=NetworkFunction.GetFile(downloadUrl);
                            InputStream inputStream=fileInfo.inputStream;
                            fileSize=fileInfo.fileSize;
                            if(fileSize<0){notifyFailed(true);throw new RuntimeException("get file size error");}
                            if(inputStream==null){notifyFailed(true);throw new RuntimeException("get input stream error");}
                            if(ImageProcess.isSDExit()){
                                String pathName="sdcard/FreeTime/";
                                File path=new File(pathName);
                                File file=new File(pathName+downloadUrl.substring(downloadUrl.lastIndexOf("/")+1));
                                if(!path.exists())
                                    path.mkdir();
                                if(!file.exists())
                                    file.createNewFile();
                                FileOutputStream fos=new FileOutputStream(file);
                                byte buf[]=new byte[1024];
                                downloadFileSize=0;
                                int onePercent=fileSize/100;
                                int nowPercent=onePercent;
                                while(downloadFileSize<fileSize&&DownloadState==DOWNLOAD_STATUS_UPDATE){
                                    if(downloadFileSize>nowPercent){
                                        Message message=handler.obtainMessage(DOWNLOAD_STATUS_UPDATE,downloadFileSize);
                                        handler.sendMessage(message);
                                        nowPercent+=onePercent;
                                    }
                                    //Log.d(TAG,String.valueOf(downloadFileSize));
                                    int numRead=inputStream.read(buf);
                                    if(numRead==-1){
                                        break;
                                    }
                                    fos.write(buf,0,numRead);
                                    downloadFileSize+=numRead;
                                }
                                fos.close();
                                inputStream.close();
                                Message message=new Message();
                                message.what=DOWNLOAD_STATUS_SUCCESS;;
                                handler.sendMessage(message);
                            }else{
                                throw new RuntimeException("download error");
                            }
                        } catch (Exception e) {
                            Log.d(TAG,"error:"+e.toString());
                            Message message=new Message();
                            message.what=DOWNLOAD_STATUS_FAILED;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }
    }

    public void notifyUpdate(boolean isUpdate){
        Intent intent=new Intent(ACTION_UPDATE);
        intent.putExtra("StartUpdate",isUpdate);
        DownloadService.this.sendBroadcast(intent);
    }
    public void notifyFinished(boolean isFinished){
        Intent intent=new Intent(ACTION_FINISHED);
        intent.putExtra("HadFinished",isFinished);
        DownloadService.this.sendBroadcast(intent);
    }
    public void notifyFailed(boolean isFailed){
        Intent intent=new Intent(ACTION_FAILED);
        intent.putExtra("Failed",isFailed);
        DownloadService.this.sendBroadcast(intent);
    }
}
