package com.oplus.fwandroid.common.di

import android.content.Context
import com.oplus.fwandroid.common.base.BaseActivity
import com.oplus.fwandroid.common.base.BaseFragment
import com.oplus.fwandroid.common.base.BaseView
import dagger.Module
import dagger.Provides

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
@Module
class ContextModule(var context: Context) {
    @Provides
    fun provideApplicationContext(): Context = context.applicationContext

    @Provides
    fun provideActivityContext(): BaseActivity = context as BaseActivity

    @Provides
    fun provideFragmentContext(): BaseFragment = context as BaseFragment

    @Provides
    fun provideViewContext(): BaseView = context as BaseView
}