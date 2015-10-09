package com.example.android.movie;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by vortana_say on 01/09/15.
 * /** An image view which always remains square with respect to its width. */

final class PicassoImageView extends ImageView {
    public PicassoImageView(Context context) {
        super(context);
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        //Log.d(PicassoImageView.class.getSimpleName(), getMeasuredWidth() + "");
        //Log.d(PicassoImageView.class.getSimpleName(), getMeasuredHeight()+ "");
    }
}
