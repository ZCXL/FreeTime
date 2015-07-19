package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuchao.freetime.Login;
import com.zhuchao.freetime.R;

import bean.UserInfo;
import function.ImageLoaderTask;
import function.ImageProcess;
import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/13/15.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private RoundImageView head;
    public static boolean isLogin=false;
    private UserInfo userInfo;
    private TextView userName;
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
    }
    private void initData(){
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_head_image:
                if(!isLogin){
                    getActivity().startActivityForResult(new Intent(getActivity(),Login.class),0);
                }
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
                    isLogin=true;
                }
            }
        }
    }
    private void updateUserInfo(){
        if(userInfo!=null){
            new ImageLoaderTask(head,getActivity(), ImageProcess.FileType_Image.HeadImage).execute(userInfo.getHead_url());
            userName.setText(userInfo.getNick_name());
        }
    }
}
