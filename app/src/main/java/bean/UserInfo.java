package bean;
import java.io.Serializable;
/**
 * Created by zhuchao on 7/12/15.
 * This class is used to save user's information.
 */
public class UserInfo extends BaseObject implements Serializable {
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

    public UserInfo(String number,String nick_name,String head_url,String signature,String stamp){
        super(TYPE.USER);
        this.number=number;
        this.nick_name=nick_name;
        this.head_url=head_url;
        this.signature=signature;
        this.stamp=stamp;
    }

}
