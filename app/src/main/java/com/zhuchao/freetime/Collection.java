package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.CollectionAdapter;
import bean.Movie;
import bean.Movies;
import function.Network;
import function.NetworkFunction;
import view_rewrite.SwipeListView;
import view_rewrite.UploadView;


public class Collection extends Activity implements Runnable, UploadView.OnUploadListener{

    private CollectionAdapter adapter;

    private SwipeListView swipeListView;

    private ArrayList<Movie>movieArrayList;

    private Movies movies;

    private ImageView back_button;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mine_my_collection);

        initView();

        if(Network.checkNetWorkState(Collection.this)){
            new Thread(this).start();
        }
    }

    private void initView(){
        back_button=(ImageView)findViewById(R.id.left_return_arrow);

        swipeListView=(SwipeListView)findViewById(R.id.collection_list);

        movieArrayList=new ArrayList<Movie>();

        adapter=new CollectionAdapter(movieArrayList,Collection.this);

        swipeListView.setAdapter(adapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void run() {
        String keys[]=new String[]{"number"};
        String parameters[]=new String[]{"241938F47DE2A7CEAB664C99E5A63F28"};
        String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_collection.php",keys,parameters);
        Log.d("result",result);
        if(result!=null&&!result.contains("error")){
            movies=new Movies(result);
            for(int i=0;i<movies.getCount();i++) {
                movieArrayList.add((Movie) movies.getItem(i));
            }
            mHandler.sendEmptyMessage(0);
        }

    }

    @Override
    public void onMore() {

    }
}
