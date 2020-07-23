package com.oplus.fwandroid.common.net

import android.content.Context
import com.trello.rxlifecycle4.android.ActivityEvent
import com.trello.rxlifecycle4.components.RxActivity
import com.trello.rxlifecycle4.components.RxFragment
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import com.trello.rxlifecycle4.components.support.RxFragmentActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import org.reactivestreams.Publisher

/**
 * @author Sinaan
 * @date 2020/7/6
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：对执行线程和绑定生命周期的方法进行封装
 * version: 1.0
 */
object RxHelper {
    fun <T> bindObservableLifeCycle(context: Context): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream: Observable<T> ->
            val observable = upstream.subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
            composeObservableContext(context, observable)
        }
    }

    fun <T> bindObservableLifeCycle(
        context: Context,
        activityEvent: ActivityEvent
    ): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream: Observable<T> ->
            val observable = upstream.subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
            composeObservableContext(context, activityEvent, observable)
        }
    }

    fun <T> bindObservableLifeCycle(fragment: RxFragment): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(fragment.bindToLifecycle())
        }
    }

    fun <T> bindFlowableLifeCycle(context: Context): FlowableTransformer<T, T>? {
        return FlowableTransformer { upstream: Flowable<T> ->
            val flowable = upstream.subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
            composeFlowableContext(context, flowable)
        }
    }

    fun <T> bindFlowableLifeCycle(
        context: Context,
        activityEvent: ActivityEvent
    ): FlowableTransformer<T, T>? {
        return FlowableTransformer { upstream: Flowable<T> ->
            val flowable = upstream.subscribeOn(
                Schedulers.io()
            )
                .observeOn(AndroidSchedulers.mainThread())
            composeFlowableContext(context, activityEvent, flowable)
        }
    }

    fun <T> bindFlowableLifeCycle(fragment: RxFragment): FlowableTransformer<T, T>? {
        return FlowableTransformer { upstream: Flowable<T> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).compose(fragment.bindToLifecycle())
        }
    }

    private fun <T> composeObservableContext(
        context: Context,
        observable: Observable<T>
    ): ObservableSource<T>? {
        return when (context) {
            is RxActivity -> {
                observable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            is RxFragmentActivity -> {
                observable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            is RxAppCompatActivity -> {
                observable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            else -> {
                observable
            }
        }
    }

    private fun <T> composeObservableContext(
        context: Context,
        activityEvent: ActivityEvent?,
        observable: Observable<T>
    ): ObservableSource<T>? {
        return when (context) {
            is RxActivity -> {
                observable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            is RxFragmentActivity -> {
                observable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            is RxAppCompatActivity -> {
                observable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            else -> {
                observable
            }
        }
    }

    private fun <T> composeFlowableContext(
        context: Context,
        flowable: Flowable<T>
    ): Publisher<T>? {
        return when (context) {
            is RxActivity -> {
                flowable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            is RxFragmentActivity -> {
                flowable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            is RxAppCompatActivity -> {
                flowable.compose(
                    context.bindUntilEvent(
                        ActivityEvent.PAUSE
                    )
                )
            }
            else -> {
                flowable
            }
        }
    }

    private fun <T> composeFlowableContext(
        context: Context,
        activityEvent: ActivityEvent?,
        flowable: Flowable<T>
    ): Publisher<T>? {
        return when (context) {
            is RxActivity -> {
                flowable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            is RxFragmentActivity -> {
                flowable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            is RxAppCompatActivity -> {
                flowable.compose(
                    context.bindUntilEvent(activityEvent!!)
                )
            }
            else -> {
                flowable
            }
        }
    }
}