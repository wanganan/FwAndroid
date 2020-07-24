package com.oplus.fwandroid.business1.di.module

import com.oplus.fwandroid.business1.di.scope.Fct1Scope
import com.oplus.fwandroid.business1.model.Fct1Model
import com.oplus.fwandroid.business1.presenter.Fct1Presenter
import com.oplus.fwandroid.business1.ui.activity.Fct1Activity
import com.oplus.fwandroid.business1.ui.adapter.Fct1Adapter
import dagger.Module
import dagger.Provides

/**
 * @author Sinaan
 * @date 2020/7/22
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
@Module
class Fct1UIModule(private var view: Fct1Activity, private var layoutId: Int) {

    @Provides
    fun provideFct1Activity(): Fct1Activity = view

    @Fct1Scope
    @Provides
    fun provideFct1Presenter(fct1Model: Fct1Model): Fct1Presenter = Fct1Presenter(view, fct1Model)

    @Provides
    fun provideFct1Adapter(): Fct1Adapter = Fct1Adapter(layoutId, mutableListOf())
}