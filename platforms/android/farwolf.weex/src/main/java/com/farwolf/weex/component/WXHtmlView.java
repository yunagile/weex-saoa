package com.farwolf.weex.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.farwolf.weex.view.WXHtmlTextView;

import com.farwolf.weex.view.WXHtmlTextView;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.Component;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXDomObject;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.WXSwitchView;

import java.util.HashMap;
import java.util.Map;

@Component(lazyload = false)
/* 设置可以渲染html的textView */
public class WXHtmlView extends WXComponent<WXHtmlTextView>{
    @Deprecated
    public WXHtmlView(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, String instanceId, boolean isLazy) {
        this(instance, dom, parent, isLazy);
    }

    public WXHtmlView(WXSDKInstance instance, WXDomObject dom, WXVContainer parent, boolean isLazy) {
        super(instance, dom, parent, isLazy);
    }

    @Override
    protected WXHtmlTextView initComponentHostView(@NonNull Context context) {
        WXHtmlTextView textView = new WXHtmlTextView(context);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        
        return textView;
    }
    @WXComponentProp(name = "html")
    public void setHtml(String html) {
        getHostView().setText(Html.fromHtml(html.toString()));
        Map<String, Object> params = new HashMap<>();
        int count = getHostView().getLineCount();
        int lineheight = getHostView().getLineHeight();
        int height = count * lineheight;
        params.put("count", count);
        params.put("height",height);
        params.put("lineheight", lineheight);
        fireEvent("pageinit", params);
    }
}
