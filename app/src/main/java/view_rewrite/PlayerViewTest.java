package view_rewrite;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.Utils;

/**
 * Created by zhuchao on 7/17/15.
 */
public class PlayerViewTest extends FrameLayout implements View.OnClickListener{
    //background
    private int background_color= Color.parseColor("#88000000");
    //top
    private RelativeLayout top_bar_relativeLayout;
    private ImageView back_button;
    private TextView top_bar_movie_name;

    //bottom
    private LinearLayout bottom_bar_relativeLayout;
    private Slider slider;
    private TextView total_time;
    //sound
    private RelativeLayout sound_bar_relativLayout;
    //brightness
    private RelativeLayout brightness_bar_relativeLayout;

    //start button
    private ImageView start_button;

    //play interface
    private SurfaceView surfaceView;

    //view player
    private Player player;

    private int loaded=0;

    //is play
    private boolean isPlay=false;
    public PlayerViewTest(Context context,AttributeSet attributeSet){
        super(context, attributeSet);

        setAttributes(attributeSet);

        initData();

    }

    public void setUrl(String url){
        player.setUrl(url);
    }

    private void setAttributes(AttributeSet attributes) {
        int background=attributes.getAttributeResourceValue(CustomerView.ANDROIDXML,"background",-1);
        if(background!=-1){
            background_color=getResources().getColor(background);
        }else{
            background=attributes.getAttributeIntValue(CustomerView.ANDROIDXML,"background",-1);
            if(background!=-1){
                background_color=background;
            }
        }

        addSurface();

        addTopBar();

        addBottomBar();

        addStart();
    }

    private void initData(){
        //seekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());

        slider.setOnValueChangedListener(new SliderChangeListener());

        back_button.setOnClickListener(this);

        start_button.setOnClickListener(this);

        player=new Player(surfaceView,slider);

    }

    /**
     * add play interface
     */
    private void addSurface(){

        surfaceView=new SurfaceView(getContext());
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        surfaceView.setLayoutParams(params);
        addView(surfaceView);

    }

    /**
     * add top bar
     */
    private void addTopBar(){

        top_bar_relativeLayout=new RelativeLayout(getContext());

        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dpToPx(50,getResources()));
        params.gravity= Gravity.TOP;
        top_bar_relativeLayout.setBackgroundColor(background_color);
        top_bar_relativeLayout.setLayoutParams(params);

        //back button
        back_button=new ImageView(getContext());
        back_button.setImageResource(R.drawable.left_return_arrow);
        RelativeLayout.LayoutParams back_button_params=new RelativeLayout.LayoutParams(Utils.dpToPx(12.66f,getResources()),Utils.dpToPx(22,getResources()));
        back_button_params.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
        back_button_params.leftMargin=Utils.dpToPx(17.33f,getResources());
        back_button.setLayoutParams(back_button_params);
        top_bar_relativeLayout.addView(back_button);

        //title name
        top_bar_movie_name=new TextView(getContext());
        RelativeLayout.LayoutParams movie_name_params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        movie_name_params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        top_bar_movie_name.setLayoutParams(movie_name_params);
        top_bar_movie_name.setTextSize(13);
        top_bar_movie_name.setTextColor(getResources().getColor(android.R.color.white));
        top_bar_movie_name.setText("FQL is SB.");
        top_bar_relativeLayout.addView(top_bar_movie_name);

