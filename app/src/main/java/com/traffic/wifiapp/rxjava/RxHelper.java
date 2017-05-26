package com.traffic.wifiapp.rxjava;



import com.traffic.wifiapp.retrofit.BaseModel;
import com.traffic.wifiapp.retrofit.ServerException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Jam on 16-6-12
 * Description: Rx 一些巧妙的处理
 */
public class RxHelper {
    /**
     * 对结果进行预处理
     *
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<BaseModel<T>, T> handleResult() {

        return tObservable -> tObservable.flatMap(new Func1<BaseModel<T>, Observable<T>>() {
            @Override
            public Observable<T> call(BaseModel<T> result) {
                if (result.success()) {
                    return createData(result.data);
                } else {
                    return Observable.error(new ServerException(result.msg));
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(data);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });

    }




}
