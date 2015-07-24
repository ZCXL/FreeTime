package function;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by zhuchao on 7/22/15.
 */
public class LoginNotification {
    public static void loginNotification(Context context){
        new AlertDialog.Builder(context).setTitle("Login").setMessage("You must login on first!").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }
}
