package yjjc.cl.com.androidstudy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/3/24.
 */
public class MyTextView extends TextView {
    private Paint mPaint1;
    private Paint mPaint2;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressWarnings("NewApi")
    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint1 = new Paint();
//        final int version = Build.VERSION.SDK_INT;
//        if (version < 23)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            mPaint1.setColor(getResources().getColor(android.R.color.holo_blue_light));
        }else
        {
            mPaint1.setColor(getResources().getColor(android.R.color.holo_blue_light, null));
        }
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint2 = new Paint();
        mPaint2.setColor(Color.YELLOW);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getMeasuredWidth()/2, getMeasuredHeight(), mPaint1);
        canvas.drawRect(getMeasuredWidth() / 2, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint2);
        canvas.save();
//        canvas.translate(10, 0);
        super.onDraw(canvas);
//        canvas.restore();
    }
}
