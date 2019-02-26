package com.farwolf.weex.module;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Base64;

import com.farwolf.photochoose.ChoosePhotoActivity_;
import com.farwolf.util.UILImageLoader;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by zhangjie on 2018/4/11.
 */

public class WXPhotoModule extends WXModule {
    private static final String TAG = "file";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";
    JSCallback  callback;


    /**
     *
     * @param aspX
     * @param aspY
     * @param themeColor
     * @param titleColor
     * @param cancelColor
     * @param callback
     */
    @JSMethod
    public void open(int aspX, int aspY , String themeColor, String titleColor, String cancelColor, JSCallback callback)
    {
         takePhoto(aspX,aspY,themeColor);
         this.callback=callback;
    }

    public void takePhoto(int width,int height,String themeColor)
    {


        Intent in=new Intent(mWXSDKInstance.getContext(), ChoosePhotoActivity_.class);
        in.putExtra("resize", true);
        in.putExtra("width", width);

        in.putExtra("height", height);
        in.putExtra("themeColor",themeColor);
        ((Activity)mWXSDKInstance.getContext()).startActivityForResult(in,1);

    }

    public void onResult(Intent in)
    {
        if(in==null)
            return;
        String path=in.getStringExtra("path");
        HashMap m=new HashMap();
        m.put("path","sdcard:"+path);
        callback.invoke(m);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
         if(requestCode==1)
         {
             if(resultCode==3||resultCode==4)
             {
                 onResult(data);
             }
         }
    }

    /**
     *
     * @param width
     * @param height
     * @param themeColor
     * @param titleColor
     * @param cancelColor
     * @param callback
     */
    @JSMethod
    public void openPhoto(int width,int height,String themeColor,String titleColor, String cancelColor,final JSCallback callback )
    {
        File editdir=new File(mWXSDKInstance.getContext().getCacheDir()+"gallery/edit");
        File takephoto=new File(mWXSDKInstance.getContext().getCacheDir()+"gallery/photo");
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor(themeColor))
                .setFabNornalColor(Color.parseColor(themeColor))
                .setFabPressedColor(Color.parseColor(themeColor))
                .setCropControlColor(Color.parseColor(themeColor))
                .build();
//        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropWidth(width)
                .setCropHeight(height)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(mWXSDKInstance.getContext(), new UILImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(editdir)
                .setTakePhotoFolder(takephoto)

                .build();
        GalleryFinal.init(coreConfig);

        GalleryFinal.openGallerySingle(1012,functionConfig, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                String url= resultList.get(0).getPhotoPath();
                //Bitmap   bm = BitmapFactory.decodeFile(url);
                HashMap m=new HashMap();
                m.put("path",url);
                callback.invoke(m);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }


    /**
     *
     * @param width
     * @param height
     * @param themeColor
     * @param callback
     */
    @JSMethod
    public void openCamera(int width,int height,String themeColor,final JSCallback callback ){
        File editdir=new File(mWXSDKInstance.getContext().getCacheDir()+"gallery/edit");
        File takephoto=new File(mWXSDKInstance.getContext().getCacheDir()+"gallery/photo");
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor(themeColor))
                .setFabNornalColor(Color.parseColor(themeColor))
                .setFabPressedColor(Color.parseColor(themeColor))
                .setCropControlColor(Color.parseColor(themeColor))
                .build();
//        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()


                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)

                .setCropWidth(width)
                .setCropHeight(height)
                .setForceCrop(true)
                .setForceCropEdit(true)
                .setEnablePreview(true)

                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(mWXSDKInstance.getContext(), new UILImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setEditPhotoCacheFolder(editdir)
                .setTakePhotoFolder(takephoto)

                .build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openCamera(1011, functionConfig,new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                String url= resultList.get(0).getPhotoPath();
                //Bitmap  bm = BitmapFactory.decodeFile(url);
                HashMap m=new HashMap();
                m.put("path",url);
                callback.invoke(m);
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {

            }
        });
    }
    /**
     * @param uploadurl 上传地址
     * @param imgurl 图片路径
     */
    @JSMethod(uiThread = false)
    public void uploadByWeb(String uploadurl,String imgurl,final JSCallback callback){
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String RequestURL = uploadurl;
        HashMap result = new HashMap();
        File file = new File(imgurl);
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                OutputStream outputSteam = conn.getOutputStream();

                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                        + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                    if (res == 200) {
                    result.put("flag","SUCCESS");
                    callback.invoke(result);
                    return;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.put("flag","FAILURE");
        callback.invoke(result);
    }
    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(bm==null)
            return null;
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String imageToBase64(String path){
        if(TextUtils.isEmpty(path)){
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try{
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data,Base64.DEFAULT);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(null !=is){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
