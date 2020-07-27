package com.oplus.fwandroid.common.di

import com.oplus.fwandroid.common.base.BaseActivity
import dagger.Subcomponent

/**
 * @author Sinaan
 * @date 2020/7/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：activity基类组件。要求 BaseActivity 的子类 Component 依赖。
 * version: 1.0
 */

/**
 * 自定义注释。
 * 注释要绑定到功能所依赖的View的生命周期，即在对应的（Activity或Fragment）下DaggerXXXComponent.builder().build().inject(this)。
 * 如果一个注射器和创建依赖对象的地方没有标记@Scope ，那么每次注入时都会创建一个新的对象，如果标记了@Scope，则在规定的生命周期内会使用同一个对象，
 * 特别注意是在规定的生命周期内单例，并不是全局单例，或者可以理解为在@Component内单例。
 * 建议一个功能模块有且仅对应一个。即该功能下的所有对象都用这一个注释。
 */
@ActivityScope
/**
 * 子组件Subcomponent（继承扩展）
 * 使用 Dagger 子组件，告知 Dagger 您希望新组件使用其他组件的一部分。新组件必须是包含共享资源的组件的子组件。
 * 子组件是【继承并扩展】父组件的对象图的组件。因此，父组件中提供的所有对象也将在子组件中提供。这样，子组件中的对象就可以依赖于父组件提供的对象。
 * 如需创建子组件的实例，您需要父组件的实例。因此，父组件向子组件提供的对象的作用域仍限定为父组件。
 * 要想定义为其他 Component 的子组件，需要使用 @Subcomponent 添加注释。
 * 子类的作用域层级必须小于父类。层级和生命周期绑定。
 * 被Subcomponent标识的组件可以直接继承使用父组件的类实例。
 * 使用步骤：
 * 1.创建新的 Dagger 模块（例如 SubcomponentsModule），并将子组件的类传递给注释的 subcomponents 属性。
 * 2.将新模块（即 SubcomponentsModule）添加到 FatherComponent。
 * 3.提供在FatherComponent接口中创建 ChildComponent 实例的 factory。【fun ChildComponent(): ChildComponent.Factory】
 * 4.最后在ChildActivity使用：DaggerFatherComponent.create().childComponent().create().inject(this)。
 * 注意：子组件不会生成DaggerChildComponent，所以只能由上述方法创建。
 *
 * 依赖dependencies（依赖关系）
 * 依赖dependencies的组件可使用直接上级暴露出来的类实例。
 * dependencies方式让Component之间更加独立，结构更加清晰，也更利于解耦。有助于实现组件化开发，不像Subcomponent一样每次继承都需要在父组件添加。
 * 如果要依赖于上级组件，需要dependencies修饰。并且作用域不能超过上级。使用方法(fatherComponent需要编译生成)：
 * FatherComponent fatherComponent = DaggerFatherComponent.create();
 * DaggerChildComponent.builder().fatherComponent(fatherComponent).build().inject(this);
 * dependencies使用步骤：
 * 1.创建父类module
 * 2.将父类module加入父组件并将方法暴露出来
 * 3.子类用dependencies依赖
 * 4.注入并调用父类方法【DaggerChildComponent.builder().fatherComponent(DaggerFatherComponent.create()).build().inject(this)】
 */
@Subcomponent(modules = [ActivityModule::class])
/**
 * 在初始化类实例时，Component首先搜索类中用Inject注解标注的构造函数属性，如果没找到，Component就会去Module中查找Provides注解标注的位置，
 */
interface ActivityComponent {

    /**
     * 在 子Component 内定义子组件 factory，以便 父Component 知道如何创建 子Component 的实例。
     */
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    /**
     * 【 此函数会告知 Dagger 某个对象希望访问该图并请求注入。Dagger 需要满足该对象所需的所有依赖项。】
     * 如果您有多个请求注入的类，则必须通过这些类的确切类型在组件中明确声明它们。
     * 例如，如果您有请求注入的 LoginActivity 和 RegistrationActivity，则需要两种 inject() 方法，而不是涵盖这两种情况的一种通用方法。
     * 接口中的函数可以具有任何名称，但在它们以参数形式接收要注入的对象时将其称为 inject() 是 Dagger 中的一种惯例。
     */
    fun inject(baseActivity: BaseActivity)

    /**
     * 这里必须将内部的类暴露出来，以便于其他依赖于（dependencies）ActivityComponent 的 Component 调用。
     * 因为ActivityComponent也是AppComponent的子组件，所以如果ActivityComponent的子依赖们也想使用AppComponent中提供的对象，则这些对象也必须在这里暴露出来。
     * 如果Component跳过ActivityComponent直接依赖于 AppComponent，则需在AppComponent中暴露对象。即只能在直接上级暴露，不然报错。
     */
    //暴露方法：fun provideFather(): Father
}