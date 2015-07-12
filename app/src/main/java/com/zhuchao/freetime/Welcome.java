package com.zhuchao.freetime;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.content.Intent;

import android.view.Window;

import bean.Version;
import function.CheckVersion;

public class Welcome extends ActionBarActivity implements Runnable{

    private boolean isFirst;//check whether use this software firstly.
    private final String setting="Setting";//get base setting name
    private SharedPreferences preferences;//get setting
    private CheckVersion checkVersion;
    private Version version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);
    }
    @Override
    public void run() {
        //input code to check version
        checkVersion=new CheckVersion(Welcome.this);
        checkVersion.setOnVersionCheckListener(new CheckVersion.OnVersionCheckListener() {
            @Override
            public void getVersion(Version version) {
                Welcome.this.version=version;//this version will pass to MainActivity to judge update or not.
            }
        });
        checkVersion.startCheck();

        preferences=getSharedPreferences(setting,MODE_PRIVATE);
        isFirst=preferences.getBoolean("STARTFIRST",true);
        if(isFirst){//use firstly
            preferences.edit().putBoolean("STARTFIRST",false).commit();
            startActivity(new Intent(Welcome.this, Introduction.class));
        }else{
            //load user info
            //load resources saved in SDCard
            startActivity(new Intent(Welcome.this,MainActivity.class));
            overridePendingTransition(R.anim.loading_out,R.anim.loading_in);
        }
    }
}
