package com.oplus.fwandroid.business1.di.module

import com.oplus.fwandroid.business1.model.Fct1Model
import com.oplus.fwandroid.business1.model.api.Fct1API
import com.oplus.fwandroid.business1.ui.activity.Fct1Activity
import com.oplus.fwandroid.common.net.RetrofitHelper
import dagger.Module
import dagger.Provides

/**
 * @author Sinaan
 * @date 2020/7/22
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：将不同功能的模块区分开。
 * version: 1.0
 */
@Module
class Fct1DataModule {
    @Provides
    fun provideFct1Model(fct1Api: Fct1API, fct1Activity: Fct1Activity): Fct1Model =
        Fct1Model(fct1Api, fct1Activity)

    @Provides
    fun provideFct1Api(): Fct1API = RetrofitHelper.build().create(Fct1API::class.java)

}