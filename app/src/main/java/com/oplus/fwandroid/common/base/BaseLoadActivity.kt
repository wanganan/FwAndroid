package com.oplus.fwandroid.common.base

import android.os.Bundle
import com.oplus.fwandroid.R
import kotlinx.android.synthetic.main.activity_base_load.*

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：基本的加载页面，实现加载框显示和加载失败替换布局，用到加载的页面要实现该类
 * version: 1.0
 */
abstract class BaseLoadActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //这里id不能写成container，否则会在执行时覆盖掉BaseActivity里的container.addView
        container_load.addView(layoutInflater.inflate(layoutLoad(), null))

        loadingView.setOnRetryClickListener {
            loadingRun()
            loadInitialData()
        }
        //因为界面是从无到有，所以只第一次和重新加载时显示。之后的刷新加载框由子类实现（refreshLayout）
        loadingRun()
        loadInitialData()
    }

    override fun layout(): Int {
        return R.layout.activity_base_load
    }

    /**
     * 子类的布局Id
     */
    abstract fun layoutLoad(): Int

    /**
     * 初始加载数据
     */
    abstract fun loadInitialData()

    /**
     * 开始加载，显示进度框
     */
    private fun loadingRun() {
        loadingView.showLoading()
    }

    /**
     * 加载完成，显示对应状态
     */
    fun loadingTerminate(status: LoadStatus) {
        when (status) {
            LoadStatus.ERROR -> loadingView.showError()
            LoadStatus.EMPTY -> loadingView.showEmpty()
            LoadStatus.OK -> loadingView.showContent()
        }
    }
    enum class LoadStatus { ERROR, EMPTY, OK }

}