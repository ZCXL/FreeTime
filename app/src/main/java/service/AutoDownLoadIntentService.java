package service;

import android.app.IntentService;
import android.content.Intent;
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

    private boolean isDownloading=false;

    private String number;

    public static ArrayList<Movie>movies=new ArrayList<Movie>();

    private SaveAndOpenMovies saveAndOpenMovies;
    private SaveAndOpenUserInfo saveAndOpenUserInfo;

    public AutoDownLoadIntentService() {
        super("AutoDownLoadIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.i(TAG, "begin onHandleIntent() in " + this);
            if (Network.getNetworkType(this) == 1) {
                //get movies' count
                Movies movieForInstance = new Movies(this);
                int count = movieForInstance.getCount();

                saveAndOpenUserInfo = new SaveAndOpenUserInfo();
                ArrayList<BaseObject> users = saveAndOpenUserInfo.Open(this);
                if (users == null) {
                    number = "1";
                } else {
                    UserInfo userInfo = (UserInfo) users.get(0);
                    number = userInfo.getNumber();
                }
                Log.i(TAG, "--userInfo--" + number);


                downLoadFile = new DownLoadFile(getApplicationContext());
                downLoadFile.setDownloadListener(this);
                downLoadFile.setErrorListener(this);
                movie_queue = new LinkedList<Movie>();
                saveAndOpenMovies = new SaveAndOpenMovies();

                Log.i(TAG, "movies' size: " + count);
                if (count == 3) {
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "has enough movies");
                } else if (count == -1) {
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "no movies");
                    String keys[] = {"number"};
                    String parameters[] = {number};
                    String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_customer_movie.php", keys, parameters);
                    if (result != null && !result.contains("error")) {
                        Log.i(TAG, result.toString());
                        movie_queue.add(new Movie(result));
                    }
                    Movie movie = movie_queue.poll();
                    Log.i(TAG, movie.getDescription());
                    movies.add(movie);
                    Log.i(TAG, "--start download task--");
                    addTask(movie);
                    Log.i(TAG, "--finished download task--");
                    saveMovie();
                } else {
                    if (count < 3) {
                        String keys[] = {"number"};
                        String parameters[] = {number};
                        Log.i(TAG, "--get movie's FileUrl--");
                        String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_customer_movie.php", keys, parameters);
                        if (result != null && !result.contains("error")) {
                            Log.i(TAG, result.toString());
                            movie_queue.add(new Movie(result));
                           /*Movie movie = new Movie(result);
                            String t = movie.getFileUrl();
                            Log.i(TAG, "movie's FileUrl" + t);
                            downLoadFile.startDownload(t);*/
                        }
                        Movie movie=movie_queue.poll();
                        movies.add(movie);
                        Log.i(TAG, "--start download task--");
                        addTask(movie);
                        Log.i(TAG, "--finished download task--");
                        saveMovie();
                    }

                }
            } else {
                Log.i(TAG, "has no wifi");
            }
        }
        Log.i(TAG, "end onHandleIntent() in" + this);
    }

    public void addTask(Movie m){
        if(m!=null) {
            movie_queue.add(m);
            if(isDownloading) {
                startDownloadTsk();
            }else{
                Log.i(TAG, "isDownloading is null");
            }
        }
    }
    private void startDownloadTsk() {
        if(!isDownloading){
            if(movie_queue.size() >0){
                Movie movie=movie_queue.poll();
                isDownloading=true;
                Log.d(TAG,movie.toString());
                downLoadFile.startDownload(movie.getFileUrl());
            }
        }else {
            Log.i(TAG,"--start work failed--");
        }
    }
    /**
     * save movie
     */
    private void saveMovie(){
        ArrayList<BaseObject> arrayList=new ArrayList<BaseObject>();
        for(BaseObject object:movies){
            arrayList.add(object);
        }
        saveAndOpenMovies.Save(this,arrayList);
    }


    public void onDestroy(){
        super.onDestroy();
        Log.i(TAG,this+" is destroy");
    }

    @Override
    public void onStart() {
        isDownloading=true;
    }

    @Override
    public void onValueChange(float value) {

    }

    @Override
    public void onSpeedChange(int speed) {

    }

    @Override
    public void onDownloadSuccess() {
        isDownloading=false;
    }


    @Override
    public void onFileSize(long file_size) {

    }

    @Override
    public void onSizeError() {
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onStreamError() {
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onSDCardError() {
        isDownloading=false;
        startDownloadTsk();
    }

    @Override
    public void onDownloadError() {
        isDownloading=false;
        startDownloadTsk();
    }
}
