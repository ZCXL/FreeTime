package view_rewrite;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;

import com.zhuchao.freetime.R;

public class CustomProgressDialog extends Dialog {
	private Context context = null;
    private static CustomProgressDialog customProgressDialog = null;
//    private Animation animation;
	public CustomProgressDialog(Context context){
        super(context);
        this.context = context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }
	public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context=context;
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }
	public static CustomProgressDialog createDialog(Context context){
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.customerprogressdialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        return customProgressDialog;
    }
    public void onWindowFocusChanged(boolean hasFocus){
        if (customProgressDialog == null){
	            return;
        }
        CircularProgressBar circularProgressBar = (CircularProgressBar) customProgressDialog.findViewById(R.id.customer_progress_dialog_progressBar);
        circularProgressBar.startAnimation();
    }
    public CustomProgressDialog setTitile(String strTitle){
        return customProgressDialog;
    }
    public CustomProgressDialog setMessage(String strMessage){
        return customProgressDialog;
    }
}
