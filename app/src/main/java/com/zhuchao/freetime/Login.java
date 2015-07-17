package com.zhuchao.freetime;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import bean.UserInfo;
import function.Network;
import function.NetworkFunction;
import view_rewrite.RippleImage;


public class Login extends Activity implements View.OnClickListener {
    public static  String TAG_QQ = "Activity_login";
    private Tencent mTencent;
    private static String mAppId;
    private QQToken qqToken;

    private String openId;
    private UserInfo userInfo;
    private RippleImage rippleImage;

    private Handler handler=new Handler(){
      public void handleMessage(Message message){
          switch (message.what){
              case 0:
                  try {
                      JSONObject object=new JSONObject(message.obj.toString());
                      userInfo.setNick_name(object.getString("nickname"));
                      userInfo.setNumber(openId);
                      userInfo.setHead_url(object.getString("figureurl_qq_2"));
                      userInfo.setSignature("");

                      //update user info to server
                      final String keys[]=new String[]{"number","nickname","headurl"};
                      final String parameters[]=new String[]{userInfo.getNumber(),userInfo.getNick_name(),userInfo.getHead_url()};
                      Log.d("UserInfo",parameters[0]+parameters[1]+parameters[2]);
                      if(Network.checkNetWorkState(Login.this)){
                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploaduserinfo.php",keys,parameters);
                                  Log.d("UserInfo",result);
                              }
                          }).start();
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
                  break;
          }
      }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();

    }

    private void initView(){
        //用来登录qq的rippleImage
        rippleImage = (RippleImage)findViewById(R.id.login_qq_login);
        rippleImage.setOnClickListener(this);

        userInfo=new UserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_qq_login:
                LoginQQ();
                break;
            default:
                break;
        }
    }
    private void LoginQQ(){
        //my application App_ID
        mAppId = AppConstant.APP_ID_QQ;
        mTencent = Tencent.createInstance(mAppId,getApplicationContext());
        mTencent.login(Login.this,"all",new BaseUiListener());
    }
    private class BaseUiListener implements IUiListener{

        @Override
        public void onComplete(Object response) {
            Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
            //successfully login qq, we will get QQ user's information
            try {
                openId=((JSONObject)response).getString("openid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            qqToken = mTencent.getQQToken();
            com.tencent.connect.UserInfo userInfo_qq = new com.tencent.connect.UserInfo(getApplicationContext(),qqToken);
            userInfo_qq.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    Log.e(TAG_QQ,"----userInfo_qq");
                    Message message=new Message();
                    message.what=0;
                    message.obj=response;
                    handler.sendMessage(message);
                }

                @Override
                public void onError(UiError uiError) {
                    Toast.makeText(getApplicationContext(),"Login failed,Please try again!",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(),"You have cancel login operation!",Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(getApplicationContext(),"Login failed,Please try again!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"You have cancel login operation!",Toast.LENGTH_SHORT).show();
        }
    }
}
