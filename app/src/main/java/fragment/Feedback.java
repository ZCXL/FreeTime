package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuchao.freetime.R;

/**
 * Created by zhuchao on 7/20/15.
 */
public class Feedback extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.setting_problem_feedback,container,false);

        return rootView;
    }
}
