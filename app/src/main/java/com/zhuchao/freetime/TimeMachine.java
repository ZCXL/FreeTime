package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import bean.TimeMachineLog;
import fragment.MineFragment;
import function.LoginNotification;
import function.Network;
import function.NetworkFunction;
import view_rewrite.DefinedScrollView;
import view_rewrite.LoadingDialog;


public class TimeMachine extends Activity implements Runnable{

    private TextView page_2_text;
    private TextView page_3_text;
    private TextView page_4_text;

    private TimeMachineLog log;

    private LoadingDialog loadingDialog;

    private DefinedScrollView definedScrollView;

    private ImageView back;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingDialog.stopProgressDialog();
                    page_2_text.setText(log.getViewNumber());
                    page_3_text.setText(log.getCommentNumber());
                    page_4_text.setText(log.getCollectionNumber());
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
        setContentView(R.layout.activity_time_machine);

        definedScrollView=(DefinedScrollView)findViewById(R.id.verticalPager);
        back=(ImageView)findViewById(R.id.left_return_arrow);
        loadingDialog=new LoadingDialog(TimeMachine.this);

        page_2_text=(TextView)findViewById(R.id.time_machine_page_2_movie_mount);
        page_3_text=(TextView)findViewById(R.id.time_machine_page_3_movie_mount);
        page_4_text=(TextView)findViewById(R.id.time_machine_page_4_movie_mount);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(Network.checkNetWorkState(TimeMachine.this)){
            if(MineFragment.isLogin) {
                loadingDialog.startProgressDialog();
                new Thread(this).start();
            }else{
                LoginNotification.loginNotification(TimeMachine.this);
            }
        }
    }

    @Override
    public void run() {
        String keys[]={"number"};
        String parameters[]={MineFragment.userInfo.getNumber()};
        String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_time_machine.php",keys,parameters);
        if(result!=null&&!result.contains("error")){
            log=new TimeMachineLog(result);
            mHandler.sendEmptyMessage(0);
        }else{
            mHandler.sendEmptyMessage(1);
        }
    }
}
