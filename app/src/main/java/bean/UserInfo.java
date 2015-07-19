package bean;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import function.ParseJson;
import function.SaveAndOpenUserInfo;

/**
 * Created by zhuchao on 7/12/15.
 * This class is used to save user's information.
 */
public class UserInfo extends BaseObject implements Parcelable, ParseJson {
    private static String TAG="ProcessJson";
    private ArrayList<BaseObject>userinfo;
    private String number;//user's account
    private String nick_name;//user's nick name;
    private String head_url;//user's head url
    private String signature;//user's signature
    private String stamp;//login stamp ensuring that one account only logins once;
    private SaveAndOpenUserInfo saveAndOpenUserInfo;

    public String getNumber() {
        return number;
    }

    public String getHead_url() {
        return head_url;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getSignature() {
        return signature;
    }

    public String getStamp() {
        return stamp;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public UserInfo(){
        super(TYPE.USER);
    }
    public UserInfo(String c){
        super(TYPE.USER);
        setUserInfo(c);
    }
    private void setUserInfo(String c){
        userinfo=getObjects(c);
        if(userinfo!=null){
            UserInfo info=(UserInfo)userinfo.get(0);
            setHead_url(info.getHead_url());
            setNumber(info.getNumber());
            setNick_name(info.getNick_name());
            setSignature(info.getSignature());
            setStamp(info.getStamp());
        }
    }
    public UserInfo(Context context){
        super(TYPE.USER);
        saveAndOpenUserInfo=new SaveAndOpenUserInfo();
        userinfo=saveAndOpenUserInfo.Open(context);
        if(userinfo!=null){
            UserInfo info=(UserInfo)userinfo.get(0);
            setHead_url(info.getHead_url());
            setNumber(info.getNumber());
            setNick_name(info.getNick_name());
            setSignature(info.getSignature());
            setStamp(info.getStamp());
        }
    }

    /**
     * Remember to complete this function.
     * @param c
     * @return
     */
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        /**
         * deal with String c for userInfo
         * Created by LMZ on 7/14/15
         */
        ArrayList<BaseObject> list = new ArrayList<BaseObject>();
        UserInfo userInfo;
        try {
            JSONObject object = new JSONObject(c);
            String U = object.getString("U");
            JSONObject user = new JSONObject(U);
            userInfo=new UserInfo();
                userInfo.setNumber(user.getString("number"));
                userInfo.setHead_url(user.getString("headurl"));
                userInfo.setNick_name(user.getString("nickname"));
                userInfo.setSignature(user.getString("signature"));
                userInfo.setStamp(user.getString("stamp"));
                list.add(userInfo);
            return list;
        } catch (JSONException e) {
            Log.d(TAG, e.toString() + "userInfo of json fault");
            return null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(nick_name);
        dest.writeString(head_url);
        dest.writeString(signature);
        dest.writeString(stamp);
    }
    public static final Parcelable.Creator<UserInfo>CREATOR=new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    private UserInfo(Parcel in){
        super(TYPE.USER);
        number=in.readString();
        nick_name=in.readString();
        head_url=in.readString();
        signature=in.readString();
        stamp=in.readString();
    }
}
