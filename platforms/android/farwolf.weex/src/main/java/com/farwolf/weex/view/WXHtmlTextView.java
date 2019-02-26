package com.farwolf.weex.view;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.support.v7.widget.AppCompatTextView;

import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

public class WXHtmlTextView extends AppCompatTextView implements WXGestureObservable {
    private WXGesture wxGesture;
    public WXHtmlTextView(Context context) {
        super(context);
        //setGravity(Gravity.CENTER_VERTICAL);
        FrameLayout.LayoutParams textLayoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(textLayoutParams);
    }
    @Override
    public void registerGestureListener(WXGesture wxGesture) {
        this.wxGesture = wxGesture;
    }
}
