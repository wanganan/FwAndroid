package com.oplus.fwandroid.common.di

import dagger.Component
import javax.inject.Singleton

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Application级别的组件，提供全局工具变量。要求 ActivityComponent 和 FragmentComponent 作为子组件继承。
 * version: 1.0
 *
 * Dagger 组件。
 * Dagger 可以在您的项目中创建一个依赖关系图，然后它可以从该图中了解在需要这些依赖项时从何处获取它们。
 * 为了让 Dagger 执行此操作，您需要创建一个接口，并使用 @Component 为其添加注释。Dagger 会创建一个容器，就像您在手动注入依赖项时执行的操作一样。
 * Dagger 是通过 Component 来确认需求与依赖对象的，可以说 Component 是他们之间的纽带。
 * 在 @Component 接口内，您可以定义返回所需类的实例的函数。@Component 会让 Dagger 生成一个容器，其中应包含满足其提供的类型所需的所有依赖项。
 * 这称为 Dagger 组件；它包含一个图，其中包括 Dagger 知道如何提供的对象及其各自的依赖项。
 * 在您【构建项目】时，Dagger 会为您生成 XXXComponent 接口的实现：DaggerXXXComponent。Dagger 会通过其注释处理器创建一个依赖关系图。
 * 为了使 Dagger 图了解模块，您必须将所需的模块用 modules 符号添加到 @Component 接口。
 * SubComponentsModule是子组件的集合，子组件和AppComponent是继承扩展的关系，可以直接使用内部对象。
 * Modules中的module不分先后顺序，能够互相引用。
 */
@Component(modules = [SubComponentsModule::class, ContextModule::class, DaoModule::class, GlideModule::class, NetModule::class])
/**
 * Dagger 中的作用域限定
 * 您可以使用作用域注释将某个对象的生命周期限定为其组件的生命周期。这意味着，每次需要提供该类型时，都会使用依赖项的同一实例。
 * 为了在请求 XXXComponent 中的代码库时获得其中某个对象的唯一实例，@Component 接口和 对象 都要添加同一作用域注释。
 * 您可以使用 Dagger 使用的 javax.inject 软件包随附的 @Singleton 注释，也可以创建并使用自定义作用域注释。
 * 但 @Singleton 是 javax.inject 软件包随附的唯一一个作用域注释。建议使用它为 ApplicationComponent 以及要在整个应用中重复使用的对象添加注释。
 * 建议根据注释的使用生命周期，为作用域注释命名。示例包括 @ApplicationScope、@LoggedUserScope 和 @ActivityScope。
 * 请注意，在将作用域应用于对象时，不要引发内存泄漏。只要限定作用域的组件在内存中，创建的对象就也在内存中。
 * 因为 ApplicationComponent 是在应用启动时（在应用类中）创建的，所以它会随着应用的销毁而被销毁。
 * Application级别的组件必须提供最高级别的作用域，即@Singleton。这在项目中是唯一的。
 */
@Singleton
/**
 * 在初始化类实例时，Component首先搜索类中用Inject注解标注的构造函数属性，如果没找到，Component就会去Module中查找Provides注解标注的位置，
 * 规范写法：XXXComponent
 */
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