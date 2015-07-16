package fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuchao.freetime.Login;
import com.zhuchao.freetime.R;

import view_rewrite.RoundImageView;

/**
 * Created by zhuchao on 7/13/15.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private RoundImageView head;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_mine,container,false);

        initView(rootView);
        initData();
        return rootView;
    }
    private void initView(View rootView){
        head=(RoundImageView)rootView.findViewById(R.id.mine_head_image);
    }
    private void initData(){
        head.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mine_head_image:
                getActivity().startActivity(new Intent(getActivity(), Login.class));
                break;
        }
    }
}
