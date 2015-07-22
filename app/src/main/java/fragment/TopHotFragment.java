package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhuchao.freetime.R;
import com.zhuchao.freetime.TopHot_Detail;

import java.util.ArrayList;

import adapter.TopHotItemAdapter;
import bean.BaseObject;
import bean.Movie;
import bean.Movies;
import function.Network;
import function.NetworkFunction;
import view_rewrite.PullToRefreshView;

/**
 * Created by zhuchao on 7/13/15.
 */
public class TopHotFragment extends Fragment implements Runnable{
    //pull to refresh view
    private PullToRefreshView pullToRefresh;
    //listView
    private ListView listView;
    //array list movie
    private ArrayList<Movie>movieArrayList;
    //adapter of item
    private TopHotItemAdapter adapter;
    //url
    private String url="http://123.56.85.58/FreeTime/code/get_top_hot.php";
    //key
    private String keys[]=new String[]{"page"};
    //Movies
    private Movies movies;
    //Page
    private int page=1;
    //header update
    private boolean headerUpdate=false;
    //footer update
    private boolean footerUpdate=false;


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    adapter.notifyDataSetChanged();
                    if(headerUpdate){
                        pullToRefresh.onHeaderRefreshComplete();
                        headerUpdate=false;
                    }
                    if(footerUpdate){
                        pullToRefresh.onFooterRefreshComplete();
                        footerUpdate=false;
                    }
                    break;
                case 1:
                    Toast.makeText(getActivity(),"Get data failed",Toast.LENGTH_SHORT).show();
                    if(headerUpdate){
                        pullToRefresh.onHeaderRefreshComplete();
                        headerUpdate=false;
                    }
                    if(footerUpdate){
                        pullToRefresh.onFooterRefreshComplete();
                        footerUpdate=false;
                    }
                    break;
                case 2:
                    page=1;
                    movieArrayList.clear();
                    new Thread(TopHotFragment.this).start();
                    break;
                case 3:
                    new Thread(TopHotFragment.this).start();
                    break;
                case 4:
                    Toast.makeText(getActivity(),"Please check network",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(1);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.main_framelayout_top_hot,container,false);

        initView(rootView);

        initData();

        //init data
        new Thread(this).start();

        return rootView;
    }

    private void initView(View rootView){
        pullToRefresh=(PullToRefreshView)rootView.findViewById(R.id.pull_to_refresh);
        listView=(ListView)rootView.findViewById(R.id.listView);

        movies=new Movies();

        movieArrayList=new ArrayList<Movie>();
        adapter=new TopHotItemAdapter(movieArrayList,getActivity());

        listView.setAdapter(adapter);

    }
    private void initData(){
        pullToRefresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                mHandler.sendEmptyMessage(3);
                footerUpdate=true;
            }
        });
        pullToRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                mHandler.sendEmptyMessage(2);
                headerUpdate=true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie=movieArrayList.get(position);
                Intent intent=new Intent(getActivity(),TopHot_Detail.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("movie",movie);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void run() {
        if(Network.checkNetWorkState(getActivity())) {
            String parameters[] = new String[]{String.valueOf(page)};
            String result = NetworkFunction.ConnectServer("http://123.56.85.58/FreeTime/code/get_top_hot.php", keys, parameters);
            Log.d("fafafafafaf",result);
            if (result != null&&!result.contains("error")) {
                ArrayList<BaseObject> temps = movies.getObjects(result);
                if (temps != null) {
                    for (int i = 0; i < temps.size(); i++) {
                        movieArrayList.add((Movie) temps.get(i));
                    }
                    mHandler.sendEmptyMessage(0);
                    page++;
                }
            } else {
                mHandler.sendEmptyMessage(1);
            }
        }else{
            mHandler.sendEmptyMessage(4);
        }
    }
}
