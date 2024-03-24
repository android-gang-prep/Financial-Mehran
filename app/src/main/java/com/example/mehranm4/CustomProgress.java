package com.example.mehranm4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomProgress extends View {
    int percent = 0;

    public CustomProgress(Context context) {
        super(context);
        init();

    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    public CustomProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    Paint paint;

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }

    public void setPercent(int percent) {
        this.percent = percent;
        if (this.percent > 100)
            this.percent = 100;
        if (this.percent < 50)
            paint.setColor(getResources().getColor(R.color.green));
        else if (this.percent < 90)
            paint.setColor(getResources().getColor(R.color.orange));
        else
            paint.setColor(getResources().getColor(R.color.red));

        invalidate();
    }

    public void setColor(int color) {
        paint.setColor(color);
        invalidate();

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(dpTpPx(2), dpTpPx(2), (getWidth() - dpTpPx(2)) / 100 * percent, getHeight() - dpTpPx(2), dpTpPx(15), dpTpPx(15), paint);
    }

    private float dpTpPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}
