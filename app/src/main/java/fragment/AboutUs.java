package fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhuchao.freetime.R;
import com.zhuchao.freetime.Set;

/**
 * Created by zhuchao on 7/19/15.
 */
public class AboutUs extends Fragment {

    private ImageView back_button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.setting_about_us,container,false);

        back_button=(ImageView)rootView.findViewById(R.id.left_return_arrow);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Set)getActivity()).back();
            }
        });
        return rootView;
    }
}
