package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.freetime.CommentActivity;
import com.zhuchao.freetime.MainActivity;
import com.zhuchao.freetime.PlayMovie;
import com.zhuchao.freetime.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;

import adapter.ViewPagerAdapter;
import bean.BaseObject;
import bean.Movie;
import function.LoginNotification;
import function.Network;
import function.NetworkFunction;
import function.SaveAndOpenMovies;
import utils.ImageLoader;
import view_rewrite.DownloadCircle;
import view_rewrite.RippleImage;

/**
 * Created by zhuchao on 7/13/15.
 */
public class ZeroTimeFragment extends Fragment implements Runnable,ViewPager.OnPageChangeListener,View.OnClickListener{
    public final static String ACTION_START="download_broadcast.START";
    public final static String ACTION_SPEED="download_broadcast.SPEED";
    public final static String ACTION_PERCENT="download_broadcast.PERCENT";
    public final static String ACTION_ERROR="download_broadcast.ERROR";
    public final static String ACTION_COMPLETED="download_broadcast.COMPLETED";
    public final static String ACTION_FILE_SIZE="download_broadcast.FILE_SIZE";
    public final static String ACTION_WAIT="download_broadcast.WAIT";

    private ViewPager moviePager;

    private ViewPagerAdapter adapter;

    private ArrayList<View> movieViews;

    private TextView movie_description;

    private TextView movie_time;

    private ImageView movie_comment;

    private RippleImage collect;

    private RippleImage share;

    private RippleImage delete;

    private View view1,view2,view3;

    private DownloadCircle downloadCircle1,downloadCircle2,downloadCircle3;

    private ImageView movie_image1,movie_image2,movie_image3;

    private RelativeLayout movie_background1,movie_background2,movie_background3;

    private DownloadMessageReceiver downloadMessageReceiver;

    public static ArrayList<Movie>movies=new ArrayList<Movie>();

    private ArrayList<Movie>tempMovies=new ArrayList<Movie>();

    private SaveAndOpenMovies saveAndOpenMovies;

    private String filesize[]={"","",""};

    private boolean collectstate[]={false,false,false};

    private Movie currentMovie;

    private ImageLoader imageLoader;

    private int position=0;

    private LinkedList<Movie>movieQueue;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(movies.size()<3){
                        if(Network.checkNetWorkState(getActivity())&&Network.getNetworkType(getActivity())==Network.NETWORNTYPE_WIFI){
                            new Thread(ZeroTimeFragment.this).start();
                        }
                    }
                    break;
                case 1:
                    if(movieQueue.size()>0){
                        Movie movie=movieQueue.poll();
                        for(int i=0;i<movies.size();i++){
                            if(movie.getMovieId().equals(movies.get(i).getMovieId())){
                                this.sendEmptyMessage(0);
                                return;
                            }
                        }
                        movies.add(movie);
                        if(position==0) {
                            setMoviePage(0);
                            currentMovie=movie;
                        }
                        sendEmptyMessage(2);
                        //while(symbol<0);
                        MainActivity.downloadMovieService.addTask(movie,String.valueOf(movies.size()-1));
                    }
                    this.sendEmptyMessage(0);
                    break;
                case 2:
                    /**
                     * check collect state
                     */
                    checkCollectState(movies.size()-1);
                    break;
                case 4:
                    if(collectstate[position])
                        collect.setImageResource(R.drawable.main_collect_button_checked);
                    else
                        collect.setImageResource(R.drawable.main_collect_button);
                    Toast.makeText(getActivity(),"Collect successfully",Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    int position=Integer.parseInt(String.valueOf(msg.obj));
                    if(Network.checkNetWorkState(getActivity())&&Network.getNetworkType(getActivity())==Network.NETWORNTYPE_WIFI){
                        new Thread(new getNewMovie(position)).start();
                    }
                    break;
                case 6:
                    position=Integer.parseInt(String.valueOf(msg.obj));
                    if(movieQueue.size()>0){
                        Movie movie=movieQueue.poll();
                        for(int i=0;i<movies.size();i++){
                            if(movie.getMovieId().equals(movies.get(i).getMovieId())){
                                Message message=new Message();
                                message.obj=position;
                                message.what=5;
                                this.sendMessage(message);
                                return;
                            }
                        }
                        movies.set(position,movie);
                        if(position==ZeroTimeFragment.this.position)
                            setMoviePage(position);
                        checkCollectState(position);
                        //while(symbol<0);
                        Log.d("position",String.valueOf(position));
                        MainActivity.downloadMovieService.addTask(movie,String.valueOf(position));
                    }
                    break;
                case 7:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_zero_time,container,false);

