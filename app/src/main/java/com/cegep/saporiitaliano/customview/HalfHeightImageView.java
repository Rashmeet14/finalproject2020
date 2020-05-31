package com.cegep.saporiitaliano.customview;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;

public class HalfHeightImageView extends AppCompatImageView {

    public HalfHeightImageView(Context context) {
        super(context);
    }

    public HalfHeightImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HalfHeightImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        setMeasuredDimension(measuredWidth, measuredWidth / 2);
    }
}
