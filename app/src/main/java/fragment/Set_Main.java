package fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhuchao.freetime.MainActivity;
import com.zhuchao.freetime.R;
import com.zhuchao.freetime.Set;

import bean.Version;
import function.CheckVersion;
import function.ImageProcess;
import function.SaveAndOpenUserInfo;
import view_rewrite.CustomProgressDialog;

/**
 * Created by zhuchao on 7/19/15.
 */
public class Set_Main extends Fragment implements View.OnClickListener{

    private Set set;

    private ImageView back_button;

    private RelativeLayout delete_button;

    private RelativeLayout problem_button;

    private RelativeLayout check_version_button;

    private RelativeLayout share_to_friends;

    private RelativeLayout join_us_button;

    private RelativeLayout about_us_button;

    private Button quit_button;

    private TextView version_text;

    private TextView memory_size;

    private CustomProgressDialog dialog;

    private CheckVersion checkVersion;

    private String versionString;

    private Version version;
    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if(version!=null&&!versionString.equals(version.getVersionId())){
                        new AlertDialog.Builder(getActivity()).setTitle("New Version").setMessage("VersionId:"+version.getVersionId()+"\nVersionDescription:"+version.getVersionDescription()).setPositiveButton("Update now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(MainActivity.downloadService!=null)
                                   MainActivity.downloadService.addDownloadTask(version.getVersionUrl(), 1);
                            }
                        }).setNegativeButton("Remember me later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                    }
                    stopProgressDialog();
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.setting,container,false);

        initView(rootView);

        initData();

        return rootView;
    }
    private void initView(View rootView){
        back_button=(ImageView)rootView.findViewById(R.id.left_return_arrow);

        delete_button=(RelativeLayout)rootView.findViewById(R.id.setting_delete_cash);

        problem_button=(RelativeLayout)rootView.findViewById(R.id.setting_problem_feedback);

        check_version_button=(RelativeLayout)rootView.findViewById(R.id.setting_check_update);

        share_to_friends=(RelativeLayout)rootView.findViewById(R.id.setting_share_with_friends);

        join_us_button=(RelativeLayout)rootView.findViewById(R.id.setting_join_in_us);

        about_us_button=(RelativeLayout)rootView.findViewById(R.id.setting_about_us);

        quit_button=(Button)rootView.findViewById(R.id.setting_quit);

        version_text=(TextView)rootView.findViewById(R.id.setting_version);

        memory_size=(TextView)rootView.findViewById(R.id.memory_size);

        memory_size.setText(String.valueOf(ImageProcess.getFolderSize())+"M");

        //set current version
        PackageManager packageManager=getActivity().getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            versionString=packageInfo.versionName;
            version_text.setText("当前版本：" + versionString);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(!MineFragment.isLogin){
            quit_button.setClickable(false);
            quit_button.setBackgroundColor(Color.parseColor("#bebebe"));
        }
        set=(Set)getActivity();
    }

    private void initData(){
        back_button.setOnClickListener(this);

        delete_button.setOnClickListener(this);

        problem_button.setOnClickListener(this);

        check_version_button.setOnClickListener(this);

        share_to_friends.setOnClickListener(this);

        join_us_button.setOnClickListener(this);

        about_us_button.setOnClickListener(this);

        quit_button.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v) {
        switch (v.getId()){
            case R.id.left_return_arrow:
                getActivity().setResult(1, null);
                getActivity().finish();
                break;
            case R.id.setting_about_us:
                if(set.aboutUs==null)
                    set.aboutUs=new AboutUs();
                FragmentTransaction transaction=set.manager.beginTransaction();
                transaction.hide(set.nowFragment);
                transaction.add(R.id.set_container,set.aboutUs);
                set.nowFragment=set.aboutUs;
                transaction.commit();
                set.isTop=false;
                break;
            case R.id.setting_join_in_us:
                if(set.joinUs==null)
                    set.joinUs=new JoinUs();
                transaction=set.manager.beginTransaction();
                transaction.hide(set.nowFragment);
                transaction.add(R.id.set_container, set.joinUs);
                set.nowFragment=set.joinUs;
                transaction.commit();
                set.isTop=false;
                break;
            case R.id.setting_problem_feedback:
                if(set.feedback==null)
                    set.feedback=new Feedback();
                transaction=set.manager.beginTransaction();
                transaction.hide(set.nowFragment);
                transaction.add(R.id.set_container, set.feedback);
                set.nowFragment=set.feedback;
                transaction.commit();
                set.isTop=false;
                break;
            case R.id.setting_delete_cash:
                ImageProcess.DeleteImage();
                Toast.makeText(getActivity(),"Cash has been cleaned",Toast.LENGTH_LONG).show();
                break;
            case R.id.setting_check_update:
                checkVersion=new CheckVersion(getActivity());
                checkVersion.setOnVersionCheckListener(new CheckVersion.OnVersionCheckListener() {
                    @Override
                    public void getVersion(Version version) {
                        Set_Main.this.version=version;
                        handleProgress.sendEmptyMessageDelayed(0,4000);
                    }
                });
                checkVersion.startCheck();
                startProgressDialog();
                break;
            case R.id.setting_quit:
                MineFragment.isLogin=false;
                quit_button.setClickable(false);
                quit_button.setBackgroundColor(Color.parseColor("#bebebe"));
                SaveAndOpenUserInfo saveAndOpenUserInfo=new SaveAndOpenUserInfo();
                saveAndOpenUserInfo.Delete(getActivity());
                Toast.makeText(getActivity(),"Login off successfully",Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting_share_with_friends:
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intent.putExtra(Intent.EXTRA_TEXT, "Free Time First Share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "Send To"));
                break;
        }
    }
    private void startProgressDialog(){
        if (dialog == null){
            dialog= CustomProgressDialog.createDialog(getActivity());
        }
        dialog.show();
    }

    private void stopProgressDialog(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
