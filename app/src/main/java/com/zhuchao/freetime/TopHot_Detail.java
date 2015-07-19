package com.zhuchao.freetime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import bean.Movie;
import function.ImageLoaderTask;
import function.ImageProcess;


public class TopHot_Detail extends Activity implements View.OnClickListener{
    //back button
    private ImageView back_button;
    //collection button
    private ImageView collect_button;
    //share button
    private ImageView share_button;
    //comment button
    private ImageView comment_button;
    //video image
    private ImageView video_image;
    //video name
    private TextView video_name;
    //sights
    private TextView video_sights;
    //comments
    private TextView video_comment;
    //description
    private TextView video_descrption;
    //play button
    private ImageView play_button;
    //movie
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.top_hot_details);

        if(getIntent().getExtras()!=null) {
            Bundle bundle=getIntent().getExtras();
            movie =bundle.getParcelable("movie");
            Log.d("detail",movie.getDescription());
        }

        initView();

        initData();
    }

    private void initView(){
        back_button=(ImageView)findViewById(R.id.top_hot_detail_return_button);

        share_button=(ImageView)findViewById(R.id.top_hot_detail_share_button);

        comment_button=(ImageView)findViewById(R.id.top_hot_detail_comment_button);

        collect_button=(ImageView)findViewById(R.id.top_hot_detail_collect_button);

        play_button=(ImageView)findViewById(R.id.top_hot_detail_play_button);

        video_image=(ImageView)findViewById(R.id.top_hot_detail_video_image);

        video_name=(TextView)findViewById(R.id.top_hot_detail_brief_introduction);

        video_sights=(TextView)findViewById(R.id.top_hot_detail_video_data_1);

        video_comment=(TextView)findViewById(R.id.top_hot_detail_video_data_2);

        video_descrption=(TextView)findViewById(R.id.top_hot_detail_detail_introduction);
    }

    private void initData(){
        if(movie!=null){
            new ImageLoaderTask(video_image,TopHot_Detail.this, ImageProcess.FileType_Image.MovieImage).execute(movie.getImageUrl());

            video_name.setText(movie.getMovieName());

            video_sights.setText("Already has " + movie.getViewNumber() + " watched");

            video_comment.setText(movie.getCommentNumber()+" people commented");

            video_descrption.setText(movie.getDescription());
        }

        back_button.setOnClickListener(this);

        share_button.setOnClickListener(this);

        comment_button.setOnClickListener(this);

        collect_button.setOnClickListener(this);

        play_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.top_hot_detail_return_button:
                TopHot_Detail.this.finish();
                break;
            case R.id.top_hot_detail_share_button:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "Free Time");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send To"));
                break;
            case R.id.top_hot_detail_collect_button:
                break;
            case R.id.top_hot_detail_comment_button:
                break;

            case R.id.top_hot_detail_play_button:
                Log.d("fa",movie.getDescription());
                Intent intent1=new Intent(TopHot_Detail.this,PlayMovie.class);
                Bundle bundle=new Bundle();
                bundle.putString("movieurl",movie.getPlayUrl());
                bundle.putString("title",movie.getMovieName());
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
        }
    }
}
