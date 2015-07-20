package view_rewrite;

/**
 * ����ʵ���������ص�������ؼ�,
 * ScrollOverListViewֻ���ṩ�������¼���,
 */
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhuchao.freetime.R;

@SuppressLint("SimpleDateFormat")
public class UploadView extends LinearLayout implements ScrollOverListView.OnScrollOverListener {
	private static final int WHAT_DID_LOAD_DATA = 1; // Handler what ���ݼ������
	private static final int WHAT_DID_MORE = 5; // Handler what �Ѿ���ȡ�����

	@SuppressWarnings("unused")
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM-dd HH:mm");

	private View mFooterView;
	private TextView mFooterTextView;
	private View mFooterLoadingView;
	private ScrollOverListView mListView;
	private OnUploadListener mOnPullDownListener;
	private boolean mIsFetchMoreing;
	private boolean mEnableAutoFetchMore;

	public UploadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFooterViewAndListView(context);
	}
	public UploadView(Context context) {
		super(context);
		initFooterViewAndListView(context);
	}

	/**
	 * �¼��ӿ�
	 */
	public interface OnUploadListener {
		void onMore();
	}
	public void notifyDidLoad() {
		mUIHandler.sendEmptyMessage(WHAT_DID_LOAD_DATA);//���ݼ������
	}

	public void notifyDidMore() {
		mUIHandler.sendEmptyMessage(WHAT_DID_MORE);//�Ѿ���ȡ�����
	}

	public void setOnPullDownListener(OnUploadListener listener) {
		mOnPullDownListener = listener;
	}

	public ListView getListView() {
		return mListView;
	}

	public void enableAutoFetchMore(boolean enable, int index) {
		if (enable) {
			mListView.setBottomPosition(index);
			mFooterLoadingView.setVisibility(View.VISIBLE);
		} else {
			mFooterTextView.setText("����");
			mFooterLoadingView.setVisibility(View.GONE);
		}
		mEnableAutoFetchMore = enable;
	}

	private void initFooterViewAndListView(Context context) {
		setOrientation(LinearLayout.VERTICAL);

		mFooterView = LayoutInflater.from(context).inflate(
				R.layout.pulldown_footer, null);
		mFooterTextView = (TextView) mFooterView
				.findViewById(R.id.pull_down_footer_text);
		mFooterLoadingView = mFooterView
				.findViewById(R.id.pull_down_footer_loading);
		mFooterView.setOnClickListener(new OnClickListener() {//����¼� 
			@Override
			public void onClick(View v) {
//				if (!mIsFetchMoreing) {
//					mIsFetchMoreing = true;
					mFooterLoadingView.setVisibility(View.VISIBLE);
					mOnPullDownListener.onMore();
				//}
			}
		});

		/*
		 * ScrollOverListView ͬ���ǿ��ǵ�����ʹ�ã����Է������ͬʱ��Ϊ����Ҫ���ļ����¼�
		 */
		mListView = new ScrollOverListView(context);
		mListView.setOnScrollOverListener(this);
		mListView.setCacheColorHint(0);
		addView(mListView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		
//�յ�listener
		mOnPullDownListener = new OnUploadListener(){
			@Override
			public void onMore() {	
			}
		};
	}

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {//���ݼ������
				showFooterView();
				mFooterLoadingView.setVisibility(View.GONE);
				return;
			}

			case WHAT_DID_MORE: {//�Ѿ���ȡ�����
				mIsFetchMoreing = false; 
				mFooterTextView.setText("����"); 
				mFooterLoadingView.setVisibility(View.GONE); 
			}
			}
		}
	};

	/**
	 * ��ʾ�Ų��Ų��ļ�
	 */
	private void showFooterView() {
		if (mListView.getFooterViewsCount() == 0 && isFillScreenItem()) {
			mListView.addFooterView(mFooterView);
			mListView.setAdapter(mListView.getAdapter());
		}
	}

	/**
	 * item��Ŀ�Ƿ�����������Ļ
	 */
	private boolean isFillScreenItem() {
		final int firstVisiblePosition = mListView.getFirstVisiblePosition();
		final int lastVisiblePosition = mListView.getLastVisiblePosition()
				- mListView.getFooterViewsCount();
		final int visibleItemCount = lastVisiblePosition - firstVisiblePosition
				+ 1;
		final int totalItemCount = mListView.getCount()
				- mListView.getFooterViewsCount();
		if (visibleItemCount < totalItemCount)
			return true;
		return false;
	}

	//�Ƿ�����
	@Override
	public boolean onListViewBottomAndPullUp(int delta) {
		if (!mEnableAutoFetchMore || mIsFetchMoreing)
			return false;
		// ����������Ļ�Ŵ���
		if (isFillScreenItem()) {
			mIsFetchMoreing = true;
			mFooterTextView.setText("���ظ�����...") ; 
			mFooterLoadingView.setVisibility(View.VISIBLE); 
			mOnPullDownListener.onMore();
			return true;
		}
		return false;
	}

	@Override
	public boolean onMotionDown(MotionEvent ev) {	
		return false;
	}

	@Override
	public boolean onMotionMove(MotionEvent ev, int delta) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMotionUp(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}
}
