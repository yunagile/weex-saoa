package com.farwolf.weex.module;

import android.content.Intent;
import android.net.Uri;
import android.app.Activity;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/*打开默认浏览器*/
public class WXFWebUtilModule extends WXModule {
    @JSMethod(uiThread = false)
    public void openUrl(String url) {
       Uri uri = Uri.parse(url);
       Intent intent = new Intent(Intent.ACTION_VIEW, uri);
       ((Activity)mWXSDKInstance.getContext()).startActivity(intent);
    }
}
