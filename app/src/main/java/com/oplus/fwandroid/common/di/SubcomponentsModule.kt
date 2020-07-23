package com.oplus.fwandroid.common.di

import dagger.Module

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 * 如何告知Dagger指明组件的依赖关系：
 * 1.创建新的 Dagger 模块（例如 SubcomponentsModule），并将子组件的类传递给注释的 subcomponents 属性。
 * 2.将新模块（即 SubcomponentsModule）添加到 父Component 中。
 * 3.提供在 父Component 接口中创建 子Component 实例的 factory。
 */
@Module(subcomponents = [ActivityComponent::class])
class SubComponentsModule {
}