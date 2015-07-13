package bean;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;

import function.ParseJson;

/**
 * Created by zhuchao on 7/12/15.
 * This class is used to save user's information.
 */
public class UserInfo extends BaseObject implements Serializable, ParseJson {
    private ArrayList<BaseObject>userinfo;
    private String number;//user's account
    private String nick_name;//user's nick name;
    private String head_url;//user's head url
    private String signature;//user's signature
    private String stamp;//login stamp ensuring that one account only logins once;
    private static final long serialVersionUID=-7620435178023928254L;

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
        setUserinfo(c);
    }
    private void setUserinfo(String c){
        userinfo=getObjects(c);
        UserInfo info=(UserInfo)userinfo.get(0);
        setHead_url(info.getHead_url());
        setNumber(info.getNumber());
        setNick_name(info.getNick_name());
        setSignature(info.getSignature());
        setStamp(info.getStamp());
    }

    /**
     * Remember to complete this function.
     * @param c
     * @return
     */
    @Override
    public ArrayList<BaseObject> getObjects(String c) {
        return null;
    }
}