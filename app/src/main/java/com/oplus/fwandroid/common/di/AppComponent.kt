package com.oplus.fwandroid.common.di

import dagger.Component
import javax.inject.Singleton

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
@Singleton
@Component(modules = [SubComponentsModule::class, ContextModule::class, DaoModule::class, GlideModule::class, NetModule::class])
interface AppComponent {
    /**
     * 提供在 父Component 接口中创建 子Component 实例的 factory。
     * 如果添加了SubComponentsModule，可以移除 inject() 方法，因为现在由 子Component 负责注入。
     * 例如：DaggerApplicationComponent.create().loginComponent().create().inject(this)
     */
    fun activityComponent(): ActivityComponent.Factory
    fun fragmentComponent(): FragmentComponent.Factory
}