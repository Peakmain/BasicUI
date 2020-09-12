package com.peakmain.ui.utils.network;

import android.content.Context;
import android.util.Log;

import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.OkHttpManager;
import com.peakmain.ui.utils.network.body.ExMultipartBody;
import com.peakmain.ui.utils.network.callback.DownloadCallback;
import com.peakmain.ui.utils.network.callback.EngineCallBack;
import com.peakmain.ui.utils.network.callback.ProgressEngineCallBack;
import com.peakmain.ui.utils.network.callback.UploadProgressListener;
import com.peakmain.ui.utils.network.download.DownloadDispatcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author ：Peakmain
 * createTime：2020/9/12
 * mail:2726449200@qq.com
 * describe：okhttp的网络引擎
 */
public class OkHttpEngine implements IHttpEngine {
    private OkHttpClient mOkHttpClient;

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }
    public OkHttpEngine(){
        if(mOkHttpClient==null){
            mOkHttpClient= OkHttpManager.getOkHttpClient();
        }
    }
    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        url = HttpUtils.jointParams(url, params);
        Log.e("Get请求路径：", url);
        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);
        //可以省略，默认是GET请求
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resultJson = response.body().string();
                callBack.onSuccess(resultJson);
                Log.e("Get返回结果：", resultJson);
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);  //打印
        Log.e("Post请求路径：", jointUrl);
        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(requestBody)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(okhttp3.Call call, IOException e) {
                        callBack.onError(e);
                    }

                    @Override
                    public void onResponse(okhttp3.Call call, Response response) throws IOException {
                        // 这个 两个回掉方法都不是在主线程中
                        String result = response.body().string();
                        Log.e("Post返回结果：", result);
                        callBack.onSuccess(result);
                    }
                }
        );
    }

    /**
     * 上传文件
     * @param context  上下文
     * @param url 方法的url
     * @param file 上传的文件
     * @param callBack 回掉
     */
    @Override
    public void uploadFile(Context context, String url, File file, final ProgressEngineCallBack callBack) {
       LogUtils.d("Upload请求的路径:"+url);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("platform", "android");
        builder.addFormDataPart("file", file.getName(), RequestBody
                .create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
        ExMultipartBody body = new ExMultipartBody(builder.build(), new UploadProgressListener() {
            @Override
            public void onProgress(long total, long current) {
                callBack.onProgress(total, current);
            }
        });
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callBack.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e("Upload返回结果：", result);
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * 单线程下载
     * @param context  上下文
     * @param url 下载的URL
     * @param outFile 保存文件的目录
     * @param callback 下载的回掉
     */
    @Override
    public void downloadSingleManager(Context context, String url, final File outFile, final DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (outFile == null) {
                    throw new NullPointerException("File cannot be empty");
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(outFile);
                int len = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
                callback.onSucceed(outFile);
            }
        });
    }

    @Override
    public void downloadMultiManager(Context context, String url, File outFile, DownloadCallback callback) {
        DownloadDispatcher.getInstance().startDownload(url,outFile, callback);
    }
    /**
     * 组装post请求参数body
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    // 添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // 处理文件 --> Object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMimeType(file
                                    .getAbsolutePath())), file));
                } else if (value instanceof List) {
                    // 代表提交的是 List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            // 获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMimeType(file
                                            .getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
