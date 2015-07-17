package com.zhuchao.freetime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;


public class TopHot_Detail extends Activity implements View.OnClickListener{
    //back button
    private ImageView back_button;
    //collection button
    private ImageView collect_button;
    //share button
    private ImageView share_button;
    //comment button
    private ImageView comment_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.top_hot_details);

        initView();

        initData();
    }

    private void initView(){
        back_button=(ImageView)findViewById(R.id.top_hot_detail_return_button);

        share_button=(ImageView)findViewById(R.id.top_hot_detail_share_button);

        comment_button=(ImageView)findViewById(R.id.top_hot_detail_comment_button);

        collect_button=(ImageView)findViewById(R.id.top_hot_detail_collect_button);
    }

    private void initData(){
        back_button.setOnClickListener(this);

        share_button.setOnClickListener(this);

        comment_button.setOnClickListener(this);

        collect_button.setOnClickListener(this);
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
                intent.putExtra(Intent.EXTRA_TEXT, "Free Time First Share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send To"));
                break;
            case R.id.top_hot_detail_collect_button:
                break;
            case R.id.top_hot_detail_comment_button:
                break;
        }
    }
}
