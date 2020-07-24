package com.oplus.fwandroid.common.di

import dagger.Module

/**
 * @author Sinaan
 * @date 2020/7/24
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：提供基类activity的公共变量
 * version: 1.0
 *
 * Dagger 模块modules
 * 除了 @Inject 注释之外，还有一种方法可告知 Dagger 如何提供类实例，即使用 Dagger 模块中的信息。
 * 如需将类型添加到 Dagger 图，建议的方法是使用构造函数注入（即在类的构造函数上使用 @Inject 注释）。
 * 有时，此方法不可行，即类没有构造函数，如Android 框架类（ Activity 和 Fragment）由系统实例化，因此 Dagger 无法为您创建这些类，您必须使用 Dagger 模块。
 * Dagger 模块是一个带有 @Module 注释的类。您可以在其中使用 @Provides 注释定义依赖项。
 * 模块是一种以语义方式封装有关如何提供对象信息的方法。建议对提供相同功能相关对象的逻辑进行分组（如Network=Retrofit+OkHttpClient+Gson+...）。如果应用扩展了，可以直接添加提供逻辑。
 * Module应尽量将不同功能的模块区分开。
 * 规范写法：XXXModule
 */
@Module
class ActivityModule() {
    /**
     * @Provides 方法的依赖项是该方法的参数。
     *【 Dagger 将提供图中存在的参数对象实例以满足依赖项要求。】
     * 规范写法：provide+对象类名
     */
//    @Provides
//    fun provideXX():XX=XX()
}