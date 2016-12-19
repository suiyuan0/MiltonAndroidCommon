package com.example.retrofitdemo;

import com.example.retrofitdemo.model.MeizhiClassify;
import com.example.retrofitdemo.model.PhoneResult;
import com.example.retrofitdemo.model.UpdateResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by milton on 16/11/2.
 */
public interface ApiManager {
    @GET("1.0.6")
    Call<String> login();

    @GET("1.0.6")
    Call<UpdateResult> update();

    @GET("login/")
    Call<LoginResult> getData(@Query("name") String name, @Query("password") String pw);

    @GET("/users/{user}/repos")
    List<LoginResult> listRepos(@Path("user") String user);

    //每个函数都必须带有 HTTP 注解来表明请求方式和请求的URL路径。类库中有5个HTTP注解:  GET ,  POST ,  PUT ,  DELETE , 和  HEAD。
    // 注解中的参数为请求的相对URL路径。

    @GET("/users/list?sort=desc")
    List<LoginResult> getData2();

    // 请求的URL可以根据函数参数动态更新。一个可替换的区块为用 {  和  } 包围的字符串，而函数参数必需用  @Path 注解表明，并且注解的参数为同样的字符串
    @GET("/group/{id}/users")
    //注意 字符串id
    List<LoginResult> groupList(@Path("id") int groupId); //注意 Path注解的参数要和前面的字符串一样 id

    //还支持查询参数
    @GET("/group/{id}/users")
    List<User> groupList(@Path("id") int groupId, @Query("sort") String sort);


    // 请求体（Request Body）
    //    通过   @Body  注解可以声明一个对象作为请求体发送到服务器。
    @POST("/users/new")
    void createUser(@Body User user, Callback<User> cb);

    //函数也可以注解为发送表单数据和multipart 数据
    //使用  @FormUrlEncoded  注解来发送表单数据；使用  @Field 注解和参数来指定每个表单项的Key，value为参数的值。
    @FormUrlEncoded
    @POST("/user/edit")
    User updateUser(@Field("first_name") String first, @Field("last_name") String last);


    //使用  @Multipart  注解来发送multipart数据。使用  @Part  注解定义要发送的每个文件。
//    @Multipart
//    @PUT("/user/photo")
//    User updateUser(@Part("photo") TypedFile photo, @Part("description") TypedString description);


    //每个函数可以定义为异步或者同步。
    //具有返回值的函数为同步执行的。
    @GET("/user/{id}/photo")
    User listUsers(@Path("id") int id);

    // 而异步执行函数没有返回值并且要求函数最后一个参数为Callback对象
    @GET("/user/{id}/photo")
    void listUsers(@Path("id") int id, Callback<User> cb);


    //使用RestAdapter的转换器把HTTP请求结果（默认为JSON）转换为Java对象，Java对象通过函数返回值或者Callback接口定义
    @GET("/users/list")
    List<User> userLists();

    @GET("/users/list")
    void userList2(Callback<List<User>> cb);

    // 如果要直接获取HTTP返回的对象，使用  Response  对象。
    @GET("16994e49e2f6/")
    Response userList();

    @GET("/users/list")
    void userList(Callback<Response> cb);


    @GET(" http://www.tngou.net/tnfs/api/classify")
    Call<MeizhiClassify> getMeizhiClassify();

    @GET(" http://www.tngou.net/tnfs/api/classify")
    Observable<MeizhiClassify> getMeizhiClassify2();

    @GET("/apistore/mobilenumber/mobilenumber")
        //是方法Url
//@Query**(“phone”)来设定body的parameters.
    Call<PhoneResult> getResult(@Header("apikey") String apikey, @Query("phone") String phone);
}

