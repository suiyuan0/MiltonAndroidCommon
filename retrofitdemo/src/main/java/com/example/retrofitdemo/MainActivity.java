package com.example.retrofitdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.retrofitdemo.model.MeizhiClassify;
import com.example.retrofitdemo.model.PhoneResult;
import com.example.retrofitdemo.model.UpdateResult;
import com.example.retrofitdemo.socketfactory.SSLSocketFactoryCompat;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static OkHttpClient okHttpClient;
    Button mLogin;
    Button mLogin2;
    Button mLogin3;
    Button mLogin4;
    Button mLogin5;
    Button mLogin6;
    Button mLogin7;
    Button mLogin8;

    public synchronized static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            try {
                // 自定义一个信任所有证书的TrustManager，添加SSLSocketFactory的时候要用到
                final X509TrustManager trustAllCert =
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        };
                final SSLSocketFactory sslSocketFactory = new SSLSocketFactoryCompat(trustAllCert);
                builder.sslSocketFactory(sslSocketFactory, trustAllCert);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogin = (Button) findViewById(R.id.login);
        mLogin2 = (Button) findViewById(R.id.login2);
        mLogin3 = (Button) findViewById(R.id.login3);
        mLogin4 = (Button) findViewById(R.id.login4);
        mLogin5 = (Button) findViewById(R.id.login5);
        mLogin6 = (Button) findViewById(R.id.login6);
        mLogin7 = (Button) findViewById(R.id.login7);
        mLogin8 = (Button) findViewById(R.id.login8);
        mLogin.setOnClickListener(this);
        mLogin2.setOnClickListener(this);
        mLogin3.setOnClickListener(this);
        mLogin4.setOnClickListener(this);
        mLogin5.setOnClickListener(this);
        mLogin6.setOnClickListener(this);
        mLogin7.setOnClickListener(this);
        mLogin8.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qa.acscloud.honeywell.com.cn/v1/00100001/upgrade/00100001/android_app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiManager apiService = retrofit.create(ApiManager.class);
        Call<MeizhiClassify> call0 = apiService.getMeizhiClassify();
        switch (v.getId()) {
            case R.id.login:
                new UserLoginTask("1", "").execute();
                break;
            case R.id.login2:


                final Call<UpdateResult> call = apiService.update();
                call.enqueue(new Callback<UpdateResult>() {
                    @Override
                    public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                        showLog0(call, response);
                        UpdateResult res = response.body();

                    }

                    @Override
                    public void onFailure(Call<UpdateResult> call, Throwable t) {
                        Log.e("alinmi222", "onFailure call.toString() = " + call.toString() + " Throwable = " + t.getMessage());
                    }
                });

//                Call<String> call = apiService.login();
//                call.enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.isSuccess()) {
//                            Log.e("alinmi222", response.body().toString());
//                        } else {
//                            Log.e("alinmi222", "response not success");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Log.e("alinmi222", "onFailure call.toString() = " + call.toString() + " Throwable = " + t.getMessage());
//                    }
//                });


