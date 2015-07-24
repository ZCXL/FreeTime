package view_rewrite;

import android.content.Context;

/**
 * Created by zhuchao on 7/22/15.
 */
public class LoadingDialog {
    private Context context;
    private CustomProgressDialog dialog;
    public LoadingDialog(Context context){
        this.context=context;
    }
    public void startProgressDialog(){
        if (dialog == null){
            dialog= CustomProgressDialog.createDialog(context);
        }
        dialog.show();
    }

    public void stopProgressDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
