package com.zhuchao.freetime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bean.BaseObject;
import bean.UserInfo;
import function.Network;
import function.NetworkFunction;
import function.SaveAndOpenUserInfo;
import openapi.UsersAPI;
import view_rewrite.LoadingDialog;
import view_rewrite.RippleImage;
import view_rewrite.SlideDown;


public class Login extends Activity implements View.OnClickListener , SlideDown.OnSlideDownListener{
    public static  String TAG_QQ = "Activity_login_qq";
    public static  String TAG_Weibo = "Activity_login_weibo";
    //member for qq
    private Tencent mTencent;
    private static String mAppId;
    private QQToken qqToken;
    private String openId;
    private UserInfo userInfo;
    private RippleImage rippleImage_QQ;
    //member for weibo
    private AuthInfo mAuthInfo;//微博 Web 授权接口类，提供登陆等功能
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;//SSO授权认证实例
    private static String mAppKeyWeibo;
    private static String mRedirectUrl;
    private static String mScopeWeibo;
    private Long uid;
    private String mCode;
    private RippleImage rippleImage_Weibo;
    private LoadingDialog loadingDialog;
    private SlideDown slideDown;
    private SaveAndOpenUserInfo saveAndOpenUserInfo=new SaveAndOpenUserInfo();

    private Handler handler=new Handler(){
      public void handleMessage(Message message){
          switch (message.what){
              case 0:
                  Log.e(TAG_QQ,"-----qq message----"+message.toString());
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
                          loadingDialog.startProgressDialog();
                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploaduserinfo.php",keys,parameters);
                                  if(!result.contains("error")){
                                      userInfo=new UserInfo(result);
                                      handler.sendEmptyMessage(1);
                                  }else{
                                      handler.sendEmptyMessage(2);
                                  }
                                  Log.d("UserInfo",result);
                              }
                          }).start();
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }
                  break;
              case 1:
                  loadingDialog.stopProgressDialog();
                  ArrayList<BaseObject>userinfos=new ArrayList<BaseObject>();
                  userinfos.add(userInfo);
                  saveAndOpenUserInfo.Save(Login.this,userinfos);
                  Intent intent=new Intent();
                  Bundle bundle=new Bundle();
                  if(userInfo!=null){
                      bundle.putParcelable("userinfo",userInfo);
                  }
                  intent.putExtras(bundle);
                  setResult(0,intent);
                  finish();
                  break;
              case 2:
                  loadingDialog.stopProgressDialog();
                  Toast.makeText(Login.this,"Login failed,please try again!",Toast.LENGTH_LONG).show();
                  break;
              case 3:
                  Log.e(TAG_Weibo,"-----Weibo message----"+message.toString());
                  try {
                      JSONObject object=new JSONObject(message.obj.toString());
                      userInfo.setNick_name(object.getString("screen_name"));
                      userInfo.setNumber(uid.toString());
                      userInfo.setHead_url(object.getString("profile_image_url"));
                      userInfo.setSignature("");

                      //update user info to server
                      final String keys[]=new String[]{"number","nickname","headurl"};
                      final String parameters[]=new String[]{userInfo.getNumber(),userInfo.getNick_name(),userInfo.getHead_url()};
                      Log.d("UserInfo",parameters[0]+parameters[1]+parameters[2]);
                      if(Network.checkNetWorkState(Login.this)){
                          loadingDialog.startProgressDialog();
                          new Thread(new Runnable() {
                              @Override
                              public void run() {
                                  String result=NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/uploaduserinfo.php",keys,parameters);
                                  if(!result.contains("error")){
                                      userInfo=new UserInfo(result);
                                      handler.sendEmptyMessage(1);
                                  }else{
                                      handler.sendEmptyMessage(2);
                                  }
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

        loadingDialog=new LoadingDialog(this);
        initView();

    }

    private void initView(){
        //qq' rippleImage
        rippleImage_QQ = (RippleImage)findViewById(R.id.login_qq_login);
        rippleImage_QQ.setOnClickListener(this);

        //weibo's rippleImage
        rippleImage_Weibo = (RippleImage)findViewById(R.id.login_weibo_login);
        rippleImage_Weibo.setOnClickListener(this);

        slideDown=(SlideDown)findViewById(R.id.login_casual_login);
        slideDown.setSlideDownListener(this);

        userInfo=new UserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_qq_login:
                LoginQQ();
                break;
            case R.id.login_weibo_login:
                LoginWeibo();
                break;
            default:
                break;
        }
    }

    /**
     * login weibo
     * Created by LMZ on 7/21/15
     */
    private void LoginWeibo() {
        //data for weibo oauth
        mAppKeyWeibo = AppConstant.APP_KEY_WEIBO;
        mRedirectUrl = AppConstant.REDIRECT_URL_WEIBO;
        mScopeWeibo = AppConstant.SCOPE_WEIBO;
        //instance for weibo
        mAuthInfo = new AuthInfo(this,mAppKeyWeibo,mRedirectUrl,mScopeWeibo);
        mSsoHandler = new SsoHandler(Login.this,mAuthInfo);
        //SSO oauth call-back
        mSsoHandler.authorize(new AuthListener());
    }

    @Override
    public void onSlideDown() {
        finish();
    }

    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle bundle) {
            Log.d(TAG_Weibo,"----bundle----"+bundle.toString());
            //analysis for bundle
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()){
                Log.d(TAG_Weibo, "----mAccessToken----" + mAccessToken.toString());
                AccessTokenKeeper.writeAccessToken(getApplicationContext(), mAccessToken);
                mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
                Log.d(TAG_Weibo, "---mAccessToken---" + mAccessToken.toString());
                //get userInfo' json for weibo
                UsersAPI mUsersAPI = new UsersAPI(getApplicationContext(),mAppKeyWeibo,mAccessToken);
                uid = Long.parseLong(mAccessToken.getUid());
                mUsersAPI.show(uid, new RequestListener() {
                    @Override
                    public void onComplete(String response) {
                        if (!TextUtils.isEmpty(response)){
                            Log.d(TAG_Weibo,"---user s---"+response);
                            Message message=new Message();
                            message.what=3;
                            message.obj=response;
                            handler.sendMessage(message);
                        }
                        else {
                            Log.e(TAG_Weibo,"---user is null---");
                        }
                    }

                    @Override
                    public void onWeiboException(WeiboException e) {
                        Toast.makeText(getApplicationContext(),"Login failed,Please try again!",Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e(TAG_Weibo, "---mUsersAPI---" + mUsersAPI.toString());
                Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
            }
            else {
                mCode = bundle.getString("code");
                Log.d(TAG_Weibo,"---mCode---"+mCode);
                String error = "Oauth failed";
                if (!TextUtils.isEmpty(mCode)) {
                    error= error + "\nObtained the code: " + mCode;
                }
                Toast.makeText(Login.this, error, Toast.LENGTH_LONG).show();
            }

        }
        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),"Login failed,Please try again!",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"You have cancel login operation!",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            Log.d(TAG_Weibo, "---mSsoHandler-----"+mSsoHandler.toString());
        }
        if (mSsoHandler == null){
            Log.e(TAG_Weibo,"---mSsoHandler null onActivity-----");
        }
    }

    /**
     * login qq
     * Created by LMZ on 7/21/15
     */
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
                    Log.d(TAG_QQ,"----get userInfo_qq----");
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
