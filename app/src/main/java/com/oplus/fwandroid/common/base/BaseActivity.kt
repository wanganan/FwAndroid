package com.oplus.fwandroid.common.base

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.oplus.fwandroid.R
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：activity共通，用于全局初始配置设置。实现类的界面初始化应写在initView()方法里
 * version: 1.0
 */
abstract class BaseActivity : RxAppCompatActivity(), BaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        container.addView(layoutInflater.inflate(layout(), null))

        //沉浸式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }

        back.setOnClickListener { finish() }
        content.text = content()
        titleBar.visibility = if ("" != content.text) View.VISIBLE else View.GONE

        next.text = next()
        next.visibility = if ("" != next.text) View.VISIBLE else View.GONE
        next.setOnClickListener { nextAction() }

        initView()
    }

    /**
     * 子类布局id
     */
    abstract fun layout(): Int

    /**
     * 标题内容，""则不显示标题栏
     */
    abstract fun content(): String

    /**
     * 标题栏右侧文本，""则不显示
     */
    abstract fun next(): String

    /**
     * 标题栏右侧点击事件
     */
    abstract fun nextAction()

    /**
     * 子类view的初始化
     */
    abstract fun initView()

    override fun host(): Activity {
        return this
    }

    override fun showShortToast(toast: String?) {
        Toast.makeText(this, toast ?: "", Toast.LENGTH_SHORT).show()
    }

    override fun showLongToast(toast: String?) {
        Toast.makeText(this, toast ?: "", Toast.LENGTH_LONG).show()
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