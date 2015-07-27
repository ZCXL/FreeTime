package com.zhuchao.freetime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fragment.MineFragment;
import function.LoginNotification;
import function.NetworkFunction;
import view_rewrite.LoadingDialog;
import view_rewrite.PressImage;


/**
 * Created by zhuchao on 7/22/15.
 */
public class SelfLabel extends Activity implements View.OnClickListener,Runnable{
    private Button confirm;
    private PressImage science,shy,food,animation,pet,creative,softhearted,idea,energy,fun,terrify,heavy,love;
    private ImageView back;
    private LoadingDialog loadingDialog;
    private boolean isCheck[]=new boolean[]{false,false,false,false,false,false,false,false,false,false,false,false,false};

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingDialog.stopProgressDialog();
                    Toast.makeText(SelfLabel.this,"Select successfully",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    loadingDialog.stopProgressDialog();
                    Toast.makeText(SelfLabel.this,"Select failed",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    loadingDialog.stopProgressDialog();
                    for(int i=0;i<13;i++)
                        initView(i);
                    break;
                case 3:
                    loadingDialog.stopProgressDialog();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.mine_self_label);

        loadingDialog=new LoadingDialog(this);
        confirm=(Button)findViewById(R.id.mine_self_label_confirm_button);
        science=(PressImage)findViewById(R.id.mine_self_label_science_button);
        shy=(PressImage)findViewById(R.id.mine_self_label_shy_button);
        food=(PressImage)findViewById(R.id.mine_self_label_food_button);
        animation=(PressImage)findViewById(R.id.mine_self_label_animation_button);
        pet=(PressImage)findViewById(R.id.mine_self_label_pet_button);
        creative=(PressImage)findViewById(R.id.mine_self_label_creative_button);
        softhearted=(PressImage)findViewById(R.id.mine_self_label_soft_hearted_button);
        idea=(PressImage)findViewById(R.id.mine_self_label_odd_idea_button);
        energy=(PressImage)findViewById(R.id.mine_self_label_positive_energy_button);
        fun=(PressImage)findViewById(R.id.mine_self_label_fun_button);
        terrify=(PressImage)findViewById(R.id.mine_self_label_terrify_button);
        heavy=(PressImage)findViewById(R.id.mine_self_label_heavy_taste_button);
        love=(PressImage)findViewById(R.id.mine_self_label_love_button);
        back=(ImageView)findViewById(R.id.left_return_arrow);

        confirm.setOnClickListener(this);
        science.setOnClickListener(this);
        shy.setOnClickListener(this);
        food.setOnClickListener(this);
        animation.setOnClickListener(this);
        pet.setOnClickListener(this);
        creative.setOnClickListener(this);
        softhearted.setOnClickListener(this);
        idea.setOnClickListener(this);
        energy.setOnClickListener(this);
        fun.setOnClickListener(this);
        terrify.setOnClickListener(this);
        heavy.setOnClickListener(this);
        love.setOnClickListener(this);
        back.setOnClickListener(this);

        if(MineFragment.isLogin){
            loadingDialog.startProgressDialog();
            new Thread(this).start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_self_label_science_button:
                if(!isCheck[2]) {
                    science.setImageResource(R.drawable.mine_self_label_science_button_checked);
                    isCheck[2]=true;
                }else {
                    science.setImageResource(R.drawable.mine_self_label_science_button);
                    isCheck[2]=false;
                }
                break;
            case R.id.mine_self_label_shy_button:
                if(!isCheck[11]) {
                    shy.setImageResource(R.drawable.mine_self_label_shy_button_checked);
                    isCheck[11]=true;
                }else {
                    shy.setImageResource(R.drawable.mine_self_label_shy_button);
                    isCheck[11]=false;
                }
                break;
            case R.id.mine_self_label_food_button:
                if(!isCheck[12]) {
                    food.setImageResource(R.drawable.mine_self_label_food_button_checked);
                    isCheck[12]=true;
                }else {
                    food.setImageResource(R.drawable.mine_self_label_food_button);
                    isCheck[12]=false;
                }
                break;
            case R.id.mine_self_label_animation_button:
                if(!isCheck[1]) {
                    animation.setImageResource(R.drawable.mine_self_label_animation_button_checked);
                    isCheck[1]=true;
                }else {
                    animation.setImageResource(R.drawable.mine_self_label_animation_button);
                    isCheck[1]=false;
                }
                break;
            case R.id.mine_self_label_pet_button:
                if(!isCheck[5]) {
                    pet.setImageResource(R.drawable.mine_self_label_pet_button_checked);
                    isCheck[5]=true;
                }else {
                    pet.setImageResource(R.drawable.mine_self_label_pet_button);
                    isCheck[5]=false;
                }
                break;
            case R.id.mine_self_label_creative_button:
                if(!isCheck[4]) {
                    creative.setImageResource(R.drawable.mine_self_label_creative_button_checked);
                    isCheck[4]=true;
                }else {
                    creative.setImageResource(R.drawable.mine_self_label_creative_button);
                    isCheck[4]=false;
                }
                break;
            case R.id.mine_self_label_soft_hearted_button:
                if(!isCheck[8]) {
                    softhearted.setImageResource(R.drawable.mine_self_label_soft_hearted_button_checked);
                    isCheck[8]=true;
                }else {
                    softhearted.setImageResource(R.drawable.mine_self_label_soft_hearted_button);
                    isCheck[8]=false;
                }
                break;
            case R.id.mine_self_label_odd_idea_button:
                if(!isCheck[3]) {
                    idea.setImageResource(R.drawable.mine_self_label_odd_idea_button_checked);
                    isCheck[3]=true;
                }else {
                    idea.setImageResource(R.drawable.mine_self_label_odd_idea_button);
                    isCheck[3]=false;
                }
                break;
            case R.id.mine_self_label_positive_energy_button:
                if(!isCheck[7]) {
                    energy.setImageResource(R.drawable.mine_self_label_positive_energy_button_checked);
                    isCheck[7]=true;
                }else {
                    energy.setImageResource(R.drawable.mine_self_label_positive_energy_button);
                    isCheck[7]=false;
                }
                break;
            case R.id.mine_self_label_fun_button:
                if(!isCheck[10]) {
                    fun.setImageResource(R.drawable.mine_self_label_fun_button_checked);
                    isCheck[10]=true;
                }else {
                    fun.setImageResource(R.drawable.mine_self_label_fun_button);
                    isCheck[10]=false;
                }
                break;
            case R.id.mine_self_label_terrify_button:
                if(!isCheck[9]) {
                    terrify.setImageResource(R.drawable.mine_self_label_terrify_button_checked);
                    isCheck[9]=true;
                }else {
                    terrify.setImageResource(R.drawable.mine_self_label_terrify_button);
                    isCheck[9]=false;
                }
                break;
            case R.id.mine_self_label_heavy_taste_button:
                if(!isCheck[6]) {
                    heavy.setImageResource(R.drawable.mine_self_label_heavy_taste_button_checked);
                    isCheck[6]=true;
                }else {
                    heavy.setImageResource(R.drawable.mine_self_label_heavy_taste_button);
                    isCheck[6]=false;
                }
                break;
            case R.id.mine_self_label_love_button:
                if(!isCheck[0]) {
                    love.setImageResource(R.drawable.mine_self_label_love_button_checked);
                    isCheck[0]=true;
                }else {
                    love.setImageResource(R.drawable.mine_self_label_love_button);
                    isCheck[0]=false;
                }
                break;
            case R.id.left_return_arrow:
                finish();
                break;
            case R.id.mine_self_label_confirm_button:
                if(MineFragment.isLogin){
                    loadingDialog.startProgressDialog();
                    new Thread(new UploadLabel()).start();
                }else{
                    LoginNotification.loginNotification(SelfLabel.this);
                }
                break;
        }
    }

