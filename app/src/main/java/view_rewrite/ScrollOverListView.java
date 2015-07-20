package view_rewrite;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ScrollOverListView extends ListView {
	private int mLastY; 
	private int mBottomPosition; 
//���캯��
	public ScrollOverListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	//���캯��
	public ScrollOverListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	//���캯��
	public ScrollOverListView(Context context) {
		super(context);
		init();
	}

	//��ʼ��
	private void init() {
		mBottomPosition = 0;
	}

//�����¼�
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		final int y = (int) ev.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mLastY = y;
			final boolean isHandled = mOnScrollOverListener.onMotionDown(ev);
			if (isHandled) {
				mLastY = y;
				return isHandled;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			final int childCount = getChildCount();//Returns the number of children in the group.
			if (childCount == 0)
			return super.onTouchEvent(ev);
			final int itemCount = getAdapter().getCount() - mBottomPosition;
			final int deltaY = y - mLastY;

			final int lastBottom = getChildAt(childCount - 1).getBottom();
			final int end = getHeight()-getPaddingBottom();
			final int firstVisiblePosition = getFirstVisiblePosition();
			final boolean isHandleMotionMove = mOnScrollOverListener.onMotionMove(ev, deltaY);
			if (isHandleMotionMove) {
				mLastY = y;
				return true;
			}

			if (firstVisiblePosition + childCount >= itemCount
					&& lastBottom <= end && deltaY < 0) {
				final boolean isHandleOnListViewBottomAndPullDown;
				isHandleOnListViewBottomAndPullDown = mOnScrollOverListener
						.onListViewBottomAndPullUp(deltaY);
				if (isHandleOnListViewBottomAndPullDown) {
					mLastY = y;
					return true;
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			final boolean isHandlerMotionUp = mOnScrollOverListener.onMotionUp(ev);
			if (isHandlerMotionUp) {
				mLastY = y;
				return true;
			}
			break;
		}
		}
		mLastY = y;
		return super.onTouchEvent(ev);
	}
	private OnScrollOverListener mOnScrollOverListener = new OnScrollOverListener() {
		@Override
		public boolean onListViewBottomAndPullUp(int delta) {
			return false;
		}

		@Override
		public boolean onMotionDown(MotionEvent ev) {
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			return false;
		}

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			return false;
		}
	};

	public void setBottomPosition(int index) {
		if (getAdapter() == null)//The adapter currently used to display data in this ListView.
			throw new NullPointerException(
			"You must set adapter before setBottonPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Bottom position must > 0");
		mBottomPosition = index;
	}

	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		mOnScrollOverListener = onScrollOverListener;
	}

	public interface OnScrollOverListener {

		boolean onListViewBottomAndPullUp(int delta);//����

		boolean onMotionDown(MotionEvent ev);
		boolean onMotionMove(MotionEvent ev, int delta);

		boolean onMotionUp(MotionEvent ev);
	}
}
