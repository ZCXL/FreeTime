package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import bean.Movie;
import view_rewrite.PlayerView;

public class PlayMovie extends Activity {

    private PlayerView playerView;
    private String playUrl;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(getIntent().getExtras()!=null){
            Bundle bundle=getIntent().getExtras();
            playUrl=bundle.getString("movieurl");
            title=bundle.getString("title");
            if(playUrl!=null) {
                Log.d("fa", playUrl);
                Toast.makeText(PlayMovie.this,playUrl,Toast.LENGTH_LONG).show();
            }
        }
        playerView=new PlayerView(PlayMovie.this,playUrl,title);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        playerView.setLayoutParams(params);

        setContentView(playerView);


    }
}
