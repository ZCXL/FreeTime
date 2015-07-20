package view_rewrite;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.freetime.R;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import utils.Utils;

/**
 * Created by zhuchao on 7/17/15.
 */
public class PlayerView extends RelativeLayout implements View.OnClickListener{
    //activity;
    private Activity activity;
    //background
    private int background_color= Color.parseColor("#88000000");
    //parent layout
    private FrameLayout parent_layout;
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
    private RelativeLayout center_relativeLayout;
    private ImageView start_button;
    private TextView notify_info;
    private static int ID_NOTIFY=1;

    //play interface
    private SurfaceView surfaceView;

    //view player
    private Player player;

    private int loaded=0;
    //is play
    private boolean isPlay=true;
    private String url;
    private String title;

    //slide event

    private boolean isPressed=false;
    private double xLast,yLast;

    private long touchTime=0;

    public static int PAUSE=1;
    public static int SPEED=2;
    public static int BACK=3;
    public static int PLAYING=4;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showAll();
                    if(player.currentState==1)
                        start_button.setImageResource(R.drawable.top_hot_detail_play_button);
                    if(player.currentState==2)
                        start_button.setImageResource(R.drawable.video_play_speed_button);
                    if(player.currentState==3)
                        start_button.setImageResource(R.drawable.video_play_back_button);
                    if(player.currentState==4)
                        start_button.setImageResource(R.drawable.video_play_pause_button);
                    this.sendEmptyMessageDelayed(1, 3000);
                    break;
                case 1:
                    if(player.lastState==PLAYING&&player.currentState==BACK) {
                        start_button.setImageResource(R.drawable.top_hot_detail_play_button);
                        hideAll();
                    }else if(player.lastState==PLAYING&&player.currentState==SPEED) {
                        start_button.setImageResource(R.drawable.top_hot_detail_play_button);
                        hideAll();
                    }else if(player.currentState==PLAYING)
                        hideAll();
                    break;
            }
        }
    };

    public PlayerView(Context context,String url,String title,Activity activity){
        super(context);

        this.activity=activity;

        this.url=url;

        this.title=title;

        setAttributes();

        initData();
    }
    public PlayerView(Context context,AttributeSet attributeSet){
        super(context, attributeSet);

        setAttributes(attributeSet);

        initData();

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
        addParent();

        addSurface();

        addTopBar();

        addBottomBar();

        addStart();
    }
    private void setAttributes(){

        addParent();

        addSurface();

        addTopBar();

        addBottomBar();

        addStart();
    }

    private void initData(){

        slider.setOnValueChangedListener(new SliderChangeListener());

        back_button.setOnClickListener(this);

        start_button.setOnClickListener(this);

        top_bar_movie_name.setText(title);

        player=new Player(surfaceView,slider,url);

    }

    /**
     * add parent layout
     */
    private void addParent(){

        parent_layout=new FrameLayout(getContext());

        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        parent_layout.setLayoutParams(params);
        parent_layout.setClickable(true);

        addView(parent_layout);
    }
    /**
     * add play interface
     */
    private void addSurface(){

        surfaceView=new SurfaceView(getContext());
        FrameLayout.LayoutParams params=new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
        surfaceView.setLayoutParams(params);
        parent_layout.addView(surfaceView);

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
        top_bar_relativeLayout.addView(top_bar_movie_name);

        parent_layout.addView(top_bar_relativeLayout);
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

        parent_layout.addView(bottom_bar_relativeLayout);
    }

    /**
     * add start button
     */
    private void addStart(){
        center_relativeLayout=new RelativeLayout(getContext());
        FrameLayout.LayoutParams center_params=new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        center_params.gravity=Gravity.CENTER;
        center_relativeLayout.setBackgroundResource(getResources().getColor(android.R.color.transparent));
        center_relativeLayout.setLayoutParams(center_params);

        notify_info=new TextView(getContext());
        RelativeLayout.LayoutParams notify_params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        notify_params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        notify_params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        notify_params.topMargin=5;
        notify_info.setId(R.id.top_hot_detail_detail_introduction);
        notify_info.setLayoutParams(notify_params);
        notify_info.setTextColor(Color.WHITE);
        notify_info.setTextSize(13);
        notify_info.setBackgroundResource(getResources().getColor(android.R.color.transparent));
        center_relativeLayout.addView(notify_info);


        start_button=new ImageView(getContext());
        start_button.setImageResource(R.drawable.video_play_pause_button);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(Utils.dpToPx(79.33f,getResources()),Utils.dpToPx(77.33f,getResources()));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, R.id.top_hot_detail_detail_introduction);
        params.topMargin=10;
        start_button.setLayoutParams(params);

        center_relativeLayout.addView(start_button);

        parent_layout.addView(center_relativeLayout);
    }
    /**
     *
     * @param title
     */
    public void setTitle(String title){
        top_bar_movie_name.setText(title);
    }

    /**
     * hide all relative layout
     */
    private void hideAll(){
        top_bar_relativeLayout.setVisibility(INVISIBLE);
        bottom_bar_relativeLayout.setVisibility(INVISIBLE);
        center_relativeLayout.setVisibility(INVISIBLE);
    }

    /**
     * show all relative layout
     */
    private void showAll(){
        top_bar_relativeLayout.setVisibility(VISIBLE);
        bottom_bar_relativeLayout.setVisibility(VISIBLE);
        center_relativeLayout.setVisibility(VISIBLE);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        double y=event.getRawY();
        double x=event.getRawX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if((System.currentTimeMillis()-touchTime)>1000){
                    touchTime=System.currentTimeMillis();
                    handler.sendEmptyMessage(0);
                }else{
                    player.pause();
                    handler.sendEmptyMessage(0);
                }
                xLast=x;
                yLast=y;
                break;
            case MotionEvent.ACTION_MOVE:
                if(checkPosition(xLast,yLast)){
                    isPressed=true;
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }
    @Override
    public void onClick(View v) {
        if(v==back_button){
            player.stop();
            activity.finish();
        }else if(v==start_button){
            if(!isPlay) {
                isPlay=true;
                player.pause();
            }else{
                isPlay=false;
                player.pause();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d("fa","Action down");
                if(event.getY()>top_bar_relativeLayout.getHeight()&&event.getY()<getHeight()-bottom_bar_relativeLayout.getHeight()){
                    isPressed=true;
                    yLast=event.getRawY();
                    xLast=event.getRawX();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("fa","Action move");
                showAll();
                float xCurrent=event.getRawX();
                float yCurrent=event.getRawY();
                double distanceX=xCurrent-xLast;
                double distanceY=yCurrent-yLast;
                double scale=Math.atan2(distanceY,distanceX);
                //Log.d("data",String.valueOf(xCurrent)+","+String.valueOf(yCurrent)+":"+String.valueOf(distanceX)+","+String.valueOf(distanceY)+":"+String.valueOf(scale));
                if((Math.abs(scale)<Math.PI/12||Math.abs(scale)>Math.PI*11/12)&&isPressed){
                    //Toast.makeText(getContext(),String.valueOf(scale),Toast.LENGTH_SHORT).show();
                    if(distanceX>0)
                        start_button.setImageResource(R.drawable.video_play_speed_button);
                    else if(distanceX<0)
                        start_button.setImageResource(R.drawable.video_play_back_button);
                    slider.setPauseValue(slider.getValue() + 3*(int) (distanceX * slider.getMax() / getWidth()));
                    isPressed=false;
                    handler.sendEmptyMessageDelayed(0,3000);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("fa","Action up");
                //isPressed=false;
                break;
        }
        return false;
    }
    private boolean checkPosition(double x,double y){
        if(y>top_bar_relativeLayout.getHeight()&&y<getHeight()-bottom_bar_relativeLayout.getHeight()){
            if(x>center_relativeLayout.getX()&&x<center_relativeLayout.getX()+center_relativeLayout.getWidth()&&y>center_relativeLayout.getY()&&y>center_relativeLayout.getY()+center_relativeLayout.getHeight()){
                return false;
            }
            return true;
        }else{
            return false;
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

        @Override
        public void onValueChanged(int value) {
            int position=value*player.mediaPlayer.getDuration()/slider.getMax();
            if(player.mediaPlayer.isPlaying()) {
                if (position > player.mediaPlayer.getCurrentPosition()) {
                    player.lastState=PLAYING;
                    player.currentState = SPEED;
                }else {
                    player.lastState=PLAYING;
                    player.currentState= BACK;
                }
                handler.sendEmptyMessage(0);
                player.mediaPlayer.seekTo(position);
            }else{
                player.position=value*player.mediaPlayer.getDuration()/slider.getMax();
                //player.mediaPlayer.pause();
            }
        }

        @Override
        public void onStopTrackingTouch(int value) {
            int position=value*player.mediaPlayer.getDuration()/slider.getMax();
            if(player.mediaPlayer.isPlaying()) {
                if (position > player.mediaPlayer.getCurrentPosition()) {
                    player.lastState=PLAYING;
                    player.currentState = SPEED;
                }else {
                    player.lastState=PLAYING;
                    player.currentState= BACK;
                }
                handler.sendEmptyMessage(0);
                player.mediaPlayer.seekTo(position);
            }else{
                player.position=value*player.mediaPlayer.getDuration()/slider.getMax();
                Log.d("position", String.valueOf(player.position));
                //player.mediaPlayer.pause();
            }
        }
    }
    public class Player implements MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener, SurfaceHolder.Callback,MediaPlayer.OnInfoListener {
        private int videoWidth;
        private int videoHeight;
        public MediaPlayer mediaPlayer;
        private SurfaceHolder surfaceHolder;
        private Slider slider;
        private Timer mTimer = new Timer();
        public int position;
        private String url;
        private boolean isPasue=true;

        public int lastState=PAUSE;
        public int currentState=PAUSE;
        private int count=0;

        public void setUrl(String url){
            this.url=url;
        }
        public Player(SurfaceView surfaceView,Slider slider,String url){
            this.slider=slider;
            this.url=url;
            Log.d("fa",url);
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mTimer.schedule(mTimerTask, 0, 1000);
            handleProgress.sendEmptyMessage(1);
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
                switch (msg.what) {
                    case 0:
                        Log.d("playing",String.valueOf(mediaPlayer.getCurrentPosition()));
                        int position = mediaPlayer.getCurrentPosition();
                        int duration = mediaPlayer.getDuration();
                        if (duration > 0) {
                            long pos = slider.getMax() * position / duration;
                            slider.setValue((int) pos);
                            total_time.setText(getTime(duration));
                        }
                        break;
                    //show notification
                    case 1:
                        if(count==0) {
                            notify_info.setText("caching data.");
                            count++;
                        }else if(count==1){
                            notify_info.setText("caching data..");
                            count++;
                        }else if(count==2){
                            notify_info.setText("caching data...");
                            count=0;
                        }
                        if(!mediaPlayer.isPlaying()){
                            this.sendEmptyMessageDelayed(1,1000);
                        }else{
                            lastState=PAUSE;
                            currentState=PLAYING;
                            handler.sendEmptyMessage(0);
                            notify_info.setText("");
                        }
                        break;
                    //start buffer
                    case 2:
                        Log.d("staring","starting progress");
                        mediaPlayer.reset();
                        try {
                            mediaPlayer.setDataSource(url);
                            mediaPlayer.prepare();
                            //pause();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        // *****************************************************

        /**
         * pause
         */
        public void pause(){
            if(mediaPlayer.isPlaying()){
                position=mediaPlayer.getCurrentPosition();
                mediaPlayer.pause();
                isPasue=true;
                //change interface
                lastState=PLAYING;
                currentState=PAUSE;
                handler.sendEmptyMessage(0);
            }else{
                if(isPasue){
                    mediaPlayer.start();
                    mediaPlayer.seekTo(position);
                    isPasue=false;
                    //change interface
                    lastState=PAUSE;
                    currentState=PLAYING;
                    handler.sendEmptyMessage(0);
                }
            }
        }

        /**
         * play
         */
        public void play() {
            rollback();
        }

        /**
         * replay
         */
        public void replay(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.seekTo(0);
            }else{
                position=0;
                rollback();
            }
        }

        /**
         * stop
         */
        public void stop() {
            if (mediaPlayer != null&&mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
        }

        /**
         * back to start
         */
        private void rollback(){
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                mediaPlayer.setOnInfoListener(this);
            } catch (Exception e) {
                Log.e("mediaPlayer", "error", e);
            }
            Log.e("mediaPlayer", "surface created");
            //start get buffer
            Log.d("mediaPlayer","send 2");
            handleProgress.sendEmptyMessage(2);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder arg0) {
            Log.e("mediaPlayer", "surface destroyed");
        }

        @Override
        public void onPrepared(MediaPlayer arg0) {
            videoWidth = mediaPlayer.getVideoWidth();
            videoHeight = mediaPlayer.getVideoHeight();
            if (videoHeight != 0 && videoWidth != 0) {
                slider.setTotal_Time(arg0.getDuration());
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
            slider.setValue(0);
            position=0;
            mediaPlayer.seekTo(0);
        }

        @Override
        public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
            int currentProgress=slider.getMax()*mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration();
            Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    notify_info.setText("Pausing to buffer more data...");
                    lastState=PLAYING;
                    currentState=PAUSE;
                    handler.sendEmptyMessage(0);
                    Toast.makeText(getContext(),"Network is bad,please stop to buffer more data",Toast.LENGTH_LONG).show();
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    lastState=PAUSE;
                    currentState=PLAYING;
                    notify_info.setText("");
                    handler.sendEmptyMessage(0);
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    notify_info.setText("Sorry,Media can't be played...");
                    break;
                case MediaPlayer.MEDIA_INFO_UNKNOWN:
                    notify_info.setText("Can't recognised error...");
                    break;
            }
            return false;
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }
    }



}
