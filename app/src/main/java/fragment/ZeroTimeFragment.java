package fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.freetime.MainActivity;
import com.zhuchao.freetime.R;

import java.util.ArrayList;
import java.util.LinkedList;

import adapter.ViewPagerAdapter;
import bean.Movie;
import view_rewrite.DownloadCircle;
import view_rewrite.RippleImage;

/**
 * Created by zhuchao on 7/13/15.
 */
public class ZeroTimeFragment extends Fragment{
    public final static String ACTION_START="download_broadcast.START";
    public final static String ACTION_SPEED="download_broadcast.SPEED";
    public final static String ACTION_PERCENT="download_broadcast.PERCENT";
    public final static String ACTION_ERROR="download_broadcast.ERROR";
    public final static String ACTION_COMPLETED="download_broadcast.COMPLETED";
    public final static String ACTION_FILE_SIZE="download_broadcast.FILE_SIZE";

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

    private DownloadMessageReceiver downloadMessageReceiver;

    private LinkedList<Movie>movieQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_zero_time,container,false);

        initView(rootView);

        return rootView;
    }

    private void initView(View rootView){
        movieQueue=new LinkedList<Movie>();

        moviePager=(ViewPager)rootView.findViewById(R.id.main_zero_time_movie_image_container);

        movie_description=(TextView)rootView.findViewById(R.id.main_zero_time_movie_description);

        movie_time=(TextView)rootView.findViewById(R.id.main_zero_time_movie_size);

        movie_comment=(ImageView)rootView.findViewById(R.id.main_zero_time_movie_comment);

        share=(RippleImage)rootView.findViewById(R.id.main_zero_time_share);

        collect=(RippleImage)rootView.findViewById(R.id.main_zero_time_collect);

        delete=(RippleImage)rootView.findViewById(R.id.main_zero_time_delete);

        movieViews=new ArrayList<View>();
        view1=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle1=(DownloadCircle)view1.findViewById(R.id.movie_download_circle);
        view2=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle2=(DownloadCircle)view2.findViewById(R.id.movie_download_circle);
        view3=LayoutInflater.from(getActivity()).inflate(R.layout.main_zero_time_movie_image,null);
        downloadCircle2=(DownloadCircle)view2.findViewById(R.id.movie_download_circle);
        movieViews.add(view1);
        movieViews.add(view2);
        movieViews.add(view3);
        adapter=new ViewPagerAdapter(movieViews);
        moviePager.setAdapter(adapter);
        moviePager.setCurrentItem(0);


        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_COMPLETED);
        filter.addAction(ACTION_ERROR);
        filter.addAction(ACTION_FILE_SIZE);
        filter.addAction(ACTION_PERCENT);
        filter.addAction(ACTION_SPEED);
        filter.addAction(ACTION_START);
        downloadMessageReceiver=new DownloadMessageReceiver();
        getActivity().registerReceiver(downloadMessageReceiver,filter);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.downloadMovieService.addTask("http://123.56.85.58/FreeTime/apk/freetime.apk");
            }
        });
    }

    public class DownloadMessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("tell me why",intent.getAction());
            if(intent.getAction().equals(ACTION_START)){
                downloadCircle1.setPercent(0.0f);
            }else if (intent.getAction().equals(ACTION_ERROR)) {

            }else if(intent.getAction().equals(ACTION_PERCENT)){
                float value=intent.getFloatExtra("percent",0f);
                Log.d("I will tell you",String.valueOf(value));
                downloadCircle1.setPercent(value/1000);
            }else if(intent.getAction().equals(ACTION_COMPLETED)){
                downloadCircle1.setPercent(1.0f);
            }else if(intent.getAction().equals(ACTION_SPEED)){
                int speed=intent.getIntExtra("speed", 0);
                downloadCircle1.setDownload_speed(String.valueOf(speed));
            }else if(intent.getAction().equals(ACTION_FILE_SIZE)){

            }
        }
    }
}
