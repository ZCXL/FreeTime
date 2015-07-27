package service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


import java.util.LinkedList;

import bean.Movie;
import fragment.ZeroTimeFragment;
import utils.DownLoadFile;

/**
 * Created by zhuchao on 7/23/15.
 */
public class DownloadMovieService extends Service implements DownLoadFile.OnDownloadListener, DownLoadFile.OnErrorListener{
    private IBinder binder;

    private DownLoadFile downLoadFile;

    private LinkedList<Movie>movie_queue;

    private LinkedList<String>movie_tag;

    private boolean isDownloading=false;

    private String tag;
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

        movie_queue=new LinkedList<Movie>();
        movie_tag=new LinkedList<String>();
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
    public void addTask(Movie m,String tag){
        if(m!=null) {
            for(int i=0;i<movie_queue.size();i++){
                if(m.getMovieId().equals(movie_queue.get(i).getMovieId())){
                    return;
                }
            }
            movie_queue.add(m);
            movie_tag.add(tag);
            if(isDownloading)
                notifyWait(tag);
            startDownloadTsk();
        }
    }
    public void startDownloadTsk(){
        if(!isDownloading){
            if(movie_queue.size() >0){
                Movie movie=movie_queue.poll();
                tag=movie_tag.poll();
                isDownloading=true;
                downLoadFile.startDownload(movie.getFileUrl());
                Log.d("tell me why", "doubi");
            }
        }
    }

    @Override
    public void onStart() {
        notifyStart();
        isDownloading=true;
    }

    @Override
    public void onValueChange(float value) {
        notifyPercent(value);
    }

    @Override
    public void onSpeedChange(int speed) {
        notifySpeed(speed);
    }

    @Override
    public void onDownloadSuccess() {
        notifyCompleted();
        isDownloading=false;
    }

    @Override
    public void onFileSize(long file_size) {
        notifyFileSize(file_size/1000000);
    }

    @Override
    public void onSizeError() {
        notifyError();
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onStreamError() {
        notifyError();
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onSDCardError() {
        notifyError();
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onDownloadError() {
        notifyError();
        isDownloading=false;
        startDownloadTsk();
    }

    private void notifyStart(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_START);
        intent.putExtra("isStart",true);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyCompleted(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_COMPLETED);
        intent.putExtra("isCompleted",true);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
        startDownloadTsk();
    }
    private void notifyError(){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_ERROR);
        intent.putExtra("isError",true);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifySpeed(int speed){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_SPEED);
        intent.putExtra("speed",speed);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyPercent(float value){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_PERCENT);
        intent.putExtra("percent",value);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyFileSize(long file_size){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_FILE_SIZE);
        intent.putExtra("file_size",file_size);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
    private void notifyWait(String tag){
        Intent intent=new Intent(ZeroTimeFragment.ACTION_WAIT);
        intent.putExtra("tag",tag);
        DownloadMovieService.this.sendBroadcast(intent);
    }
}
