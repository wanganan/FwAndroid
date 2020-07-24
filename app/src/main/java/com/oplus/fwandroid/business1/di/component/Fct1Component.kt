package com.oplus.fwandroid.business1.di.component

import com.oplus.fwandroid.business1.di.module.Fct1DataModule
import com.oplus.fwandroid.business1.di.module.Fct1UIModule
import com.oplus.fwandroid.business1.di.scope.Fct1Scope
import com.oplus.fwandroid.business1.ui.activity.Fct1Activity
import com.oplus.fwandroid.common.di.ActivityComponent
import dagger.Component

/**
 * @author Sinaan
 * @date 2020/7/22
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Dagger 组件。按照功能添加注入对象。
 * version: 1.0
 */
@Component(
    modules = [Fct1UIModule::class, Fct1DataModule::class],
    dependencies = [ActivityComponent::class]
)
@Fct1Scope
interface Fct1Component {
    fun inject(activity: Fct1Activity)
}