package com.zhuchao.freetime;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fragment.MineFragment;
import fragment.TopHotFragment;
import fragment.ZeroTimeFragment;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private FragmentTransaction transaction;
    private FragmentManager manager;
    private ZeroTimeFragment zeroTimeFragment;
    private TopHotFragment topHotFragment;
    private MineFragment mineFragment;
    private RelativeLayout zero_time,top_hot,mine;
    private TextView zero_time_text,top_hot_text,mine_text;
    private ImageView zero_time_image,top_hot_image,mine_image;

    private String curFragmentTag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();
    }
    private void initView(){
        //init button at main interface's bottom.
        zero_time=(RelativeLayout)findViewById(R.id.main_zero_time_layout);
        top_hot=(RelativeLayout)findViewById(R.id.main_top_hot_layout);
        mine=(RelativeLayout)findViewById(R.id.main_mine_layout);

        //init image at bottom.
        zero_time_image=(ImageView)findViewById(R.id.main_zero_time);
        top_hot_image=(ImageView)findViewById(R.id.main_top_hot);
        mine_image=(ImageView)findViewById(R.id.main_mine);

        //init text at bottom.
        zero_time_text=(TextView)findViewById(R.id.main_zero_time_text);
        top_hot_text=(TextView)findViewById(R.id.main_top_hot_text);
        mine_text=(TextView)findViewById(R.id.main_mine_text);
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
                zero_time_image.setImageResource(R.drawable.main_bottom_icon_1_checked);
                top_hot_image.setImageResource(R.drawable.main_bottom_icon_2_unchecked);
                mine_image.setImageResource(R.drawable.main_bottom_icon_3_unchecked);
                zero_time_text.setTextColor(getResources().getColor(R.color.main_bottom_text_checked_color));
                top_hot_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                mine_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                break;
            case 2:
                zero_time_image.setImageResource(R.drawable.main_bottom_icon_1_unchecked);
                top_hot_image.setImageResource(R.drawable.main_bottom_icon_2_checked);
                mine_image.setImageResource(R.drawable.main_bottom_icon_3_unchecked);
                zero_time_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                top_hot_text.setTextColor(getResources().getColor(R.color.main_bottom_text_checked_color));
                mine_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                break;
            case 3:
                zero_time_image.setImageResource(R.drawable.main_bottom_icon_1_unchecked);
                top_hot_image.setImageResource(R.drawable.main_bottom_icon_2_unchecked);
                mine_image.setImageResource(R.drawable.main_bottom_icon_3_checked);
                zero_time_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                top_hot_text.setTextColor(getResources().getColor(R.color.main_bottom_text_unchecked_color));
                mine_text.setTextColor(getResources().getColor(R.color.main_bottom_text_checked_color));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        hideAllFragment(fragmentTransaction);
        switch (v.getId()){
            case R.id.main_zero_time_layout:
                setAllUnchecked(1);
                if(zeroTimeFragment==null){
                    zeroTimeFragment=new ZeroTimeFragment();
                    fragmentTransaction.add(R.id.container,zeroTimeFragment,"main_zero_time");
                }else{
                    fragmentTransaction.show(zeroTimeFragment);
                }
                curFragmentTag="main_zero_time";
                break;
            case R.id.main_top_hot_layout:
                setAllUnchecked(2);
                if(topHotFragment==null){
                    topHotFragment=new TopHotFragment();
                    fragmentTransaction.add(R.id.container,topHotFragment,"main_top");
                }else{
                    fragmentTransaction.show(topHotFragment);
                }
                curFragmentTag="main_top";
                break;
            case R.id.main_mine_layout:
                setAllUnchecked(3);
                if(mineFragment==null){
                    mineFragment=new MineFragment();
                    fragmentTransaction.add(R.id.container,mineFragment,"main_mine");
                }else{
                    fragmentTransaction.show(mineFragment);
                }
                curFragmentTag="main_mine";
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*在这里，我们通过碎片管理器中的Tag，就是每个碎片的名称，来获取对应的fragment*/
        Fragment f = manager.findFragmentByTag(curFragmentTag);
        /*然后在碎片中调用重写的onActivityResult方法*/
        f.onActivityResult(requestCode, resultCode, data);
    }
}