        initView(rootView);

        for(int i = 0; i < movies.size()&&i<3;i++) {
            tempMovies.add(movies.get(i));
            if(i==0) {
                downloadCircle1.endDownload();
                currentMovie=movies.get(0);
            }if(i==1)
                downloadCircle2.endDownload();
            if(i==2)
                downloadCircle3.endDownload();
            checkCollectState(i);
        }

        mHandler.sendEmptyMessage(0);

        return rootView;
    }

    private void initView(View rootView){
        movieQueue=new LinkedList<Movie>();

        saveAndOpenMovies=new SaveAndOpenMovies();

        imageLoader=new ImageLoader(getActivity());

        moviePager=(ViewPager)rootView.findViewById(R.id.main_zero_time_movie_image_container);

        movie_description=(TextView)rootView.findViewById(R.id.main_zero_time_movie_description);

        movie_time=(TextView)rootView.findViewById(R.id.main_zero_time_movie_size);

        movie_comment=(ImageView)rootView.findViewById(R.id.main_zero_time_movie_comment);

        share=(RippleImage)rootView.findViewById(R.id.main_zero_time_share);

        collect=(RippleImage)rootView.findViewById(R.id.main_zero_time_collect);

        collect.setOnClickListener(this);

        delete=(RippleImage)rootView.findViewById(R.id.main_zero_time_delete);

        delete.setOnClickListener(this);

        movieViews=new ArrayList<View>();
        view1=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle1=(DownloadCircle)view1.findViewById(R.id.movie_download_circle);
        downloadCircle1.setOnClickListener(this);
        movie_image1=(ImageView)view1.findViewById(R.id.main_movie_image);
        movie_background1=(RelativeLayout)view1.findViewById(R.id.main_movie_background);

        view2=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle2=(DownloadCircle)view2.findViewById(R.id.movie_download_circle);
        downloadCircle2.setOnClickListener(this);
        movie_image2=(ImageView)view2.findViewById(R.id.main_movie_image);
        movie_background2=(RelativeLayout)view2.findViewById(R.id.main_movie_background);

        view3=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle3=(DownloadCircle)view3.findViewById(R.id.movie_download_circle);
        downloadCircle3.setOnClickListener(this);
        movie_image3=(ImageView)view3.findViewById(R.id.main_movie_image);
        movie_background3=(RelativeLayout)view3.findViewById(R.id.main_movie_background);

        movieViews.add(view1);
        movieViews.add(view2);
        movieViews.add(view3);
        adapter=new ViewPagerAdapter(movieViews);
        moviePager.setAdapter(adapter);
        moviePager.setCurrentItem(0);
        moviePager.setOnPageChangeListener(this);

        setMoviePage(0);

        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_COMPLETED);
        filter.addAction(ACTION_ERROR);
        filter.addAction(ACTION_FILE_SIZE);
        filter.addAction(ACTION_PERCENT);
        filter.addAction(ACTION_SPEED);
        filter.addAction(ACTION_START);
        filter.addAction(ACTION_WAIT);
        downloadMessageReceiver=new DownloadMessageReceiver();
        getActivity().registerReceiver(downloadMessageReceiver,filter);

        share.setOnClickListener(this);
        movie_comment.setOnClickListener(this);
    }

    @Override
    public void run() {
        String keys[]={"number"};
        String parameters[]={"1"};
        if(MineFragment.isLogin)
            parameters[0]=MineFragment.userInfo.getNumber();
        String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_customer_movie.php",keys,parameters);
        if(result!=null&&!result.contains("error")){
            movieQueue.add(new Movie(result));
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }
    @Override
    public void onPageSelected(int i) {
        setMoviePage(i);
        position=i;
        if(movies.size()>=3)
            currentMovie=movies.get(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        if(v==downloadCircle1){
            if(Network.checkNetWorkState(getActivity())){
                if(MineFragment.isLogin){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number"};
                            String parameters[]={movies.get(0).getMovieId(),MineFragment.userInfo.getNumber()};
                            NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploadviewlog.php",keys,parameters);
                        }
                    }).start();
                }
            }
            if(tempMovies.size()>=1) {
                Intent intent1=new Intent(getActivity(),PlayMovie.class);
                Bundle bundle=new Bundle();
                String url = tempMovies.get(0).getPlayUrl();
                bundle.putString("movieurl", "sdcard/FreeTime/Movies/" + url.substring(url.lastIndexOf("/") + 1));
                bundle.putString("title", tempMovies.get(0).getMovieName());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }else{
                Toast.makeText(getActivity(),"Downloading...,Wait...",Toast.LENGTH_SHORT).show();
            }
        }else if(v==downloadCircle2){
            if(Network.checkNetWorkState(getActivity())){
                if(MineFragment.isLogin){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number"};
                            String parameters[]={movies.get(0).getMovieId(),MineFragment.userInfo.getNumber()};
                            NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploadviewlog.php",keys,parameters);
                        }
                    }).start();
                }
            }
            if(tempMovies.size()>=2) {
                Intent intent1=new Intent(getActivity(),PlayMovie.class);
                Bundle bundle=new Bundle();
                String url = tempMovies.get(1).getPlayUrl();
                bundle.putString("movieurl", "sdcard/FreeTime/Movies/" + url.substring(url.lastIndexOf("/") + 1));
                bundle.putString("title", tempMovies.get(1).getMovieName());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }else{
                Toast.makeText(getActivity(),"Downloading...,Wait...",Toast.LENGTH_SHORT).show();
            }
        }else if(v==downloadCircle3){
            if(Network.checkNetWorkState(getActivity())){
                if(MineFragment.isLogin){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number"};
                            String parameters[]={movies.get(2).getMovieId(),MineFragment.userInfo.getNumber()};
                            NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploadviewlog.php",keys,parameters);
                        }
                    }).start();
                }
            }
            if(tempMovies.size()>=3) {
                Intent intent1=new Intent(getActivity(),PlayMovie.class);
                Bundle bundle=new Bundle();
                String url = tempMovies.get(2).getPlayUrl();
                bundle.putString("movieurl", "sdcard/FreeTime/Movies/" + url.substring(url.lastIndexOf("/") + 1));
                bundle.putString("title", tempMovies.get(2).getMovieName());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }else{
                Toast.makeText(getActivity(),"Downloading...,Wait...",Toast.LENGTH_SHORT).show();
            }
        }else if(v==movie_comment){
            if(currentMovie!=null) {
                Intent intent = new Intent(getActivity(), CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie", currentMovie);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }else if(v==collect){
            if(currentMovie!=null)
              updateCollectState(position);
        }else if(v==delete){
            if(currentMovie!=null) {
                deleteMovie(position);
                Toast.makeText(getActivity(),"Have delete!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(),"No Movie!",Toast.LENGTH_SHORT).show();
            }
        }else if(v==share){
            if(currentMovie!=null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, currentMovie.getMovieName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send To"));
            }
        }
    }

    public class DownloadMessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int position=Integer.valueOf(intent.getStringExtra("tag"));
            updateView(position, intent);
        }
    }

    /**
     * set value while page scrolled
     * @param position
     */
    private void setMoviePage(int position){
        Movie temp=null;
        switch (position){
            case 0:
                if(movies.size()>=1){
                    temp=movies.get(0);
                }
                if(temp!=null){
                    movie_description.setText(temp.getMovieName());
                    movie_image1.setTag(temp.getImageUrl());
                    movie_time.setText("File Size:" + temp.getFileSize());
                    imageLoader.DisplayImage(temp.getImageUrl(), movie_image1);
                    if(collectstate[0])
                        collect.setImageResource(R.drawable.main_collect_button_checked);
                    else
                        collect.setImageResource(R.drawable.main_collect_button);
                }else{
                    movie_description.setText("网速不给力哦~加载不出来呢！");
                    movie_time.setText("File Size:");
                }
                break;
            case 1:
                if(movies.size()>=2){
                    temp=movies.get(1);
                }
                if(temp!=null){
                    movie_description.setText(temp.getMovieName());
                    movie_image2.setTag(temp.getImageUrl());
                    movie_time.setText("File Size:" + temp.getFileSize());
                    imageLoader.DisplayImage(temp.getImageUrl(), movie_image2);
                    if(collectstate[1])
                        collect.setImageResource(R.drawable.main_collect_button_checked);
                    else
                        collect.setImageResource(R.drawable.main_collect_button);
                }else{
                    movie_description.setText("网速不给力哦~加载不出来呢！");
                    movie_time.setText("File Size:");
                }
                break;
            case 2:
                if(movies.size()>=3){
                    temp=movies.get(2);
                }
                if(temp!=null){
                    movie_description.setText(temp.getMovieName());
                    movie_image3.setTag(temp.getImageUrl());
                    movie_time.setText("File Size:" + temp.getFileSize());
                    imageLoader.DisplayImage(temp.getImageUrl(), movie_image3);
                    if(collectstate[2])
                        collect.setImageResource(R.drawable.main_collect_button_checked);
                    else
                        collect.setImageResource(R.drawable.main_collect_button);
                }else{
                    movie_description.setText("网速不给力哦~加载不出来呢！");
                    movie_time.setText("File Size:");
                }
                break;
        }
    }

    /**
     * update view in pager
     * @param position
     * @param intent
     */
    private void updateView(int position,Intent intent) {
        if (position == 0) {
            if (intent.getAction().equals(ACTION_START)) {
                downloadCircle1.setStart();
                downloadCircle1.setPercent(0.0f);
            } else if (intent.getAction().equals(ACTION_ERROR)) {
                downloadCircle1.downloadError();
            } else if (intent.getAction().equals(ACTION_PERCENT)) {
                float value = intent.getFloatExtra("percent", 0f);
                downloadCircle1.setPercent(value / 1000);
            } else if (intent.getAction().equals(ACTION_COMPLETED)) {
                if(tempMovies.size()>=1) {
                    movies.get(0).setFileSize(filesize[0]);
                    tempMovies.set(0, movies.get(0));
                }else {
                    tempMovies.add(movies.get(0));
                }
                saveMovie();
                downloadCircle1.setPercent(1.0f);
                downloadCircle1.endDownload();
            } else if (intent.getAction().equals(ACTION_SPEED)) {
                int speed = intent.getIntExtra("speed", 0);
                downloadCircle1.setDownload_speed(String.valueOf(speed));
            } else if (intent.getAction().equals(ACTION_FILE_SIZE)) {
                filesize[0]=String.valueOf(intent.getLongExtra("file_size",0))+"M";
            }else if(intent.getAction().equals(ACTION_WAIT)){
                downloadCircle1.setWait();
            }
        }else if(position==1){
            if (intent.getAction().equals(ACTION_START)) {
                downloadCircle2.setStart();
                downloadCircle2.setPercent(0.0f);
            } else if (intent.getAction().equals(ACTION_ERROR)) {
                downloadCircle2.downloadError();
            } else if (intent.getAction().equals(ACTION_PERCENT)) {
                float value = intent.getFloatExtra("percent", 0f);
                downloadCircle2.setPercent(value / 1000);
            } else if (intent.getAction().equals(ACTION_COMPLETED)) {
                if(tempMovies.size()>=2) {
                    movies.get(1).setFileSize(filesize[1]);
                    tempMovies.set(1, movies.get(1));
                }else {
                    tempMovies.add(movies.get(1));
                }
                saveMovie();
                downloadCircle2.setPercent(1.0f);
                downloadCircle2.endDownload();
            } else if (intent.getAction().equals(ACTION_SPEED)) {
                int speed = intent.getIntExtra("speed", 0);
                downloadCircle2.setDownload_speed(String.valueOf(speed));
            } else if (intent.getAction().equals(ACTION_FILE_SIZE)) {
                filesize[1]=String.valueOf(intent.getLongExtra("file_size",0))+"M";
            }else if(intent.getAction().equals(ACTION_WAIT)){
                downloadCircle2.setWait();
            }
        }else if(position==2){
            if (intent.getAction().equals(ACTION_START)) {
                downloadCircle3.setStart();
                downloadCircle3.setPercent(0.0f);
            } else if (intent.getAction().equals(ACTION_ERROR)) {
                downloadCircle3.downloadError();
            } else if (intent.getAction().equals(ACTION_PERCENT)) {
                float value = intent.getFloatExtra("percent", 0f);
                downloadCircle3.setPercent(value / 1000);
            } else if (intent.getAction().equals(ACTION_COMPLETED)) {
                if(tempMovies.size()>=3) {
                    movies.get(2).setFileSize(filesize[2]);
                    tempMovies.set(2, movies.get(2));
                }else {
                    tempMovies.add(movies.get(2));
                }
                saveMovie();
                downloadCircle3.setPercent(1.0f);
                downloadCircle3.endDownload();
            } else if (intent.getAction().equals(ACTION_SPEED)) {
                int speed = intent.getIntExtra("speed", 0);
                downloadCircle3.setDownload_speed(String.valueOf(speed));
            } else if (intent.getAction().equals(ACTION_FILE_SIZE)) {
                filesize[2]=String.valueOf(intent.getLongExtra("file_size",0))+"M";
            }else if(intent.getAction().equals(ACTION_WAIT)){
                downloadCircle3.setWait();
            }
        }
    }

    /**
     * save movie
     */
    private void saveMovie(){
        ArrayList<BaseObject>arrayList=new ArrayList<BaseObject>();
        for(BaseObject object:tempMovies){
            arrayList.add(object);
        }
        saveAndOpenMovies.Save(getActivity(),arrayList);
    }

    /**
     * check collect state
     * @param i
     */
    private void checkCollectState(final int i){
        if(MineFragment.isLogin&&Network.checkNetWorkState(getActivity())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String keys[] = new String[]{"number", "movieid"};
                    String parameters[] = new String[]{MineFragment.userInfo.getNumber(), movies.get(i).getMovieId()};
                    String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_collection_state.php", keys, parameters);
                    Log.d("result", result);
                    if (result != null && !result.contains("error") && !result.equals("no")) {
                        try {
                            JSONObject object = new JSONObject(result);
                            String state = object.getString("state");
                            if (state.equals("0"))
                                collectstate[i] = false;
                            else
                                collectstate[i] = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * update collect state
     * @param position
     */
    private void updateCollectState(final int position){
        if(Network.checkNetWorkState(getActivity())){
            if(MineFragment.isLogin){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String keys[]={"movieid","number","available"};
                        String parameters[];
                        if(collectstate[position])
                            parameters=new String[]{movies.get(position).getMovieId(),MineFragment.userInfo.getNumber(),"1"};
                        else
                            parameters=new String[]{movies.get(position).getMovieId(),MineFragment.userInfo.getNumber(),"2"};
                        String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/collect.php",keys,parameters);
                        Log.d("result",result);
                        if(result!=null&&!result.contains("error")){
                            collectstate[position]=collectstate[position]?false:true;
                            mHandler.sendEmptyMessage(4);
                        }
                    }
                }).start();
            }else{
                LoginNotification.loginNotification(getActivity());
            }
        }
    }
    public class getNewMovie implements Runnable{

        private int position;
        public getNewMovie(int position){
            this.position=position;
        }
        @Override
        public void run() {
            String keys[]={"number"};
            String parameters[]={"1"};
            if(MineFragment.isLogin)
                parameters[0]=MineFragment.userInfo.getNumber();
            String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_customer_movie.php",keys,parameters);
            if(result!=null&&!result.contains("error")){
                movieQueue.add(new Movie(result));
                Message message=new Message();
                message.obj=position;
                message.what=6;
                mHandler.sendMessage(message);
            }
        }
    }
    private void deleteMovie(final int position){
        Message message=new Message();
        message.obj=position;
        message.what=5;
        mHandler.sendMessage(message);
    }
}