//                Response response = apiService.userList().ex
//                if (response.isSuccess()) {
//                    Log.e("alinmi222", "response.body() = " + response.body().toString());
//                }
                break;
            case R.id.login3:
                final Call<UpdateResult> call2 = apiService.update();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response<UpdateResult> response = call2.execute();
                            showLog0(null, response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                call2.cancel();
                break;
            case R.id.login4:
                call0.enqueue(new Callback<MeizhiClassify>() {
                    @Override
                    public void onResponse(Call<MeizhiClassify> call, Response<MeizhiClassify> response) {
                        showLog(call, response);
                        MeizhiClassify meizhiClassify = response.body();
                        Log.e("alinmi222", "meizhiClassify.status =  " + meizhiClassify.status);
                        List<MeizhiClassify.TngouEntity> list = meizhiClassify.getTngou();
                        if (list != null && list.size() > 0) {
                            for (int i = 0; i < list.size(); i++) {
                                MeizhiClassify.TngouEntity entity = list.get(i);
                                Log.e("alinmi222", "title = " + entity.title + " ,name = " + entity.name + " , description = " + entity.description + " , keywords = " + entity.keywords + " , id = " + entity.id + " , seq =" + entity.seq);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MeizhiClassify> call, Throwable t) {
                        Log.e("alinmi222", "onFailure call.toString() = " + call.toString() + " Throwable = " + t.getMessage());
                    }
                });
                break;
            case R.id.login5:
                call0.cancel();
                break;
            case R.id.login6:
                Retrofit retrofit2 = new Retrofit.Builder()
                        .baseUrl("http://api.nuuneoi.com/base/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();
                ApiManager apiService2 = retrofit2.create(ApiManager.class);
                Observable<MeizhiClassify> observable = apiService2.getMeizhiClassify2();
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<MeizhiClassify>() {
                            @Override
                            public void onCompleted() {
                                Log.e("alinmi222", "Completed");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("alinmi222", "onError Throwable = " + e.getMessage());
                            }

                            @Override
                            public void onNext(MeizhiClassify meizhiClassify) {
                                Log.e("alinmi", "meizhiClassify.status =  " + meizhiClassify.status);
                                List<MeizhiClassify.TngouEntity> list = meizhiClassify.getTngou();
                                if (list != null && list.size() > 0) {
                                    MeizhiClassify.TngouEntity entity = list.get(0);
                                    Log.e("alinmi222", "title = " + entity.title + " ,name = " + entity.name + " , description = " + entity.description + " , keywords = " + entity.keywords + " , id = " + entity.id + " , seq =" + entity.seq);
                                }
                            }
                        });
                break;
            case R.id.login7:

                Retrofit retrofit3 = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
                        .baseUrl("http://apis.baidu.com")//主机地址
                        .build();
                //这里采用的是Java的动态代理模式
                ApiManager service = retrofit3.create(ApiManager.class);
                //传入我们请求的键值对的值，mPhoneEt是我们输入的手机号
                Call<PhoneResult> call7 = service.getResult("b90f262a4f321bb6755157bf1915b4f8", "18625292922");
                call7.enqueue(new Callback<PhoneResult>() {
                    @Override
                    public void onResponse(Call<PhoneResult> call, Response<PhoneResult> response) {
                        //处理结果
                        if (response.isSuccessful()) {
                            PhoneResult result = response.body();
                            if (result != null) {
                                if (result.getRetMsg() != null) {
                                    Log.e("alinmi222", "result.getRetMsg() = " + result.getRetMsg());
                                }
                                PhoneResult.RetDataEntity entity = result.getRetData();
                                if (entity == null) {
                                    Log.e("alinmi222", "entity = null ");
                                } else {
                                    Log.e("alinmi222", entity.getPhone() + " is " + entity.getProvince() + " " + entity.getCity() + " " + entity.getSupplier() + " " + entity.getSuit() + " prefix :" + entity.getPrefix());
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PhoneResult> call, Throwable t) {
                    }
                });
                break;
            case R.id.login8:
                Retrofit retrofit8 = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())//增加返回值为Gson的支持(以实体类返回)
                        .baseUrl("https://qa.acscloud.honeywell.com.cn/v1/00100001/upgrade/00100001/android_app/")//主机地址
                        .client(getOkHttpClient())       //注意这里要给retrofit 设置okhttpclient
                        .build();

                ApiManager apiService8 = retrofit8.create(ApiManager.class);
                final Call<UpdateResult> call8 = apiService8.update();
                call8.enqueue(new Callback<UpdateResult>() {
                    @Override
                    public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                        showLog0(call, response);
                        UpdateResult res = response.body();

                    }

                    @Override
                    public void onFailure(Call<UpdateResult> call, Throwable t) {
                        Log.e("alinmi222", "onFailure call.toString() = " + call.toString() + " Throwable = " + t.getMessage());
                    }
                });

                break;
            default:
                break;
        }
    }

    private void showLog0(Call<UpdateResult> call, Response<UpdateResult> response) {
        Log.e("alinmi222", "response.toString() = " + response.toString());
        if (response.body() != null) {
            Log.e("alinmi222", "response.body().toString() = " + response.body().toString());
        }
        if (response.errorBody() != null) {
            Log.e("alinmi222", "response.errorBody().toString() = " + response.errorBody().toString());
        }
        if (response.headers() != null) {
            Log.e("alinmi222", "response.headers().toString() = " + response.headers().toString());
        }
        if (response.message() != null) {
            Log.e("alinmi222", "response.message().toString() = " + response.message().toString());
        }
        Log.e("alinmi222", "response.code() = " + response.code());
        if (response.isSuccessful()) {
            Log.e("alinmi222", "response.isSuccess");
        } else {
            Log.e("alinmi222", "response not success");
        }
    }

    private void showLog(Call<MeizhiClassify> call, Response<MeizhiClassify> response) {
        Log.e("alinmi222", "response.toString() = " + response.toString());
        if (response.body() != null) {
            Log.e("alinmi222", "response.body().toString() = " + response.body().toString());
        }
        if (response.errorBody() != null) {
            Log.e("alinmi222", "response.errorBody().toString() = " + response.errorBody().toString());
        }
        if (response.headers() != null) {
            Log.e("alinmi222", "response.headers().toString() = " + response.headers().toString());
        }
        if (response.message() != null) {
            Log.e("alinmi222", "response.message().toString() = " + response.message().toString());
        }
        Log.e("alinmi222", "response.code() = " + response.code());
        if (response.isSuccessful()) {
            Log.e("alinmi222", "response.isSuccess");
        } else {
            Log.e("alinmi222", "response not success");
        }
    }
}
