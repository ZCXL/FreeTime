package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuchao.freetime.Collection;
import com.zhuchao.freetime.Login;
import com.zhuchao.freetime.R;
import com.zhuchao.freetime.SelfLabel;
import com.zhuchao.freetime.Set;

import bean.UserInfo;
import function.ImageLoaderTask;
import function.ImageProcess;
import function.LoginNotification;
import view_rewrite.RippleLayout;
import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/13/15.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private RoundImageView head;
    public static boolean isLogin=false;
    //User info
    public static UserInfo userInfo;
    //User name
    private TextView userName;
    //Set button
    private ImageView set_button;
    //collect button
    private RippleLayout collect_button;
    //self label
    private RippleLayout label_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_mine,container,false);

        initView(rootView);

        initData();
        return rootView;
    }
    private void initView(View rootView){
        head=(RoundImageView)rootView.findViewById(R.id.mine_head_image);

        userName=(TextView)rootView.findViewById(R.id.mine_user_name);

        set_button=(ImageView)rootView.findViewById(R.id.mine_set);

        collect_button=(RippleLayout)rootView.findViewById(R.id.mine_collection_block);

        label_button=(RippleLayout)rootView.findViewById(R.id.mine_self_label_block);

        if(isLogin&&userInfo!=null){
            userName.setText(userInfo.getNick_name());
            new ImageLoaderTask(head,getActivity(), ImageProcess.FileType_Image.HeadImage).execute(userInfo.getHead_url());
        }
    }
    private void initData(){
        head.setOnClickListener(this);

        set_button.setOnClickListener(this);

        collect_button.setListener(this);

        label_button.setListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_head_image:
                if(!isLogin){
                    getActivity().startActivityForResult(new Intent(getActivity(),Login.class),0);
                }
                break;
            case R.id.mine_set:
                getActivity().startActivityForResult(new Intent(getActivity(), Set.class),1);
                break;
            case R.id.mine_collection_block:
                if(!isLogin)
                    LoginNotification.loginNotification(getActivity());
                else
                    getActivity().startActivity(new Intent(getActivity(), Collection.class));
                break;
            case R.id.mine_self_label_block:
                if(!isLogin)
                    LoginNotification.loginNotification(getActivity());
                else
                    getActivity().startActivity(new Intent(getActivity(), SelfLabel.class));
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent date){
        if(resultCode==0){
            Log.d("login","requestcode");
            if(date!=null){
                Log.d("login","date");
                Bundle bundle=date.getExtras();
                if(bundle!=null) {
                    Log.d("login","bundle");
                    userInfo = bundle.getParcelable("userinfo");
                    updateUserInfo();
                }
            }
        }else if(requestCode==1){
            if(!isLogin){
                userName.setText("Click to login");
                head.setImageResource(R.drawable.qq_login_button);
            }
        }
    }
    private void updateUserInfo(){
        if(userInfo!=null){
            new ImageLoaderTask(head,getActivity(), ImageProcess.FileType_Image.HeadImage).execute(userInfo.getHead_url());
            userName.setText(userInfo.getNick_name());
            isLogin=true;
        }
    }
}
