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
import view_rewrite.RippleImage;


public class Login extends Activity implements View.OnClickListener {
    public static  String TAG_QQ = "Activity_login";
    private Tencent mTencent;
    public static String mAppId;
    public static String openidString;
    public static String nicknameString;
    public QQToken qqToken;
    Bitmap bitmap = null;

    private RippleImage rippleImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //用来登录qq的rippleImage
        rippleImage = (RippleImage)findViewById(R.id.login_qq_login);
        rippleImage.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_qq_login:
                LoginQQ();

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
            try{
                Log.e(TAG_QQ,"--------" + response.toString());
                openidString = ((JSONObject) response).getString("openid");
                Log.e(TAG_QQ,"--------" + openidString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //successfully login qq, we will get QQ user's information
            qqToken = mTencent.getQQToken();
            com.tencent.connect.UserInfo userInfo_qq = new com.tencent.connect.UserInfo(getApplicationContext(),qqToken);
            userInfo_qq.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    Log.e(TAG_QQ,"----userInfo_qq");

                }

                @Override
                public void onError(UiError uiError) {

                }

                @Override
                public void onCancel() {

                }
            });
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }
}
