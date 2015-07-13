package com.zhuchao.freetime;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import fragment.MineFragment;
import fragment.TopHotFragment;
import fragment.ZeroTimeFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private FragmentTransaction transaction;
    private FragmentManager manager;
    private ZeroTimeFragment zeroTimeFragment;
    private TopHotFragment topHotFragment;
    private MineFragment mineFragment;
    private ImageView zero_time,top_hot,mine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView(){
        //init button at main interface's bottom.
        zero_time=(ImageView)findViewById(R.id.main_zero_time);
        top_hot=(ImageView)findViewById(R.id.main_top_hot);
        mine=(ImageView)findViewById(R.id.main_mine);

        //set button's listener
        zero_time.setOnClickListener(this);
        top_hot.setOnClickListener(this);
        mine.setOnClickListener(this);

        //show zero time interface
        manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        zeroTimeFragment=new ZeroTimeFragment();
        transaction.add(R.id.container, zeroTimeFragment);
        transaction.commit();

    }

    /**
     * hide any fragment visible
     * @param transaction
     */
    private void hideAllFragment(FragmentTransaction transaction){
        if(zeroTimeFragment!=null){
            transaction.hide(zeroTimeFragment);
        }
        if(topHotFragment!=null){
            transaction.hide(topHotFragment);
        }
        if(mineFragment!=null){
            transaction.hide(mineFragment);
        }
    }
    private void setAllUnchecked(int i){
        switch (i){
            case 1:
                zero_time.setImageResource(R.drawable.main_bottom_icon_1_checked);
                top_hot.setImageResource(R.drawable.main_bottom_icon_2_unchecked);
                mine.setImageResource(R.drawable.main_bottom_icon_3_unchecked);
                break;
            case 2:
                zero_time.setImageResource(R.drawable.main_bottom_icon_1_unchecked);
                top_hot.setImageResource(R.drawable.main_bottom_icon_2_checked);
                mine.setImageResource(R.drawable.main_bottom_icon_3_unchecked);
                break;
            case 3:
                zero_time.setImageResource(R.drawable.main_bottom_icon_1_unchecked);
                top_hot.setImageResource(R.drawable.main_bottom_icon_2_unchecked);
                mine.setImageResource(R.drawable.main_bottom_icon_3_checked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (v.getId()){
            case R.id.main_zero_time:
                setAllUnchecked(1);
                zero_time.setBackgroundResource(R.drawable.main_bottom_icon_1_unchecked);
                if(zeroTimeFragment==null){
                    zeroTimeFragment=new ZeroTimeFragment();
                    fragmentTransaction.add(R.id.container,zeroTimeFragment);
                }else{
                    fragmentTransaction.show(zeroTimeFragment);
                }
                break;
            case R.id.main_top_hot:
                setAllUnchecked(2);
                if(topHotFragment==null){
                    topHotFragment=new TopHotFragment();
                    fragmentTransaction.add(R.id.container,topHotFragment);
                }else{
                    fragmentTransaction.show(topHotFragment);
                }
                break;
            case R.id.main_mine:
                setAllUnchecked(3);
                if(mineFragment==null){
                    mineFragment=new MineFragment();
                    fragmentTransaction.add(R.id.container,mineFragment);
                }else{
                    fragmentTransaction.show(mineFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }
}
