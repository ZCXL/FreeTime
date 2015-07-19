package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuchao.freetime.R;
import com.zhuchao.freetime.Test;

import view_rewrite.RippleImage;

/**
 * Created by zhuchao on 7/13/15.
 */
public class ZeroTimeFragment extends Fragment {
    private RippleImage rippleImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_zero_time,container,false);
        rippleImage=(RippleImage)rootView.findViewById(R.id.main_zero_time_share);
        rippleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), Test.class));
            }
        });
        return rootView;
    }
}
