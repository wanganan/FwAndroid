package com.oplus.fwandroid.business1.di.component

import com.oplus.fwandroid.business1.di.module.Fct1DataModule
import com.oplus.fwandroid.business1.di.module.Fct1UIModule
import com.oplus.fwandroid.business1.di.scope.Fct1Scope
import com.oplus.fwandroid.business1.ui.activity.Fct1Activity
import dagger.Component

/**
 * @author Sinaan
 * @date 2020/7/22
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Dagger 组件。按照功能添加注入对象。
 * version: 1.0
 * Dagger 可以在您的项目中创建一个依赖关系图，然后它可以从该图中了解在需要这些依赖项时从何处获取它们。
 * 为了让 Dagger 执行此操作，您需要创建一个接口，并使用 @Component 为其添加注释。Dagger 会创建一个容器，就像您在手动注入依赖项时执行的操作一样。
 * Dagger 是通过 Component 来确认需求与依赖对象的，可以说 Component 是他们之间的纽带。
 * 在 @Component 接口内，您可以定义返回所需类的实例的函数。@Component 会让 Dagger 生成一个容器，其中应包含满足其提供的类型所需的所有依赖项。
 * 这称为 Dagger 组件；它包含一个图，其中包括 Dagger 知道如何提供的对象及其各自的依赖项。
 * 在您【构建项目】时，Dagger 会为您生成 XXXComponent 接口的实现：DaggerXXXComponent。Dagger 会通过其注释处理器创建一个依赖关系图。
 * 为了使 Dagger 图了解模块，您必须将所需的模块用 modules 符号添加到 @Component 接口。
 */
@Component(modules = [Fct1UIModule::class, Fct1DataModule::class])
/**
 * Dagger 中的作用域限定
 * 您可以使用作用域注释将某个对象的生命周期限定为其组件的生命周期。这意味着，每次需要提供该类型时，都会使用依赖项的同一实例。
 * 为了在请求 XXXComponent 中的代码库时获得其中某个对象的唯一实例，@Component 接口和 对象 都要添加同一作用域注释。
 * 您可以使用 Dagger 使用的 javax.inject 软件包随附的 @Singleton 注释，也可以创建并使用自定义作用域注释。
 * 但 @Singleton 是 javax.inject 软件包随附的唯一一个作用域注释。建议使用它为 ApplicationComponent 以及要在整个应用中重复使用的对象添加注释。
 * 建议根据注释的使用生命周期，为作用域注释命名。示例包括 @ApplicationScope、@LoggedUserScope 和 @ActivityScope。
 * 请注意，在将作用域应用于对象时，不要引发内存泄漏。只要限定作用域的组件在内存中，创建的对象就也在内存中。
 * 因为 ApplicationComponent 是在应用启动时（在应用类中）创建的，所以它会随着应用的销毁而被销毁。
 */
@Fct1Scope
interface Fct1Component {
    /**
     * 【 此函数会告知 Dagger 某个对象希望访问该图并请求注入。Dagger 需要满足该对象所需的所有依赖项。】
     * 如果您有多个请求注入的类，则必须通过这些类的确切类型在组件中明确声明它们。
     * 例如，如果您有请求注入的 LoginActivity 和 RegistrationActivity，则需要两种 inject() 方法，而不是涵盖这两种情况的一种通用方法。
     * 接口中的函数可以具有任何名称，但在它们以参数形式接收要注入的对象时将其称为 inject() 是 Dagger 中的一种惯例。
     */
    fun inject(activity: Fct1Activity)
}