package com.oplus.fwandroid.common.di

import dagger.Component
import javax.inject.Singleton

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Application级别的组件，提供全局工具变量
 * version: 1.0
 */
/**
 * Application级别的组件必须提供最高级别的作用域，即@Singleton。这在项目中是唯一的。
 */
@Singleton
/**
 * SubComponentsModule是子组件的集合，子组件和AppComponent是继承扩展的关系，可以直接使用内部对象。
 */
@Component(modules = [SubComponentsModule::class, ContextModule::class, DaoModule::class, GlideModule::class, NetModule::class])
interface AppComponent {
    /**
     * 提供在 父Component 接口中创建 子Component 实例的 factory。
     * 如果添加了SubComponentsModule，可以移除 inject() 方法，因为现在由 子Component 负责注入。
     * 例如：DaggerApplicationComponent.create().loginComponent().create().inject(this)
     */
    fun activityComponent(): ActivityComponent.Factory
    fun fragmentComponent(): FragmentComponent.Factory

    /**
     * 如果有直接依赖（dependencies）的 Component 需要使用对象，这里必须将内部的类暴露出来，以便于其他依赖于AppComponent 的 Component 调用。
     * 如果有子组件（Subcomponent）需要使用对象，这里就没必要将内部的类暴露出来，因为子组件是继承扩展关系，可以直接使用。
     * 如果有依赖于 AppComponent 的子组件的 Component 需要使用对象，则必须在子组件中将需要的这里的类对象暴露出来，此时这里如果也暴露的话并不冲突，不会引发错误。
     * 如果有依赖于 AppComponent 的依赖的 Component 需要使用对象，则不管这里还是子类暴露与否，都无法获取到这里的对象。需要获取的对象必须在直接依赖上级中定义，不能跨依赖获取。
     * 即 Subcomponent 直接可过渡对象，而 dependencies 不可以，需要暴露出来才可使用。
     * 如果不暴露使用，会引发类似这样的错误：
     * public abstract interface XXXComponent {
     * ^
     * com.xxx is injected at
     */
    //暴露方法：fun provideFather(): Father
}