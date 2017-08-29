package com.kc.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kc.myasapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxJava2UseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java2_use);
    }

    private void schedulerUse() {

    }

    /**
     * 操作符的使用
     * <p>
     * 以上就是一些常用的操作符，通过操作符的使用。我们每次调用一次操作符，就进行一次观察者对象的改变
     * ，同时将需要传递的数据进行转变，最终Observer对象获得想要的数据。
     * <p>
     * 以网络加载为例，我们通过Observable开启子线程，进行一些网络请求获取数据的操作，获得到网络数据后，
     * 然后通过操作符进行转换，获得我们想要的形式的数据，然后传递给Observer对象。
     * <p>
     * 作者：Ruheng
     * 链接：http://www.jianshu.com/p/d149043d103a
     * 來源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */
    private void operationSignUse() {
        //map操作符
        //map()操作符，就是把原来的Observable对象转换成另一个Observable对象，同时将传输的数据进行一些灵活的操作，方便Observer获得想要的数据形式。
        Observable<Integer> observable = Observable.just("hello").map(new Function<String, Integer>() {
            @Override
            public Integer apply(String s) throws Exception {
                return s.length();
            }
        });
        //flatMap
        //flatMap()对于数据的转换比map()更加彻底，如果发送的数据是集合
        // ，flatmap()重新生成一个Observable对象，并把数据转换成Observer想要的数据形式。
        // 它可以返回任何它想返回的Observable对象。
        List<String> list = new ArrayList();
        Observable<Object> observable1 = Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        });
        //filter()操作符
        //
        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object s) throws Exception {
                String newStr = (String) s;
                if (newStr.charAt(5) - '0' > 5) {
                    return true;
                }
                return false;
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println((String) o);
            }
        });
        //take()操作符
        //take()操作符：输出最多指定数量的结果。
        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).take(5).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                System.out.println((String) s);
            }
        });
        //doOnNext()
        //doOnNext()允许我们在每次输出一个元素之前做一些额外的事情。
        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).take(5).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("准备工作");
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                System.out.println((String) s);
            }
        });
    }


    /**
     * 重要思想：Observable (被观察者)只有在被Observer (观察者)订阅后才能执行其内部的相关逻辑
     */
    private void create() {
        //disposable为订阅关系，可调用其dispose()方法解除可观察者和观察者的订阅关系。
        //这是第1种创建可观察者的方式
        Disposable disposable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                //进行一些逻辑处理
                e.onNext("fdf");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //观察者接收数据
                    }
                });
        disposable.dispose();
        //new Observer为创建观察者的第2种方式
        //通过just方法创建可观察者
        Observable.just("dsdsd")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //d为订阅关系
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        //观察者接收数据
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //3、通过几乎所有的容器创建可观察者
        List<String> list = new ArrayList<>();
        Observable.fromIterable(list)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {

                    }
                });
        //4、通过defer创建可观察者
        //当观察者订阅时，才创建Observable，并且针对每个观察者创建都是一个新的Observable。以何种方式创建这个Observable对象，当满足回调条件后，就会进行相应的回调。
        Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("dsdsd");
            }
        });
        //5、通过defer创建可观察者
        //创建一个按固定时间间隔发射整数序列的Observable，可用作定时器。即按照固定2秒一次调用onNext()方法。
        Disposable disposable1 = Observable.interval(1, 1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
        //6/range( )方式
        //创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，如果为0则不发送，负数则抛异常。上述表示发射1到20的数。即调用20次nNext()方法，依次传入1-20数字。
        Disposable disposable2 = Observable.range(1, 20)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {

                    }
                });
        //7、timer( )方式
        //创建一个Observable，它在一个给定的延迟后发射一个特殊的值，即表示延迟2秒后，调用onNext()方法。
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
        //8、repeat( )方式
        //创建一个Observable，该Observable的事件可以重复调用。
        Observable<Integer> observable = Observable.just(123).repeat();
    }
}