    @Override
    public void run() {
        String keys[]={"number"};
        String parameters[]={MineFragment.userInfo.getNumber()};
        String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_user_interesting.php",keys,parameters);
        if(result!=null&&!result.contains("error")){
            try {
                JSONObject object=new JSONObject(result);
                JSONArray array=object.getJSONArray("I");
                for(int i=0;i<array.length();i++){
                    int number=array.getInt(i);
                    isCheck[Integer.valueOf(number)-1]=true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(2);
        }else{
            mHandler.sendEmptyMessage(3);
        }
    }
    private class UploadLabel implements Runnable{

        @Override
        public void run() {
            String keys[]={"number","interesting"};
            StringBuilder builder=new StringBuilder();
            builder.append("{");
            for(int i=0;i<13;i++){
                if(isCheck[i]){
                    builder.append("\""+String.valueOf(i+1)+"\":true,");
                }else{
                    builder.append("\""+String.valueOf(i+1)+"\":false,");
                }
            }
            builder.deleteCharAt(builder.length()-1);
            builder.append("}");
            String parameters[]={MineFragment.userInfo.getNumber(),builder.toString()};
            String result= NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/update_user_interesting.php",keys,parameters);
            if(result!=null&&!result.contains("error")){
                mHandler.sendEmptyMessage(0);
            }else{
                mHandler.sendEmptyMessage(1);
            }
        }
    }
    private void initView(int position){
        switch (position){
            case 0:
                if(isCheck[0]) {
                    love.setImageResource(R.drawable.mine_self_label_love_button_checked);
                }else {
                    love.setImageResource(R.drawable.mine_self_label_love_button);
                }
                break;
            case 1:
                if(isCheck[1]) {
                    animation.setImageResource(R.drawable.mine_self_label_animation_button_checked);
                }else {
                    animation.setImageResource(R.drawable.mine_self_label_animation_button);
                }
                break;
            case 2:
                if(isCheck[2]) {
                    science.setImageResource(R.drawable.mine_self_label_science_button_checked);
                }else {
                    science.setImageResource(R.drawable.mine_self_label_science_button);
                }
                break;
            case 3:
                if(isCheck[3]) {
                    idea.setImageResource(R.drawable.mine_self_label_odd_idea_button_checked);
                }else {
                    idea.setImageResource(R.drawable.mine_self_label_odd_idea_button);
                }
                break;
            case 4:
                if(isCheck[4]) {
                    creative.setImageResource(R.drawable.mine_self_label_creative_button_checked);
                }else {
                    creative.setImageResource(R.drawable.mine_self_label_creative_button);
                }
                break;
            case 5:
                if(isCheck[5]) {
                    pet.setImageResource(R.drawable.mine_self_label_pet_button_checked);
                }else {
                    pet.setImageResource(R.drawable.mine_self_label_pet_button);
                }
                break;
            case 6:
                if(isCheck[6]) {
                    heavy.setImageResource(R.drawable.mine_self_label_heavy_taste_button_checked);
                }else {
                    heavy.setImageResource(R.drawable.mine_self_label_heavy_taste_button);
                }
                break;
            case 7:
                if(isCheck[7]) {
                    energy.setImageResource(R.drawable.mine_self_label_positive_energy_button_checked);
                }else {
                    energy.setImageResource(R.drawable.mine_self_label_positive_energy_button);
                }
                break;
            case 8:
                if(isCheck[8]) {
                    softhearted.setImageResource(R.drawable.mine_self_label_soft_hearted_button_checked);
                }else {
                    softhearted.setImageResource(R.drawable.mine_self_label_soft_hearted_button);
                }
                break;
            case 9:
                if(isCheck[9]) {
                    terrify.setImageResource(R.drawable.mine_self_label_terrify_button_checked);
                }else {
                    terrify.setImageResource(R.drawable.mine_self_label_terrify_button);
                }
                break;
            case 10:
                if(isCheck[10]) {
                    fun.setImageResource(R.drawable.mine_self_label_fun_button_checked);
                }else {
                    fun.setImageResource(R.drawable.mine_self_label_fun_button);
                }
                break;
            case 11:
                if(isCheck[11]) {
                    shy.setImageResource(R.drawable.mine_self_label_shy_button_checked);
                }else {
                    shy.setImageResource(R.drawable.mine_self_label_shy_button);
                }
                break;
            case 12:
                if(isCheck[12]) {
                    food.setImageResource(R.drawable.mine_self_label_food_button_checked);
                }else {
                    food.setImageResource(R.drawable.mine_self_label_food_button);
                }
                break;
        }
    }
}
