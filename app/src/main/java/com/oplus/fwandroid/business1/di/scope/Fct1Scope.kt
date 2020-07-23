package com.oplus.fwandroid.business1.di.scope

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import javax.inject.Scope

/**
 * @author Sinaan
 * @date 2020/7/22
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：自定义注释。建议一个功能模块有且仅对应一个。即该功能下的所有对象都用这一个注释。
 * 注释要绑定到功能所依赖的View的生命周期，即在对应的（Activity或Fragment）下DaggerXXXComponent.builder().build().inject(this)。
 * version: 1.0
 * 如果一个注射器和创建依赖对象的地方没有标记@Scope，那么每次注入时都会创建一个新的对象，如果标记了@Scope，则在规定的生命周期内会使用同一个对象，
 * 特别注意是在规定的生命周期内单例，并不是全局单例，或者可以理解为在@Component内单例。
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
annotation class Fct1Scope