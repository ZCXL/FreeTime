package com.zhuchao.freetime;

import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.WindowManager;

public class Welcome extends ActionBarActivity implements Runnable{

    private boolean isFirst;//check whether use this software firstly.
    private final String setting="Setting";//get base setting name
    private SharedPreferences preferences;//get setting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.activity_welcome);
    }
    @Override
    public void run() {
        preferences=getSharedPreferences(setting,MODE_PRIVATE);
        isFirst=preferences.getBoolean("STARTFIRST",true);
        if(isFirst){//use firstly
            //preferences.edit().putBoolean("STARTFIRST",false);
        }else{

        }
    }
}
