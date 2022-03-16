package com.android.autofillkeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public final class InputView extends FrameLayout {

    // If true, this InputView will simulate Gboard's InputView behavior, which expands its
    // region to the entire window regardless of its content view's size.
    private static final boolean EXPAND_TO_WINDOW = false;

    private int mRealHeight;

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRealHeight = getMeasuredHeight();
        if (EXPAND_TO_WINDOW && MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            setMeasuredDimension(getMeasuredWidth(), MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    int getTopInsets() {
        return getMeasuredHeight() - mRealHeight;
    }
}