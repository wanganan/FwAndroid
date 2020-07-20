package com.oplus.fwandroid.common.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.trello.rxlifecycle4.components.support.RxFragment

/**
 * @author Sinaan
 * @date 2020/7/20
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：fragment共通，用于全局初始配置设置。实现类的界面初始化应写在initView()方法里
 * version: 1.0
 */
abstract class BaseFragment : RxFragment(), BaseView {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 子类view的初始化
     */
    abstract fun initView()

    override fun host(): Activity {
        return requireActivity()
    }

    override fun showToast(toast: String?) {
        Toast.makeText(activity, toast ?: "", Toast.LENGTH_SHORT).show()
    }

    override fun showLongToast(toast: String?) {
        Toast.makeText(activity, toast ?: "", Toast.LENGTH_LONG).show()
    }

    override fun showBottomDialog() {
        TODO("Not yet implemented")
    }

    override fun showCenterEditDialog() {
        TODO("Not yet implemented")
    }

    override fun showCenterPromptDialog() {
        TODO("Not yet implemented")
    }

    override fun showPopupDialog() {
        TODO("Not yet implemented")
    }

    override fun background(): Int {
        TODO("Not yet implemented")
    }
}