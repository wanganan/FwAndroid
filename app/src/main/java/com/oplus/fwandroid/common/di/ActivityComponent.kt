package com.oplus.fwandroid.common.di

import com.oplus.fwandroid.common.base.BaseActivity
import dagger.Subcomponent

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：activity基类组件
 * version: 1.0
 *
 * 子组件
 * 使用 Dagger 子组件，告知 Dagger 您希望新组件使用其他组件的一部分。新组件必须是包含共享资源的组件的子组件。
 * 子组件是【继承并扩展】父组件的对象图的组件。因此，父组件中提供的所有对象也将在子组件中提供。这样，子组件中的对象就可以依赖于父组件提供的对象。
 * 如需创建子组件的实例，您需要父组件的实例。因此，父组件向子组件提供的对象的作用域仍限定为父组件。
 * 要想定义为其他 Component 的子组件，需要使用 @Subcomponent 添加注释。
 * 子类的作用域层级必须小于父类。层级和生命周期绑定。
 * 使用步骤：
 * 1.创建新的 Dagger 模块（例如 SubcomponentsModule），并将子组件的类传递给注释的 subcomponents 属性。
 * 2.将新模块（即 SubcomponentsModule）添加到 FatherComponent。
 * 3.提供在FatherComponent接口中创建 ChildComponent 实例的 factory。【fun ChildComponent(): ChildComponent.Factory】
 * 4.最后在ChildActivity使用：DaggerFatherComponent.create().childComponent().create().inject(this)。
 * 注意：子组件不会生成DaggerChildComponent，所以只能由上述方法创建。
 *
 *
 */
@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    /**
     * 在 子Component 内定义子组件 factory，以便 父Component 知道如何创建 子Component 的实例。
     */
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(baseActivity: BaseActivity)

    /**
     * 这里必须将内部的类暴露出来，以便于其他依赖于（dependencies）ActivityComponent 的 Component 调用。
     * 因为ActivityComponent也是AppComponent的子组件，所以如果ActivityComponent的子依赖们也想使用AppComponent中提供的对象，则这些对象也必须在这里暴露出来。
     * 如果Component跳过ActivityComponent直接依赖于 AppComponent，则需在AppComponent中暴露对象。即只能在直接上级暴露，不然报错。
     */
    //暴露方法：fun provideFather(): Father
}