package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;


/**
 * Created by zhuchao on 7/22/15.
 */
public class SelfLabel extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mine_self_label);
    }
}
