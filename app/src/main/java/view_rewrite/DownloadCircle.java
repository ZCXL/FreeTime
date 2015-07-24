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
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuchao.freetime.R;

import utils.Utils;

/**
 * Created by zhuchao on 7/23/15.
 */
public class DownloadCircle extends CustomerView {
    private int circleColor_inside;

    private int circleColor_outside;

    private int textColor;

    private int textSize;

    private int start_button_source;

    private ImageView start_button;

    private TextView notification;

    private TextView download_speed;

    private float percent=0.0f;

    private boolean download_success=false;

    private boolean end_animation=false;

    private int radius_first=0;

    public void setPercent(float percent){
        this.percent=percent;
        notification.setText(String.format("%.1f",percent*100)+"%");
    }
    public void setDownload_speed(String speed){
        this.download_speed.setText(speed+"kb/s");
    }
    public void setWait(){
        this.download_speed.setText("");
        this.notification.setText("Waiting");
    }
    public void setStart(){
        download_success=false;
        if(download_speed.getVisibility()==GONE){
            notification.setVisibility(VISIBLE);
            download_speed.setVisibility(VISIBLE);
        }
        if(start_button.getVisibility()==VISIBLE)
            start_button.setVisibility(GONE);
    }
    public void endDownload(){
        if(download_speed.getVisibility()==VISIBLE){
            notification.setVisibility(GONE);
            download_speed.setVisibility(GONE);
        }
        if(start_button.getVisibility()==GONE)
            start_button.setVisibility(VISIBLE);
        download_success=true;
    }
    public DownloadCircle(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        setAttributes(attributeSet);
    }
    @Override
    void setAttributes(AttributeSet attributes) {
        setBackgroundResource(android.R.color.transparent);

        TypedArray array=getContext().obtainStyledAttributes(attributes,R.styleable.DownloadCircle);

        circleColor_inside=array.getColor(R.styleable.DownloadCircle_circle_color_inside, 0);

        circleColor_outside = array.getColor(R.styleable.DownloadCircle_circle_color_outside, 0);

        textSize = array.getDimensionPixelSize(R.styleable.DownloadCircle_text_size, 0);

        textColor = array.getColor(R.styleable.DownloadCircle_text_color, 0);

        start_button_source=attributes.getAttributeResourceValue(CUSTOMREXML,"image_button",-1);

        notification=new TextView(getContext());

        LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        notification.setTextSize(textSize);
        notification.setTextColor(textColor);
        notification.setId(R.id.main_zero_time_movie_description);
        notification.setLayoutParams(params);
        notification.setText("0%");
        addView(notification);

        download_speed=new TextView(getContext());
        LayoutParams speed_params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        speed_params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        speed_params.addRule(RelativeLayout.BELOW,R.id.main_zero_time_movie_description);
        speed_params.topMargin=10;
        download_speed.setLayoutParams(speed_params);
        download_speed.setTextSize(textSize - 10);
        download_speed.setTextColor(textColor);
        download_speed.setText("0k/s");
        addView(download_speed);

        start_button=new ImageView(getContext());
        LayoutParams start_params=new LayoutParams(Utils.dpToPx(61.5f,getResources()),Utils.dpToPx(61.5f,getResources()));
        start_params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        start_button.setImageResource(start_button_source);
        start_button.setVisibility(GONE);

        addView(start_button);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(!download_success) {
            int arcO = -90;
            int arcD = (int) (percent * 360);
            Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas temp = new Canvas(bitmap);

            Paint paint = new Paint();
            paint.setColor(circleColor_inside);
            paint.setStrokeWidth(Utils.dpToPx(2, getResources()));
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);

            Paint percentPaint = new Paint();
            percentPaint.setColor(circleColor_outside);
            percentPaint.setStrokeWidth(Utils.dpToPx(2, getResources()));
            temp.drawArc(new RectF(0, 0, getWidth(), getHeight()), arcO, arcD, true, percentPaint);

            Paint paint1 = new Paint();
            paint1.setColor(getResources().getColor(android.R.color.transparent));
            paint1.setAntiAlias(true);
            paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            temp.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - Utils.dpToPx(4, getResources()), paint1);

            canvas.drawBitmap(bitmap, 0, 0, new Paint());
        }
        invalidate();
    }
    private void drawEndAnimation(Canvas canvas){
        if(radius_first<getHeight()/2){
            //draw a changing circle
            Paint paint=new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            radius_first+=1;
            canvas.drawCircle(getHeight()/2,getWidth()/2,radius_first,paint);
        }
    }

    //make circular color
    protected int makePressColor(){
        int r = (this.circleColor_outside >> 16) & 0xFF;
        int g = (this.circleColor_outside >> 8) & 0xFF;
        int b = (this.circleColor_outside>> 0) & 0xFF;
        return Color.argb(128, r, g, b);
    }
}
