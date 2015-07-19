package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import bean.Movie;
import view_rewrite.PlayerView;

public class PlayMovie extends Activity {

    private PlayerView playerView;
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_play_movie);

        playerView=(PlayerView)findViewById(R.id.play_video);
        //get movie source
        if(getIntent().getExtras()!=null){
            movie=getIntent().getExtras().getParcelable("movie");
            playerView.setTitle(movie.getMovieName());
            playerView.setUrl(movie.getPlayUrl());
        }

    }
}
