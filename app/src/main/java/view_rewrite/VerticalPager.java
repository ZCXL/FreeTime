package view_rewrite;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class VerticalPager extends LinearLayout{

    private Scroller mScroller;
    private Context mContext;
    public VerticalPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        mScroller=new Scroller(context,new LinearInterpolator());
//		mScroller=new Scroller(mContext, new Interpolator() {
//
//			@Override
//			public float getInterpolation(float input) {
//				return 300;
//			}
//		});

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalHeight=0;
        int count=getChildCount();

        for(int i=0;i<count;i++){
            View childView=getChildAt(i);

            childView.layout(l, totalHeight, r, totalHeight+b);

            totalHeight+=b;
        }
    }

    private VelocityTracker mVelocityTracker;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }


    private int mLastMotionY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mVelocityTracker==null){
            mVelocityTracker=VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        int action=event.getAction();

        float y=event.getY();

        switch(action){
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                mLastMotionY=(int) y;
                Log.d("montion", ""+getScrollY());
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY=(int) (mLastMotionY-y);
                if(getScrollY()>0&&getScrollY()<getMeasuredHeight());
                   scrollBy(0,deltaY);
                //mScroller.startScroll(0, getScrollY(), 0, deltaY);
                invalidate();

                mLastMotionY=(int) y;
                break;
            case MotionEvent.ACTION_UP:
                if(mVelocityTracker!=null){
                    mVelocityTracker.recycle();
                    mVelocityTracker=null;
                }

                if(getScrollY()<0){
                    mScroller.startScroll(0, -400, 0, 400);
                }else if(getScrollY()>(getHeight()*(getChildCount()-1))){
                    View lastView=getChildAt(getChildCount()-1);

                    mScroller.startScroll(0,lastView.getTop()+300, 0, -300);
                }else{
                    int position=getScrollY()/getHeight();
                    int mod=getScrollY()%getHeight();


                    if(mod>getHeight()/3){
                        View positionView=getChildAt(position+1);
                        mScroller.startScroll(0, positionView.getTop()-300, 0, +300);
                    }else{
                        View positionView=getChildAt(position);
                        mScroller.startScroll(0, positionView.getTop()+300, 0, -300);
                    }


                }
                invalidate();
                break;
        }

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if(mScroller.computeScrollOffset()){
            scrollTo(0, mScroller.getCurrY());
        }else{

        }
    }
    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);// 得到模式
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);// 得到尺寸

        switch (widthMode) {
            /**
             * mode共有三种情况，取值分别为MeasureSpec.UNSPECIFIED, MeasureSpec.EXACTLY,
             * MeasureSpec.AT_MOST。
             *
             *
             * MeasureSpec.EXACTLY是精确尺寸，
             * 当我们将控件的layout_width或layout_height指定为具体数值时如andorid
             * :layout_width="50dip"，或者为FILL_PARENT是，都是控件大小已经确定的情况，都是精确尺寸。
             *
             *
             * MeasureSpec.AT_MOST是最大尺寸，
             * 当控件的layout_width或layout_height指定为WRAP_CONTENT时
             * ，控件大小一般随着控件的子空间或内容进行变化，此时控件尺寸只要不超过父控件允许的最大尺寸即可
             * 。因此，此时的mode是AT_MOST，size给出了父控件允许的最大尺寸。
             *
             *
             * MeasureSpec.UNSPECIFIED是未指定尺寸，这种情况不多，一般都是父控件是AdapterView，
             * 通过measure方法传入的模式。
             */
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

}
