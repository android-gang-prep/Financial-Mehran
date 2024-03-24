package com.example.mehranm4;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.example.mehranm4.database.entity.CategoryEntity;
import com.example.mehranm4.databinding.CategoryCirBinding;
import com.example.mehranm4.models.CategoryModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CircleChatView extends View {
    public CircleChatView(Context context) {
        super(context);
    }

    public CircleChatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleChatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private List<CategoryModel> categories;
    private List<Paint> categoryPaint;

    private void init() {
        categories = new ArrayList<>();
        categoryPaint = new ArrayList<>();
    }

    ValueAnimator valueAnimator = null;
    float visible=0f;

    private void startAnimation() {
        if (valueAnimator!=null)
            valueAnimator.cancel();

        visible=0f;
        valueAnimator= ObjectAnimator.ofFloat(0f,1f);
        valueAnimator.setDuration(1000);
        valueAnimator.setStartDelay(200);
        valueAnimator.addUpdateListener(animation -> {
            visible= (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.start();
    }

    public void addData(List<CategoryModel> categories) {
        categoryPaint.clear();
        this.categories = new ArrayList<>(categories);


        for (int i = 0; i < categories.size(); i++) {
            Paint paint = new Paint();
            paint.setStrokeWidth(dpToPx(50));
            paint.setColor(Color.parseColor(categories.get(i).category.getColor()));
            paint.setStyle(Paint.Style.STROKE);
            categoryPaint.add(paint);
        }

        startAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (categories.size() == 0)
            return;

        long total = 0;

        for (int i = 0; i < categories.size(); i++) {
            total += categories.get(i).getTotal();
        }

        double div = 360 / ((double) total);

        float startAngle = 0;
        int x = getWidth() / 2;
        int y = getHeight() / 2;
        int radius = (int) (Math.min(getWidth(), getHeight()) / 2 - dpToPx(30));
        for (int i = 0; i < categories.size(); i++) {

            canvas.drawArc(x - radius, y - radius, x + radius, y + radius, startAngle, (float) (categories.get(i).getTotal() * div)*visible, false, categoryPaint.get(i));

            startAngle += (float) (categories.get(i).getTotal() * div)*visible;
        }

    }

    private float dpToPx(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
}
