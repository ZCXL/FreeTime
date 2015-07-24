package com.zhuchao.freetime;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;

import android.util.Log;
import android.view.Window;

import bean.Movie;
import bean.Movies;
import bean.UserInfo;
import bean.Version;
import fragment.MineFragment;
import fragment.ZeroTimeFragment;
import function.CheckVersion;
import function.Network;

public class Welcome extends Activity implements Runnable{

    private boolean isFirst;//check whether use this software firstly.
    private final String setting="Setting";//get base setting name
    private SharedPreferences preferences;//get setting
    private CheckVersion checkVersion;
    private Version version;

    private UserInfo userInfo;
    private Movies movies;
    public static int signal=0;//thread synthesize

    private boolean initOver=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
        new Thread(this).start();
    }
    @Override
    public void run() {
        //input code to check version
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Check network connection
//        if(!Network.checkNetWorkState(Welcome.this)){
//
//        }
        checkVersion=new CheckVersion(Welcome.this);
        checkVersion.setOnVersionCheckListener(new CheckVersion.OnVersionCheckListener() {
            @Override
            public void getVersion(Version version) {
                Welcome.this.version=version;//this version will pass to MainActivity to judge update or not.
            }
        });
        checkVersion.startCheck();

        //thread
        while(signal<0||!initOver){
            doNothing();
            if(!initOver){
                movies=new Movies(Welcome.this);
                userInfo=new UserInfo(Welcome.this);
                initOver=true;
            }
        }
        preferences=getSharedPreferences(setting,MODE_PRIVATE);
        isFirst=preferences.getBoolean("START_FIRST",true);
        if(isFirst){//use firstly
            preferences.edit().putBoolean("START_FIRST",false).commit();
            Intent intent=new Intent(Welcome.this,Introduction.class);
            Bundle bundle=new Bundle();
            bundle.putParcelable("version",version);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(R.anim.out_to_left,R.anim.in_from_right);
            Welcome.this.finish();
        }else{
            //load user info
            if(userInfo!=null&&userInfo.getNumber()!=null){
                Log.d("tell me why",userInfo.getHead_url());
                MineFragment.isLogin=true;
                MineFragment.userInfo=userInfo;
            }
            if(movies!=null&&movies.getCount()>0){
                for(int i=0,j=movies.getCount();i<j;i++){
                    ZeroTimeFragment.movies.add((Movie)movies.getItem(i));
                }
            }
            startActivity(new Intent(Welcome.this, MainActivity.class));
            overridePendingTransition(R.anim.loading_in, R.anim.loading_out);
            Welcome.this.finish();
        }
    }
    private void doNothing(){

    }
}
