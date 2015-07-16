package view_rewrite;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.zhuchao.freetime.R;

import utils.Utils;

/**
 * Created by zhuchao on 7/16/15.
 */
public class CircularProgressBar extends CustomerView {
    //default circle color
    private int circleColor=Color.parseColor("#00b7ee");
    //default circle width
    private int width=4;
    //radius 1
    private float radius_first=0f;

    //radius 2
    private float radius_second=0f;
    //symbol of ending first animation
    private boolean isEndFirst;
    private boolean isEndSecond;
    private int count=0;

    //start animation
    private boolean isStart=false;
    public CircularProgressBar(Context context,AttributeSet attributeSet){
        super(context, attributeSet);

        setAttributes(attributeSet);
    }
    @Override
    void setAttributes(AttributeSet attributes) {
        setBackgroundResource(android.R.color.transparent);
        setMinimumHeight(Utils.dpToPx(32, getResources()));
        setMinimumWidth(Utils.dpToPx(32, getResources()));

        TypedArray array=getContext().obtainStyledAttributes(attributes, R.styleable.CircularProgressBar);

        circleColor=array.getColor(R.styleable.CircularProgressBar_circle_color,0);
        width=array.getDimensionPixelSize(R.styleable.CircularProgressBar_width,4);

    }

    public void startAnimation(){
        isStart=true;
        isEndFirst=false;
        isEndSecond=true;
        invalidate();
    }
    public void endAnimation(){
        isStart=false;
        isEndSecond=true;
        isEndFirst=true;
        count=0;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(isStart){
            if(!isEndFirst)
                drawFirstAnimation(canvas);
            else
                drawSecondAnimation(canvas);
            invalidate();
        }
    }

    private void drawFirstAnimation(Canvas canvas){
        if(radius_first<getHeight()/2){
            //draw a changing circle
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius_first+=1;
            canvas.drawCircle(getHeight()/2,getWidth()/2,radius_first,paint);
        }else{
            //draw a big circle using background
            Bitmap bitmap=Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp=new Canvas(bitmap);
            Paint paint=new Paint();
            paint.setColor(makePressColor());
            paint.setAntiAlias(true);
            temp.drawCircle(getWidth()/2,getHeight()/2,getHeight()/2,paint);

            //clear unnecessary color
            Paint transparentPaint=new Paint();
            transparentPaint.setAntiAlias(true);
            transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
            transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            //clear
            if(count >= 50){
                radius_second = (radius_second >= getWidth()/2)? (float)getWidth()/2 : radius_second+1;
            }else{
                radius_second = (radius_second >= getWidth()/2-Utils.dpToPx(width, getResources()))? (float)getWidth()/2-Utils.dpToPx(width, getResources()) : radius_second+1;
            }
            temp.drawCircle(getWidth()/2, getHeight()/2, radius_second, transparentPaint);
            canvas.drawBitmap(bitmap, 0, 0, new Paint());
            if(radius_second >= getWidth()/2-Utils.dpToPx(4, getResources()))
                count++;
            if(radius_second >= getWidth()/2) {
                isEndFirst = true;
                //start second animation;
                isEndSecond=false;
            }
        }
    }
    int arcD = 1;
    int arcO = 0;
    float rotateAngle = 0;
    int limited = 0;
    /**
     * Draw second animation of view
     * @param canvas
     */
    private void drawSecondAnimation(Canvas canvas){
        if(arcO == limited)
            arcD+=6;
        if(arcD >= 290 || arcO > limited){
            arcO+=6;
            arcD-=6;
        }
        if(arcO > limited + 290){
            limited = arcO;
            arcO = limited;
            arcD = 1;
        }
        rotateAngle += 4;
        canvas.rotate(rotateAngle,getWidth()/2, getHeight()/2);

        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(circleColor);
        temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, paint);
        Paint transparentPaint = new Paint();
        transparentPaint.setAntiAlias(true);
        transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
        transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        temp.drawCircle(getWidth()/2, getHeight()/2, (getWidth()/2)-Utils.dpToPx(width, getResources()), transparentPaint);

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }
    //make circular color
    protected int makePressColor(){
        int r = (this.circleColor >> 16) & 0xFF;
        int g = (this.circleColor >> 8) & 0xFF;
        int b = (this.circleColor>> 0) & 0xFF;
        return Color.argb(128,r, g, b);
    }

}
