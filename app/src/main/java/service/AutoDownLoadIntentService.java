package service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;

import bean.BaseObject;
import bean.Movie;
import bean.Movies;
import bean.UserInfo;
import function.Network;
import function.NetworkFunction;
import function.SaveAndOpenMovies;
import function.SaveAndOpenUserInfo;
import utils.DownLoadFile;

/**
 * autoDownLoad movies
 * Created by LMZ 7/26/15
 */
public class AutoDownLoadIntentService extends IntentService implements DownLoadFile.OnDownloadListener,DownLoadFile.OnErrorListener {
    private final static String TAG = "AutoIntentService";

    private DownLoadFile downLoadFile;

    private LinkedList<Movie> movie_queue;

    private String number;

    private Movies movieForInstance;

    private Movie movie;

    private SaveAndOpenMovies saveAndOpenMovies;
    private SaveAndOpenUserInfo saveAndOpenUserInfo;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(movie_queue.size()>0){
                        movie=movie_queue.poll();
                        if(movie!=null)
                            startDownloadTsk(movie);
                    }
                    break;
            }
        }
    };
    public AutoDownLoadIntentService() {
        super("AutoDownLoadIntentService");
        saveAndOpenUserInfo = new SaveAndOpenUserInfo();
        downLoadFile = new DownLoadFile(getApplicationContext());
        downLoadFile.setDownloadListener(this);
        downLoadFile.setErrorListener(this);
        movie_queue = new LinkedList<Movie>();
        saveAndOpenMovies = new SaveAndOpenMovies();
        movieForInstance = new Movies(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            if (Network.getNetworkType(this) == 1) {
                //get movies' count
                int count = movieForInstance.getCount();

                ArrayList<BaseObject> users = saveAndOpenUserInfo.Open(this);
                if (users == null) {
                    number = "1";
                } else {
                    UserInfo userInfo = (UserInfo) users.get(0);
                    number = userInfo.getNumber();
                }
                if(count<3){
                    new Thread(new GetNewMovie(String.valueOf(number))).start();
                }
            } else {
                Log.i(TAG, "has no wifi");
            }
        }
    }

    private void startDownloadTsk(Movie m) {
        if(m!=null)
            downLoadFile.startDownload(m.getFileUrl());
    }
    /**
     * save movie
     */
    private void saveMovie(){
        ArrayList<BaseObject> arrayList=new ArrayList<BaseObject>();
        arrayList.add(movie);
        for(int i=0;i<movieForInstance.getCount();i++)
            arrayList.add(movieForInstance.getItem(i));
        saveAndOpenMovies.Save(this,arrayList);
    }


    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,this+" is destroy");
    }

    @Override
    public void onStart() {
        saveMovie();
    }

    @Override
    public void onValueChange(float value) {

    }

    @Override
    public void onSpeedChange(int speed) {

    }

    @Override
    public void onDownloadSuccess() {

    }


    @Override
    public void onFileSize(long file_size) {

    }

    @Override
    public void onSizeError() {

    }

    @Override
    public void onStreamError() {

    }

    @Override
    public void onSDCardError() {

    }

    @Override
    public void onDownloadError() {

    }
    public class GetNewMovie implements Runnable{
        private String number;
        public GetNewMovie(String number){
            this.number=number;
        }
        @Override
        public void run() {
            String keys[]={"number"};
            String parameters[]={number};
            String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_customer_movie.php",keys,parameters);
            if(result!=null&&!result.contains("error")){
                Movie movie=new Movie(result);
                movie_queue.add(movie);
                mHandler.sendEmptyMessage(0);
            }
        }
    }
}
