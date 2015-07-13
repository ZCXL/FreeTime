package com.zhuchao.freetime;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import adapter.ViewPagerAdapter;
import animation.DepthPageTransformer;
import bean.Version;


public class Introduction extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private ArrayList<View>list;
    private View page1,page2,page3;
    private ImageView icon1,icon2,icon3;
    private ImageView start_button;
    private Version version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_introduction);

        getVersion(savedInstanceState);

        initView();

        initData();


    }

    private void getVersion(Bundle bundle){
        if(bundle!=null){
            version=(Version)bundle.getSerializable("version");
        }
    }
    private void initView(){
        LayoutInflater inflater=LayoutInflater.from(this);
        //Init there icon at bottom.

        icon1=(ImageView)findViewById(R.id.introduction_icon_1);
        icon2=(ImageView)findViewById(R.id.introduction_icon_2);
        icon3=(ImageView)findViewById(R.id.introduction_icon_3);

        //Init there pages.
        page1=inflater.inflate(R.layout.introduction_page_1,null);
        page2=inflater.inflate(R.layout.introduction_page_2,null);
        page3=inflater.inflate(R.layout.introduction_page_3,null);

        start_button=(ImageView)page3.findViewById(R.id.introdution_start_button);//start button.

        viewPager=(ViewPager)findViewById(R.id.introduction_viewPager);
        list=new ArrayList<View>();
        adapter=new ViewPagerAdapter(list);


        //Create quick start button in menu.
        Intent intent=new Intent();

        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.logo);

        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);

        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "FreeTime");

        Intent i = new Intent();

        i.setAction(Intent.ACTION_MAIN);

        i.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName component = new ComponentName(this,Welcome.class);
        i.setComponent(component);
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, i);
        sendBroadcast(intent);
        Toast.makeText(Introduction.this, "Have create quick start.", Toast.LENGTH_LONG).show();

    }

    /**
     * Init layout data.
     */
    private void initData(){
        viewPager.setOnPageChangeListener(this);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Introduction.this,MainActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("version",version);
                intent.putExtras(bundle);
                startActivity(intent);
                Introduction.this.finish();
            }
        });
        viewPager.setAdapter(adapter);
        list.add(page1);
        list.add(page2);
        list.add(page3);
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0);

    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        Clear();
        switch (i){
            case 0:
                icon1.setImageResource(R.drawable.lead_icon_checked);
                break;
            case 1:
                icon2.setImageResource(R.drawable.lead_icon_checked);
                break;
            case 2:
                icon3.setImageResource(R.drawable.lead_icon_checked);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    /**
     * Cleat the bg of there icon at bottom.
     */
    private void Clear(){
        icon1.setImageResource(R.drawable.lead_icon_unchecked);
        icon2.setImageResource(R.drawable.lead_icon_unchecked);
        icon3.setImageResource(R.drawable.lead_icon_unchecked);
    }
}
