package com.zhuchao.freetime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import adapter.CommentBackAdapter;
import adapter.ViewPagerAdapter;
import bean.Comment;
import bean.Comments;
import bean.Movie;
import function.Network;
import function.NetworkFunction;
import view_rewrite.CircularProgressBar;
import view_rewrite.EditWindow;
import view_rewrite.LoadingDialog;


public class CommentActivity extends Activity implements Runnable,View.OnClickListener{

    private ListView comment_back_list;

    //private UploadView uploadView;

    private ArrayList<Comment>comments;

    private CommentBackAdapter adapter;

    private LoadingDialog loadingDialog;

    private TextView comment_number;

    private ImageView back_button;

    //footer view
    private View footerView;
    //progress bar
    private CircularProgressBar circularProgressBar;
    //page
    private int page=1;

    private Movie movie;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter.notifyDataSetChanged();
                    loadingDialog.stopProgressDialog();
                    //uploadView.notifyDidLoad();
                    break;
                case 1:
                    loadingDialog.stopProgressDialog();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.top_hot_detail_comment);

        if(getIntent().getExtras()!=null){
            Bundle bundle=getIntent().getExtras();
            movie=bundle.getParcelable("movie");
            Log.d("detail",movie.getDescription());
        }

        initView();

        if(Network.checkNetWorkState(CommentActivity.this)){
            loadingDialog.startProgressDialog();
            new Thread(this).start();
        }
    }
    private void initView(){
        loadingDialog=new LoadingDialog(CommentActivity.this);

        comment_number=(TextView)findViewById(R.id.comment_number);

        back_button=(ImageView)findViewById(R.id.left_return_arrow);

        back_button.setOnClickListener(this);

        comments=new ArrayList<Comment>();

        adapter=new CommentBackAdapter(CommentActivity.this,comments,movie);

        comment_back_list=(ListView)findViewById(R.id.comment_back_list);

        comment_back_list.setAdapter(adapter);

        footerView=LayoutInflater.from(CommentActivity.this).inflate(R.layout.pulldown_footer, null);

        circularProgressBar=(CircularProgressBar)footerView.findViewById(R.id.pull_down_footer_loading);

        comment_back_list.addFooterView(footerView);

        footerView.setOnClickListener(this);

        if(movie!=null)
            comment_number.setText(movie.getCommentNumber()+" comments");

    }

    @Override
    public void run() {
        String keys[]=new String[]{"movieid","page"};
        String parameters[]=new String[]{movie.getMovieId(),String.valueOf(page)};
        String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_comment.php", keys, parameters);
        Log.d("result2", result);
        if(result!=null&&!result.contains("error")){
            Comments comments1=new Comments(result);
            for(int i=0;i<comments1.getCount();i++){
                comments.add((Comment)comments1.getItem(i));
            }
            page++;
            mHandler.sendEmptyMessage(0);
        }else{
            mHandler.sendEmptyMessage(1);
        }
    }

    @Override
    public void onClick(View v) {
        if(v==footerView){
            if(Network.checkNetWorkState(CommentActivity.this)){
                loadingDialog.startProgressDialog();
                new Thread(this).start();
            }
        }else if(v==back_button){
            finish();
        }
    }
}
