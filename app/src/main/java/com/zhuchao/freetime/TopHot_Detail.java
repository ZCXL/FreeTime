package com.zhuchao.freetime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import bean.Comment;
import bean.Comments;
import bean.Movie;
import function.ImageLoaderTask;
import function.ImageProcess;
import function.Network;
import function.NetworkFunction;
import utils.ImageLoader;
import view_rewrite.CustomProgressDialog;
import view_rewrite.RoundImageView;
import view_rewrite.UploadView;


public class TopHot_Detail extends Activity implements View.OnClickListener, UploadView.OnUploadListener,Runnable{
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
    private TextView video_description;
    //play button
    private ImageView play_button;
    //movie
    private Movie movie;
    //container
    private LinearLayout linearLayout;
    //comments
    private Comments comments;

    private Comment comment;

    private CustomProgressDialog dialog;

    //image loader
    private ImageLoader imageLoader;
    //comment edit
    private EditText comment_edit;
    //send button
    private Button send;
    //state
    private boolean isCollect=false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(isCollect){
                        collect_button.setImageResource(R.drawable.top_hot_detail_collect_button_checked);
                    }else{
                        collect_button.setImageResource(R.drawable.top_hot_detail_collect_button);
                    }
                    for(int i=0;i<comments.getCount();i++){
                        Comment comment=(Comment)comments.getItem(i);
                        addComment(comment);
                    }
                    stopProgressDialog();
                    break;
                case 1:
                    stopProgressDialog();
                    break;
                case 2:
                    comment_edit.setText("");
                    addComment(comment);
                    Toast.makeText(TopHot_Detail.this,"Comment Successfully",Toast.LENGTH_SHORT).show();
                    stopProgressDialog();
                    break;
                case 3:
                    Toast.makeText(TopHot_Detail.this,"Comment failed,please try again!",Toast.LENGTH_SHORT).show();
                    stopProgressDialog();
                    break;
                case 4:
                    if(isCollect){
                        collect_button.setImageResource(R.drawable.top_hot_detail_collect_button_checked);
                        Toast.makeText(TopHot_Detail.this,"Collect successfully!",Toast.LENGTH_SHORT).show();
                    }else{
                        collect_button.setImageResource(R.drawable.top_hot_detail_collect_button);
                        Toast.makeText(TopHot_Detail.this,"Cancel collect successfully!",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
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

        if(Network.checkNetWorkState(TopHot_Detail.this)){
            startProgressDialog();
            new Thread(this).start();
        }
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

        video_description=(TextView)findViewById(R.id.top_hot_detail_detail_introduction);

        linearLayout=(LinearLayout)findViewById(R.id.pull_down_view);

        imageLoader=new ImageLoader(TopHot_Detail.this);

        comment_edit=(EditText)findViewById(R.id.top_hot_detail_bottom_edit);

        send=(Button)findViewById(R.id.top_hot_detail_send_comment);
    }

    private void initData(){
        if(movie!=null){
            new ImageLoaderTask(video_image,TopHot_Detail.this, ImageProcess.FileType_Image.MovieImage).execute(movie.getImageUrl());

            video_name.setText(movie.getMovieName());

            video_sights.setText("Already has " + movie.getViewNumber() + " watched");

            video_comment.setText(movie.getCommentNumber()+" people commented");

            video_description.setText(movie.getDescription());
        }

        back_button.setOnClickListener(this);

        share_button.setOnClickListener(this);

        comment_button.setOnClickListener(this);

        collect_button.setOnClickListener(this);

        play_button.setOnClickListener(this);

        send.setOnClickListener(this);
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
                if(Network.checkNetWorkState(TopHot_Detail.this)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number","available"};
                            String parameters[];
                            if(isCollect)
                                parameters=new String[]{movie.getMovieId(),"241938F47DE2A7CEAB664C99E5A63F28","1"};
                            else
                                parameters=new String[]{movie.getMovieId(),"241938F47DE2A7CEAB664C99E5A63F28","2"};
                            String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/collect.php",keys,parameters);
                            if(result!=null&&!result.contains("error")){
                                isCollect=isCollect?false:true;
                                mHandler.sendEmptyMessage(4);
                            }
                        }
                    }).start();
                }
                break;
            case R.id.top_hot_detail_comment_button:
                startActivity(new Intent(TopHot_Detail.this,CommentActivity.class));
                break;
            case R.id.top_hot_detail_play_button:
                if(Network.checkNetWorkState(TopHot_Detail.this)){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"movieid","number"};
                            String parameters[]={movie.getMovieId(),"241938F47DE2A7CEAB664C99E5A63F28"};
                            NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploadviewlog.php",keys,parameters);
                        }
                    }).start();
                }
                Intent intent1=new Intent(TopHot_Detail.this,PlayMovie.class);
                Bundle bundle=new Bundle();
                bundle.putString("movieurl",movie.getPlayUrl());
                bundle.putString("title",movie.getMovieName());
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.top_hot_detail_send_comment:
                final String commt=comment_edit.getText().toString();
                if(commt!=null&&!commt.equals("")){
                    startProgressDialog();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String keys[]={"number","comment","movieid"};
                            String parameters[]={"241938F47DE2A7CEAB664C99E5A63F28",commt,movie.getMovieId()};
                            String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/comment.php",keys,parameters);
                            if(result!=null&&!result.contains("error")){
                                comment=new Comment(result);
                                mHandler.sendEmptyMessage(2);
                            }else{
                                mHandler.sendEmptyMessage(1);
                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(TopHot_Detail.this,"Comment can't be null",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMore() {

    }

    @Override
    public void run() {
        String keys[]=new String[]{"movieid","page"};
        String parameters[]=new String[]{movie.getMovieId(),"1"};
        String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_comment.php",keys,parameters);
        keys=new String[]{"number","movieid"};
        parameters=new String[]{"241938F47DE2A7CEAB664C99E5A63F28",movie.getMovieId()};
        String result1=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_collection_state.php",keys,parameters);
        Log.d("result1", result);
        Log.d("result2", result1);
        if((result!=null&&!result.contains("error"))&&(result1!=null&&!result1.contains("error"))){
            comments=new Comments(result);
            try {
                JSONObject object=new JSONObject(result1);
                String state=object.getString("state");
                if(state.equals("0"))
                    isCollect=false;
                else
                    isCollect=true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(0);
        }else{
            mHandler.sendEmptyMessage(1);
        }
    }
    private void startProgressDialog(){
        if (dialog == null){
            dialog= CustomProgressDialog.createDialog(TopHot_Detail.this);
        }
        dialog.show();
    }

    private void stopProgressDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    private void addComment(Comment comment){
        View view= LayoutInflater.from(TopHot_Detail.this).inflate(R.layout.comment_item,null);
        RoundImageView imageView=(RoundImageView)view.findViewById(R.id.mine_head_image);
        TextView name=(TextView)view.findViewById(R.id.user_name);
        TextView comment_text=(TextView)view.findViewById(R.id.comment_text);
        TextView time=(TextView)view.findViewById(R.id.comment_time);
        name.setText(comment.getNick_name());
        comment_text.setText(comment.getComment());
        time.setText(comment.getTime());
        imageView.setTag(comment.getHead_url());
        imageLoader.DisplayImage(comment.getHead_url(), imageView);
        linearLayout.addView(view);
    }
}
