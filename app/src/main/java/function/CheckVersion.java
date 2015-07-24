package function;

import android.content.Context;
import android.util.Log;

import com.zhuchao.freetime.Welcome;

import java.util.ArrayList;

import bean.BaseObject;
import bean.Version;
import bean.Versions;

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
     * Created by LMZ on 7/14/15
     */
    public void startCheck(){
        Welcome.signal-=1;
        new Thread(new Runnable(){
            @Override
            public void run(){
                if(Network.checkNetWorkState(context)){
                    String url = "http://123.56.85.58/FreeTime/code/check_version.php";
                    String parameter[] = new String[]{};
                    String keys[] = new String[]{};
                    String result = NetworkFunction.ConnectServer(url,keys,parameter);//联网解析JSON
                    if (result != null) {
                        Versions versions = new Versions(result);
                        if(versions==null)
                            return;
                        version=(Version) versions.getItem(0);

                        //ArrayList<BaseObject> versionArray = versions.getObjects(result);
                        int totalCount = versions.getCount();
                        int index = 0;
                        if (totalCount >= 1) {
                            version = (Version) versions.getItem(0);
                            int max = Integer.parseInt(version.getVersionId().replace(".", ""));
                            for (int i = 0; i < totalCount; i++) {
                                version = (Version) versions.getItem(i);
                                int tmp = Integer.parseInt(version.getVersionId().replace(".", ""));
                                if (tmp > max) {
                                    max = tmp;
                                    index = i;
                                }
                            }
                            version = (Version) versions.getItem(index);
                            if (listener != null)
                                listener.getVersion(version);
                            Welcome.signal+=1;
                        }
                    }
                }
            }
        }).start();
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
