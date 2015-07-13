package function;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Use this class to get Network info.
 * Created by zhuchao on 7/12/15.
 */
public class Network {
    /**
     * network type convert to number(0,1,2)
     */
    public static final int NETWORNTYPE_NONE = 0;
    public static final int NETWORNTYPE_WIFI = 1;
    public static final int NETWORNTYPE_MOBILE = 2;
    /**
     * Check network connection state
     * @param context
     * @return boolean isConnected WIFI(1)=true
     * Created by LMZ on 7/13/15
     */
    public static boolean checkNetWorkState(Context context){
        boolean isConnected = false;
        int netWorkState = getNetworkType(context);
        if (netWorkState == 1||netWorkState == 2){
            isConnected = true;
        }else{
            isConnected=false;
        }
        return isConnected;
    }

    /**
     * Check network type,for example,wifi.
     * @param context
     * @return int mNetWorkType{0,1,2}
     * Created by LMZ on 7/13/15
     */
    public static int getNetworkType(Context context){
        int mNetWorkType = 0;
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                mNetWorkType = NETWORNTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")){
                mNetWorkType = NETWORNTYPE_MOBILE;
            }
        } else {
            mNetWorkType = NETWORNTYPE_NONE;
        }
        return mNetWorkType;
    }
}
