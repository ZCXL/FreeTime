package fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhuchao.freetime.R;
import com.zhuchao.freetime.TopHot_Detail;

import java.util.ArrayList;

import adapter.TopHotItemAdapter;
import bean.Movie;
import view_rewrite.PullToRefreshView;

/**
 * Created by zhuchao on 7/13/15.
 */
public class TopHotFragment extends Fragment {
    //pull to refresh view
    private PullToRefreshView pullToRefresh;
    //listView
    private ListView listView;
    //adapter of item
    private TopHotItemAdapter adapter;
    //set of movies
    private ArrayList<Movie>movies;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pullToRefresh.onHeaderRefreshComplete();
            //3s后执行代码
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
        return rootView;
    }

    private void initView(View rootView){
        pullToRefresh=(PullToRefreshView)rootView.findViewById(R.id.pull_to_refresh);
        listView=(ListView)rootView.findViewById(R.id.listView);

        movies=new ArrayList<Movie>();
        for(int i=0;i<10;i++){
            Movie movie=new Movie();
            movies.add(movie);
        }
        adapter=new TopHotItemAdapter(movies,getActivity());

        listView.setAdapter(adapter);

    }
    private void initData(){
        pullToRefresh.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                pullToRefresh.onFooterRefreshComplete();
            }
        });
        pullToRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) {
                mHandler.postDelayed(mRunnable, 3000);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().startActivity(new Intent(getActivity(), TopHot_Detail.class));
            }
        });
    }
}
