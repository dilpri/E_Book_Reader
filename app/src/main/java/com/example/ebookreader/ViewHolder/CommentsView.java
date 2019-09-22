/*
 * Copyright (c) 2019.
 * Gigara @ G Soft Solutions
 */

package com.example.ebookreader.ViewHolder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CommentsView extends ListView {

    public CommentsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentsView(Context context) {
        super(context);
    }

    public CommentsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
