package function;

import android.content.Context;

import bean.Version;

/**
 * Created by zhuchao on 7/12/15.
 */
public class CheckVersion{
    private Context context;
    private OnVersionCheckListener listener;
    private Version version;
    public CheckVersion(Context context){
        this.context=context;
    }
    public void setOnVersionCheckListener(OnVersionCheckListener listener){
        this.listener=listener;
    }

    public OnVersionCheckListener getListener(){
        return listener;
    }

    /**
     * use this function to get all version info and pass last version
     */
    public void startCheck(){

    }
    /**
     * @author zhuchao
     * This interface is to return version info to user
     */
    public interface OnVersionCheckListener{
        /**
         * return last version
         * @param version
         */
        void getVersion(Version version);
    }
}
