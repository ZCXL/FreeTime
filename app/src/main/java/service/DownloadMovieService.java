package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import fragment.ZeroTimeFragment;
import utils.DownLoadFile;

/**
 * Created by zhuchao on 7/23/15.
 */
public class DownloadMovieService extends Service implements DownLoadFile.OnDownloadListener, DownLoadFile.OnErrorListener{
    private IBinder binder;

    private String tasks;

    private DownLoadFile downLoadFile;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        binder=new DownloadMovieServiceBinder();
        downLoadFile=new DownLoadFile(getApplicationContext());
        downLoadFile.setDownloadListener(this);
        downLoadFile.setErrorListener(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        binder=null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return START_STICKY;
    }


    public class DownloadMovieServiceBinder extends Binder{
        public DownloadMovieService getService(){
            return DownloadMovieService.this;
        }
    }

    public void addTask(String url){
        this.tasks=url;
        startDownloadTsk();
    }

    public void startDownloadTsk(){
        downLoadFile.startDownload(tasks);
    }

    @Override
    public void onStart() {
        notifyStart();
    }

    @Override
    public void onValueChange(float value) {
        Log.d("tell me why",String.valueOf(value));
        notifyPercent(value);
    }

    @Override
    public void onSpeedChange(int speed) {
        notifySpeed(speed);
    }

    @Override
    public void onDownloadSuccess() {
        notifyCompleted();
    }

    @Override
    public void onFileSize(long file_size) {
        notifyFileSize(file_size);
    }

    @Override
    public void onSizeError() {
        notifyError();
    }

    @Override
    public void onStreamError() {
        notifyError();
    }

    @Override
    public void onSDCardError() {
        notifyError();
    }

    @Override
    public void onDownloadError() {
        notifyError();
    }

    private void notifyStart(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_START);
        intent.putExtra("isStart",true);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyCompleted(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_COMPLETED);
        intent.putExtra("isCompleted",true);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyError(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_ERROR);
        intent.putExtra("isError",true);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifySpeed(int speed){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_SPEED);
        intent.putExtra("speed",speed);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyPercent(float value){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_PERCENT);
        intent.putExtra("percent",value);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyFileSize(long file_size){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_FILE_SIZE);
        intent.putExtra("file_size",file_size);
        DownloadMovieService.this.sendBroadcast(intent);
    }
}
