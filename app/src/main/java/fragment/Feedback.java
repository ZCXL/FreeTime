package fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.zhuchao.freetime.R;
import com.zhuchao.freetime.Set;

import view_rewrite.LoadingDialog;

/**
 * Created by zhuchao on 7/20/15.
 */
public class Feedback extends Fragment{
    private LoadingDialog loadingDialog;
    private Button confirm;
    private EditText edit;
    private ImageView back;
    Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    loadingDialog.stopProgressDialog();
                    Toast.makeText(getActivity(),"Feedback successfully",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.setting_problem_feedback,container,false);

        loadingDialog=new LoadingDialog(getActivity());
        back=(ImageView)rootView.findViewById(R.id.left_return_arrow);
        confirm=(Button)rootView.findViewById(R.id.feedback_confirm);
        edit=(EditText)rootView.findViewById(R.id.feedback_edit);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startProgressDialog();
                if(edit.getText().toString()!=null&&!edit.getText().equals("")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                handler.sendEmptyMessage(0);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    Toast.makeText(getActivity(),"Feedback can't be null",Toast.LENGTH_SHORT).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Set)getActivity()).back();
            }
        });
        return rootView;
    }
}
