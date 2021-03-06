package com.oplus.fwandroid.common.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.hjq.language.LanguagesManager
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.utils.ManifestHelper
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

        // 提供 ActivityComponent 图供子类使用，并实例化@Inject标注的属性。子类可直接在其内部通过@Inject关联对象，无需在这里关联。
        (applicationContext as BaseApplication).appComponent.activityComponent().create()
            .inject(this)

        back.setOnClickListener { finish() }
        content.text = content()
        titleBar.visibility = if ("" != content.text) View.VISIBLE else View.GONE

        next.text = next()
        next.visibility = if ("" != next.text) View.VISIBLE else View.GONE
        next.setOnClickListener { nextAction() }

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

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

    override fun host(): Activity {
        return this
    }

    override fun showToast(toast: String?) {
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

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            //重写字体缩放比例
            val config = res.configuration
            //初始化全局字体设置，如果项目中需要全局缩放字体大小时请修改manifest中font_scale的值。
            config.fontScale = ManifestHelper.getMetaData(this, ManifestHelper.FONT_SCALE).toFloat()
            res.updateConfiguration(config, res.displayMetrics)
        }
        return res
    }

    override fun attachBaseContext(newBase: Context) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            //重写字体缩放比例
            val res = newBase.resources
            val config = res.configuration
            //初始化全局字体设置，如果项目中需要全局缩放字体大小时请修改manifest中font_scale的值。
            config.fontScale =
                ManifestHelper.getMetaData(newBase, ManifestHelper.FONT_SCALE).toFloat()
            val newContext = newBase.createConfigurationContext(config)
            super.attachBaseContext(LanguagesManager.attach(newContext))// 绑定语种
        } else {
            super.attachBaseContext(LanguagesManager.attach(newBase))// 绑定语种
        }
    }
}