        addView(top_bar_relativeLayout);
    }

    /**
     * add bottom
     */
    private void addBottomBar(){
        bottom_bar_relativeLayout=new LinearLayout(getContext());

        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity=Gravity.BOTTOM;
        bottom_bar_relativeLayout.setOrientation(LinearLayout.VERTICAL);
        bottom_bar_relativeLayout.setBackgroundColor(background_color);
        bottom_bar_relativeLayout.setLayoutParams(params);

        //slider
        slider=new Slider(getContext());
        slider.setMax(100);
        slider.setMin(0);
        slider.setValue(0);
        slider.setShowNumberIndicator(true);
        LinearLayout.LayoutParams slider_params=new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        slider_params.setMargins(Utils.dpToPx(5, getResources()), 0, Utils.dpToPx(5, getResources()), 0);
        slider.setLayoutParams(slider_params);
        slider.setPadding(0, Utils.dpToPx(3, getResources()), 0, 0);
        slider.setMax(100);
        bottom_bar_relativeLayout.addView(slider);

        //relativeLayout
        RelativeLayout time_relativeLayout=new RelativeLayout(getContext());
        LinearLayout.LayoutParams time_layout_params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        time_relativeLayout.setPadding(0, Utils.dpToPx(3, getResources()), 0, Utils.dpToPx(3, getResources()));
        time_relativeLayout.setLayoutParams(time_layout_params);

        //time
        total_time=new TextView(getContext());
        RelativeLayout.LayoutParams time_params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        time_params.setMargins(0, Utils.dpToPx(10, getResources()), Utils.dpToPx(10, getResources()), 0);
        time_params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        time_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        time_params.rightMargin=Utils.dpToPx(20,getResources());
        total_time.setLayoutParams(time_params);
        total_time.setTextColor(getResources().getColor(android.R.color.white));
        total_time.setTextSize(12);
        time_relativeLayout.addView(total_time);


        bottom_bar_relativeLayout.addView(time_relativeLayout);

        addView(bottom_bar_relativeLayout);
    }

    /**
     * add start button
     */
    private void addStart(){
        start_button=new ImageView(getContext());
        start_button.setImageResource(R.drawable.top_hot_detail_play_button);

        FrameLayout.LayoutParams params=new LayoutParams(Utils.dpToPx(79.33f,getResources()),Utils.dpToPx(77.33f,getResources()));
        params.gravity=Gravity.CENTER;
        start_button.setLayoutParams(params);

        addView(start_button);
    }
    public void setTitle(String title){
        top_bar_movie_name.setText(title);
    }
    public void setTotalTime(String time){
        total_time.setText(time);
    }

    @Override
    public void onClick(View v) {
        if(v==back_button){
            player.stop();
        }else if(v==start_button){
            if(loaded==0){
                isPlay=true;
                player.play();
                start_button.setImageResource(R.drawable.video_play_pause_button);
                loaded++;
                return;
            }
            if(!isPlay) {
                isPlay=true;
                player.pause();
                start_button.setImageResource(R.drawable.video_play_pause_button);
            }else{
                isPlay=false;
                player.pause();
                start_button.setImageResource(R.drawable.top_hot_detail_play_button);
            }
        }
    }
    public static String getTime(long time){
        time=time/1000;
        int hour=(int)(time/3600);
        int minutes=(int)((time-hour*3600)/60);
        int second=(int)(time-hour*3600-minutes*60);
        StringBuilder builder=new StringBuilder();
        if(hour>0){
            if(hour<10)
                builder.append("0"+String.valueOf(hour)+":");
            else
                builder.append(String.valueOf(hour)+":");
        }
        if(minutes>0){
            if(hour<10)
                builder.append("0"+String.valueOf(minutes)+":");
            else
                builder.append(String.valueOf(minutes)+":");
        }else{
            builder.append("00:");
        }
        if(second>0){
            if(second<10)
                builder.append("0"+String.valueOf(second));
            else
                builder.append(String.valueOf(second));
        }
        return builder.toString();
    }

    private class SliderChangeListener implements Slider.OnValueChangedListener{

        int progress;
        @Override
        public void onValueChanged(int value) {
            this.progress=value*player.mediaPlayer.getDuration()/slider.getMax();
        }

        @Override
        public void onStopTrackingTouch(int value) {
            int position=value*player.mediaPlayer.getDuration()/slider.getMax();
            if(player.mediaPlayer.isPlaying())
               player.mediaPlayer.seekTo(position);
            else{
                player.position=value*player.mediaPlayer.getDuration()/slider.getMax();
                player.mediaPlayer.pause();
            }
        }
    }
    public class Player implements MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
        private int videoWidth;
        private int videoHeight;
        public MediaPlayer mediaPlayer;
        private SurfaceHolder surfaceHolder;
        private Slider slider;
        private Timer mTimer = new Timer();
        public int position;
        private String url;
        private boolean isPasue=true;

        public void setUrl(String url){
            this.url=url;
        }
        public Player(SurfaceView surfaceView,Slider slider){
            this.slider=slider;
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mTimer.schedule(mTimerTask, 0, 1000);
        }
        TimerTask mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer == null)
                    return;
                if (mediaPlayer.isPlaying()) {
                    handleProgress.sendEmptyMessage(0);
                }
            }
        };

        Handler handleProgress = new Handler() {
            public void handleMessage(Message msg) {

                int position = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                if(duration>0){
                    long pos=slider.getMax()*position/duration;
                    slider.setValue((int) pos);
                    total_time.setText(getTime(duration));
                }
            };
        };

        // *****************************************************

        public void pause(){
            if(mediaPlayer.isPlaying()){
                position=mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                isPasue=true;
            }else{
                if(isPasue){
                    mediaPlayer.start();
                    isPasue=false;
                }
            }
        }
        public void play() {
            rollback();
        }
        public void replay(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(0);
            }else{
                position=0;
                rollback();
            }
        }
        public void stop() {
            if (mediaPlayer != null&&mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }
        private void rollback(){
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        public void setUrl(String videoUrl) {
//            this.url=videoUrl;
//            try {
//                mediaPlayer.reset();
//                mediaPlayer.setDataSource(url);
//                mediaPlayer.prepare();
//                // mediaPlayer.start();
//            } catch (IllegalArgumentException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IllegalStateException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
            Log.e("mediaPlayer", "surface changed");
        }

        @Override
        public void surfaceCreated(SurfaceHolder arg0) {
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDisplay(surfaceHolder);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnPreparedListener(this);
            } catch (Exception e) {
                Log.e("mediaPlayer", "error", e);
            }
            Log.e("mediaPlayer", "surface created");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            Log.e("mediaPlayer", "surface destroyed");
        }

        @Override
        public void onPrepared(MediaPlayer arg0) {
            videoWidth = mediaPlayer.getVideoWidth();
            videoHeight = mediaPlayer.getVideoHeight();
            slider.setTotal_Time(arg0.getDuration());
            if (videoHeight != 0 && videoWidth != 0) {
                arg0.start();
            }
            if(position>0){
                mediaPlayer.seekTo(position);
            }
            Log.e("mediaPlayer", "onPrepared");
        }

        @Override
        public void onCompletion(MediaPlayer arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
            int currentProgress=slider.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
            Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
        }

    }



}
