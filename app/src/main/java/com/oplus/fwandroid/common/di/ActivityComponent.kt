package com.oplus.fwandroid.common.di

import dagger.Subcomponent

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
/**
 * 子组件
 * 使用 Dagger 子组件，告知 Dagger 您希望新组件使用其他组件的一部分。新组件必须是包含共享资源的组件的子组件。
 * 子组件是【继承并扩展】父组件的对象图的组件。因此，父组件中提供的所有对象也将在子组件中提供。这样，子组件中的对象就可以依赖于父组件提供的对象。
 * 如需创建子组件的实例，您需要父组件的实例。因此，父组件向子组件提供的对象的作用域仍限定为父组件。
 * 要想定义为其他 Component 的子组件，需要使用 @Subcomponent 添加注释：
 */
@Subcomponent
interface ActivityComponent {
    /**
     * 在 子Component 内定义子组件 factory，以便 父Component 知道如何创建 子Component 的实例。
     */
    interface Factory{
        fun create():ActivityComponent
    }

    fun inject()
}