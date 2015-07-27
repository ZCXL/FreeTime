package view_rewrite;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zhuchao.freetime.R;

import bean.CommentBack;
import fragment.MineFragment;
import function.LoginNotification;
import function.Network;
import function.NetworkFunction;

/**
 * Created by zhuchao on 7/25/15.
 */
public class EditWindow extends Dialog implements Runnable{
    private String commented_number,comment_time,movieId;

    private View view;
    private Context context;
    private EditText editText;
    private Button send;
    private CommentBack back;
    private int position;

    private OnCommentListener onCommentListener;

    private LoadingDialog loadingDialog;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingDialog.stopProgressDialog();
                    if(onCommentListener!=null) {
                        onCommentListener.onSuccess(back, position-1);
                        dismiss();
                    }
                    break;
                case 1:
                    loadingDialog.stopProgressDialog();
                    if(onCommentListener!=null)
                        onCommentListener.onError();
                    break;
            }
        }
    };
    public EditWindow(Context context){
        super(context,android.R.style.Theme_Translucent);
        this.context=context;
        loadingDialog=new LoadingDialog(context);
    }
    @Override
    public void dismiss(){
        super.dismiss();
        InputMethodManager m=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        view= LayoutInflater.from(context).inflate(R.layout.comment_back_edit,null);
        editText=(EditText)view.findViewById(R.id.comment_back_edit);
        send=(Button)view.findViewById(R.id.comment_back_send);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditWindow.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Network.checkNetWorkState(context)) {
                    loadingDialog.startProgressDialog();
                    new Thread(EditWindow.this).start();
                }
            }
        });
    }
    @Override
    public void show(){
        super.show();
        editText.selectAll();
    }
    public void setParameters(String commented_number,String comment_time,String movieId,int position){
        this.commented_number=commented_number;
        this.comment_time=comment_time;
        this.movieId=movieId;
        this.position=position;
    }

    @Override
    public void run() {
        if (MineFragment.isLogin) {
            loadingDialog.startProgressDialog();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String con = editText.getText().toString();
                    if (con != null && !con.equals("")) {
                        String keys[] = {"movieid", "number", "comment", "commentnumber", "commentednumber", "commenttime"};
                        String parameters[] = {movieId,commented_number, con, MineFragment.userInfo.getNumber(), commented_number, comment_time};
                        String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/comment_back.php", keys, parameters);
                        if (result != null && !result.contains("error")) {
                            back = new CommentBack(result);
                            Log.d("tell me why", back.getComment());
                            mHandler.sendEmptyMessage(0);
                        } else {
                            mHandler.sendEmptyMessage(1);
                        }
                    } else {
                        Toast.makeText(context, "Comment can't be null", Toast.LENGTH_SHORT).show();
                    }
                }
            }).start();
        } else {
            LoginNotification.loginNotification(context);
        }
    }


    public OnCommentListener getOnCommentListener() {
        return onCommentListener;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }
    /**
     * comment back interface
     */
    public interface OnCommentListener{
        void onSuccess(CommentBack back,int position);
        void onError();
    }

}
