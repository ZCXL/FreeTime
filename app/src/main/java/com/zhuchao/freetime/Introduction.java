package com.zhuchao.freetime;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import java.util.ArrayList;


public class Introduction extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;

    private ArrayList<View>list;
    private View page1,page2,page3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_introduction);
        initView();
    }

    private void initView(){

    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
