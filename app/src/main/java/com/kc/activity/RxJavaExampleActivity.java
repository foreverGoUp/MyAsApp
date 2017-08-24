package com.kc.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kc.myasapp.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaExampleActivity extends AppCompatActivity {

    private static final String TAG = "RxJavaExampleActivity";

    private ImageView mIv;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIv = new ImageView(this);
        setContentView(R.layout.activity_rx_java_example);

        mTv = (TextView) findViewById(R.id.tv);
//        setContentView(mIv);

//        usageExample();
//        exchangeUsage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mTv.setText("thread");
            }
        }).start();
    }

    private void rxJava2Test() {
        //在任何运行bgLooper的线程中观察结果
        Thread thread = new Thread();
        Looper bgLooper = null;
        io.reactivex.Observable.just("dsd", "dfs")
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.from(bgLooper))
                .subscribe();

    }

    private void exchangeUsage() {
        Observable.just("1")
                .map(new Func1<String, Integer>() {//map可以改变一个Observable的返回类型
                    @Override
                    public Integer call(String s) {
                        return Integer.parseInt(s);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.e(TAG, "map int value=1 ? " + integer);
                    }
                });


        Observable.just("2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.e(TAG, "准备阶段");
                        mTv.setText("准备toast，time=" + System.currentTimeMillis());
                    }
                })
                .subscribeOn(Schedulers.io())//指定准备工作doOnSubscribe在主线程运行
                .flatMap(new Func1<String, Observable<Integer>>() {//flatMap可以改变一个Observable到另一个Observable。
                    @Override
                    public Observable<Integer> call(String s) {
                        Log.e(TAG, "变换阶段");
                        return Observable.just(Integer.parseInt(s));
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.e(TAG, "订阅阶段");
                Log.e(TAG, "flatMap int value=2 ? " + integer);
                Toast.makeText(RxJavaExampleActivity.this, "flatMap int value=2 ? " + integer, Toast.LENGTH_SHORT).show();
                mTv.append("| 开始toast，time=" + System.currentTimeMillis());
            }
        });

    }

    private void usageExample() {
        //打印数组中的字符串
        String[] names = new String[]{"张三", "李四", "王五"};
        Observable.from(names)
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.e(TAG, s);
                    }
                }).unsubscribe();


        //根据图片资源id显示一张图片,报错时吐司提示。
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Drawable drawable = getResources().getDrawable(R.drawable.umeng_socialize_pulltorefresh_arrow);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())//定义事件产生在io线程
                .observeOn(AndroidSchedulers.mainThread())//定义事件消费在UI线程
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RxJavaExampleActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mIv.setImageDrawable(drawable);
                    }

                });

    }

    private void createExample() {
        //创建被观察者方式1
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("dd");
                subscriber.onNext("dsl");
                subscriber.onCompleted();
            }
        });
        //创建被观察者方式2
        Observable<String> observable1 = Observable.just("dsds", "dsd", "sdsd");
        //创建被观察者方式3
        String[] arr = new String[]{"1", "2", "222"};
        Observable<String> observable2 = Observable.from(arr);

        //创建观察者1
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext");
            }
        };
        //创建观察者2
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError");
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG, "onNext" + s);
            }
        };


        //开始订阅
        observable.subscribe(observer);
        observable.subscribe(subscriber);

        //另一种方式创建订阅者：不完整定义订阅者
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e(TAG, "onNext" + s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "onError");
            }
        };
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.e(TAG, "onCompleted");
            }
        };
        observable.subscribe(onNextAction);
        observable.subscribe(onNextAction, onErrorAction);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }
}
