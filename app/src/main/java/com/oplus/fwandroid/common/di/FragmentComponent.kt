package com.oplus.fwandroid.common.di

import com.oplus.fwandroid.common.base.BaseFragment
import dagger.Subcomponent

/**
 * @author Sinaan
 * @date 2020/7/24
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：fragment基类组件
 * version: 1.0
 */
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentComponent
    }

    fun inject(baseFragment: BaseFragment)
}