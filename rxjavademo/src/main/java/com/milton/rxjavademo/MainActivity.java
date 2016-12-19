package com.milton.rxjavademo;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String tag = "RxJava";
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;
    ImageView ivShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        ivShow = (ImageView) findViewById(R.id.iv_show);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                Observable observable1 = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onStart();
                        subscriber.onNext("Hello");
                        subscriber.onNext("Hi");
                        subscriber.onNext("Aloha");
                        subscriber.onCompleted();
                    }
                });

                Observer<String> observer1 = new Observer<String>() {

                    @Override
                    public void onNext(String s) {
                        Log.d(tag, "Item: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(tag, "Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(tag, "Error!");
                    }

                };

                Subscriber<String> subscriber1 = new Subscriber<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(tag, "Item: " + s);
                    }

                    @Override
                    public void onCompleted() {
                        Log.d(tag, "Completed!");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(tag, "Error!");
                    }
                };

                observable1.subscribe(observer1);
//                o1.subscribe(subscriber1);
                break;
            case R.id.btn2:
//                Observable observable2 = Observable.just("Hello", "Hi", "Aloha");
                String[] words = {"Hello", "Hi", "Aloha"};
                Observable observable2 = Observable.from(words);
                Action1<String> onNextAction = new Action1<String>() {
                    // onNext()
                    @Override
                    public void call(String s) {
                        Log.d(tag, s);
                    }
                };
                Action1<Throwable> onErrorAction = new Action1<Throwable>() {
                    // onError()
                    @Override
                    public void call(Throwable throwable) {
                        // Error handling
                    }
                };
                Action0 onCompletedAction = new Action0() {
                    // onCompleted()
                    @Override
                    public void call() {
                        Log.d(tag, "completed");
                    }
                };

                // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
                observable2.subscribe(onNextAction);
                // 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
                observable2.subscribe(onNextAction, onErrorAction);
                // 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
                observable2.subscribe(onNextAction, onErrorAction, onCompletedAction);
                break;
            case R.id.btn3:
                String[] names = {"lily", "jack", "david", "jerry"};
                Observable.from(names)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String name) {
                                Log.d(tag, name);
                            }
                        });


                final int drawableRes = R.mipmap.ic_launcher;


                Observable.create(new Observable.OnSubscribe<Drawable>() {
                    @Override
                    public void call(Subscriber<? super Drawable> subscriber) {
                        Drawable drawable = getResources().getDrawable(drawableRes);
                        subscriber.onNext(drawable);
                        subscriber.onCompleted();
                    }
                }).subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        ivShow.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }

                });


                break;
            case R.id.btn4:
                Observable.just(1, 2, 3, 4)
                        .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                        .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                        .subscribe(new Action1<Integer>() {
                            @Override
                            public void call(Integer number) {
                                Log.d(tag, "number:" + number);
                            }
                        });
                break;
            default:
                break;
        }
    }
}
