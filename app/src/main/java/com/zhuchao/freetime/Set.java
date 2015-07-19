package com.zhuchao.freetime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Window;

import fragment.AboutUs;
import fragment.JoinUs;
import fragment.Set_Main;


public class Set extends FragmentActivity {

    public AboutUs aboutUs;

    public JoinUs joinUs;

    private Set_Main main;

    public Fragment nowFragment;

    public FragmentManager manager;

    private FragmentTransaction transaction;

    public boolean isTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_set);

        initView();
    }
    private void initView(){
        manager=getSupportFragmentManager();

        transaction=manager.beginTransaction();

        main=new Set_Main();

        transaction.add(R.id.set_container, main);

        transaction.commit();

        nowFragment=main;

        isTop=true;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if(!isTop){
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.remove(nowFragment);
                    transaction.show(main);
                    nowFragment=main;
                    transaction.commit();
                    isTop=true;
                    return false;
                }else{
                    finish();
                }
        }
        return super.onKeyDown(keyCode, event);
    }
    public void back(){
        if(!isTop) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(nowFragment);
            transaction.show(main);
            nowFragment = main;
            transaction.commit();
            isTop = true;
        }
    }
}
