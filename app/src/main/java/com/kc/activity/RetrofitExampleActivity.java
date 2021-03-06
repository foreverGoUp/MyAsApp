package com.kc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kc.bean.RegisterPost;
import com.kc.myasapp.R;
import com.kc.retrofit.MyService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RetrofitExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_example);


    }

    /**
     * 我们经常使用拦截器实现以下功能：
     * <p/>
     * 设置通用Header
     * 设置通用请求参数
     * 拦截响应
     * 统一输出日志
     * 实现缓存
     */
    private void interceptorsUsageEx() {
        //设置通用Header:为所有的请求都添加相同的多个header

        //统一输出请求日志

        //拦截响应

    }

    public static Interceptor getResponseHeader() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response response = chain.proceed(chain.request());
                String time = response.header("time");//获取到响应头信息
                if (time != null) {

                }
                return response;
            }
        };

        return interceptor;
    }

    /**
     * 日志拦截器
     */
    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    /**
     * 使用addHeader()不会覆盖之前设置的header,若使用header()则会覆盖之前的header
     */
    public static Interceptor getRequestHeader() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request originReq = chain.request();
                Request.Builder builder = originReq.newBuilder();
                builder.addHeader("appid", "1");
                builder.addHeader("timeStamp", "1");
                builder.addHeader("appkey", "1");
                builder.addHeader("signature", "1");

                builder.method(originReq.method(), originReq.body());
                Request request = builder.build();
                return chain.proceed(request);
            }
        };

        return interceptor;
    }

    private void baseUsageExample() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("fdfd/df/fdfd")
                .build();
        MyService api = retrofit.create(MyService.class);
        Call<ResponseBody> call = api.getStudent("dd", 1);
        //同步请求方式
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {

            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //异步请求方式
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //查询请求的状态以及对请求的操作,都可调用call实例的方法。
        call.isCanceled();//请求是否已取消
        call.cancel();//取消请求


        //同一请求发起多次请求
        call.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //上传json数据示例
        Call<ResponseBody> call1 = api.postRegisterInfo(new RegisterPost("kc", 25));
        call1.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        //上传单个文件示例
        //构建requestbody
        File file = new File("dfjk");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        //将resquestbody封装为MultipartBody.Part对象
        MultipartBody.Part part = MultipartBody.Part.createFormData("name", "filename", requestBody);
        Call<ResponseBody> call2 = api.postFiles(part);

        //下载文件示例
        Call<ResponseBody> call3 = api.downloadFile("df/dfd/adf");
        call3.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                InputStream is = responseBody.byteStream();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        //下载文件RxJava示例,其他请求也可写成RxJava形式。
        Observable<ResponseBody> observable = api.downloadFile2("url");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {

                    }
                });
    }


}
