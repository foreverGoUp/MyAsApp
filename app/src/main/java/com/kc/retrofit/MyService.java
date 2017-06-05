package com.kc.retrofit;

import com.kc.bean.RegisterPost;
import com.kc.bean.UserPost;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/6/4.
 */
public interface MyService {
    @Streaming
    @Multipart
    @FormUrlEncoded
    @Headers({//使用@Headers添加多个请求头
            "User-Agent:android",
            "Cache-Control:public,max-age=120"
    })
    @GET("abc/dff")
    Call<ResponseBody> getStudent(@Header("token") String token, @Query("id") int activeId);//@Header是以方法参数形势传入的


    //@Body根据转换方式将实例对象转换为相应的字符串作为请求参数传递.
    @POST("abc/dfd")
    Call<ResponseBody> getStudent(@Body UserPost userPost);

    /**
     * 上传json数据格式的方法
     */
    @POST("fdf/sds")
    Call<ResponseBody> postRegisterInfo(@Body RegisterPost registerPost);

    /**
     * 定义上传单个文件(图片)接口
     */
    @Multipart
    @POST("dsa/sdd")
    Call<ResponseBody> postFiles(@Part MultipartBody.Part file);

    /**
     * 定义上传多个文件(图片)接口
     */
    @Multipart
    @POST("dsa/sdd")
    Call<ResponseBody> postFiles(@PartMap Map<String, MultipartBody.Part> map);

    /**
     * 定义上传图文混传接口
     */
    @Multipart
    @POST("dsa/sdd")
    Call<ResponseBody> postFiles(@Body RegisterPost registerPost, @Part MultipartBody.Part headImage);

    /**
     * 下载文件（图片）接口
     * <p/>
     * 这里需要注意的是如果下载的文件较大，比如在10m以上，那么强烈建议你使用@Streaming进行注解，否则将会出现IO异常.
     */
//    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    //RxJava形式
    @GET
    Observable<ResponseBody> downloadFile2(@Url String url);
}
