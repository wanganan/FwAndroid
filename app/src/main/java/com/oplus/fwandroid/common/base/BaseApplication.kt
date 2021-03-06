package com.oplus.fwandroid.common.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.multidex.MultiDex
import com.hjq.language.LanguagesManager
import com.oplus.fwandroid.BuildConfig
import com.oplus.fwandroid.common.Global
import com.oplus.fwandroid.common.di.AppComponent
import com.oplus.fwandroid.common.di.DaggerAppComponent
import com.oplus.fwandroid.common.utils.PreferencesUtil
import com.orhanobut.logger.*
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits


/**
 * @author Sinaan
 * @date 2020/6/24
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
open class BaseApplication : Application() {

    //将 AppComponent 图提供给其他 Android 框架类使用。
    val appComponent: AppComponent = DaggerAppComponent.create()

    companion object {
        //@JvmStatic是为了方便java中调用，即BaseApplication.getInstance()。否则在需要BaseApplication.Companion.getInstance()。
        //lateinit表示这个属性开始是没有值的，但是，在使用前将被赋值（否则，就会抛出异常）。
        //private set用于说明外部类不能对其进行赋值。
        @JvmStatic
        lateinit var instance: Application
            private set

        @JvmStatic
        lateinit var currentActivity: Activity
            private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        //Logger配置
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(0) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            .logStrategy(LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag(BuildConfig.APPLICATION_ID) // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        //将Log日志保存到文件中，默认路径：storage/emulated/0/logger
//        Logger.addLogAdapter(DiskLogAdapter(formatStrategy))

        //模拟token
        val releaseToken =
            "196242c2f5e09c5be7ba1f82593278972d50fe68c316ae2a50d43fa8340f48f03cda8b8e33323f52f56c2a4093985d79a235e9864aaf0fd04244bdd3bac5800a" //线上token
        val testToken =
            "12cf3d8e92e725f724882ef5269297b21f67e78681e5e4060d3ba09a5159403b05643f5e2d5b1e290281004adcd9ae39242e793268b99a00c1048f762627bdc6" //测试token
        PreferencesUtil.put(Global.User.SHPNAME, Global.User.token, releaseToken)

        //获取当前栈顶（界面正在显示）的Activity
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        //安装Dalvik可执行文件分包支持，android 5.0（minSdkVersion<=20）以下的设备需要。
        //否则当Dex文件突破65536方法数量时，打包时就会抛出异常。【Cannot fit requested classes in a single dex file (# methods: 70534 > 65536)】
        MultiDex.install(this)

        //屏幕适配：对单位的自定义配置, 请在App启动时完成。如果你项目用副单位开发请打开进行设置。
//        configUnits()

        //初始化国际化框架，如果项目中用到了语言切换请打开。默认跟随系统语言变化。切换方法见README。
//        LanguagesManager.init(this)
    }

    /**
     * ContextWrapper的方法，在onCreate()前调用，可以在这里进行全局配置。
     * super.attachBaseContext前不能调用getApplicationContext()，会报NullPointerException
     */
    override fun attachBaseContext(base: Context?) {
        //国际化适配（绑定语种）
        //只要是 Context 的子类都需要重写，Activity，Service 也雷同
        super.attachBaseContext(LanguagesManager.attach(base))
    }

    /**
     * 注意!!! 布局时的实时预览在开发阶段是一个很重要的环节, 很多情况下 Android Studio 提供的默认预览设备并不能完全展示我们的设计图
     * 所以我们就需要自己创建模拟设备, 以下链接是给大家的福利, 按照链接中的操作可以让预览效果和设计图完全一致!
     * @see <a href="https://github.com/JessYanCoding/AndroidAutoSize/blob/master/README-zh.md#preview">dp、pt、in、mm 这四种单位的模拟设备创建方法</a>
     * <p>
     * v0.9.0 以后, AndroidAutoSize 强势升级, 将这个方案做到极致, 现在支持5种单位 (dp、sp、pt、in、mm)
     * {@link UnitsManager} 可以让使用者随意配置自己想使用的单位类型
     * 其中 dp、sp 这两个是比较常见的单位, 作为 AndroidAutoSize 的主单位, 默认被 AndroidAutoSize 支持
     * pt、in、mm 这三个是比较少见的单位, 只可以选择其中的一个, 作为 AndroidAutoSize 的副单位, 与 dp、sp 一起被 AndroidAutoSize 支持
     * 副单位是用于规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
     * 您选择什么单位, 就在 layout 文件中用什么单位布局
     * <p>
     * 两个主单位和一个副单位, 可以随时使用 {@link UnitsManager} 的方法关闭和重新开启对它们的支持
     * 如果您想完全规避修改 {@link DisplayMetrics#density} 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
     * 那请调用 {@link UnitsManager#setSupportDP}、{@link UnitsManager#setSupportSP} 都设置为 {@code false}
     * 停止对两个主单位的支持 (如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持)
     * 并调用 {@link UnitsManager#setSupportSubunits} 从三个冷门单位中选择一个作为副单位
     * 三个单位的效果都是一样的, 按自己的喜好选择, 比如我就喜欢 mm, 翻译为中文是妹妹的意思
     * 然后在 layout 文件中只使用这个副单位进行布局, 这样就可以完全规避修改 {@link DisplayMetrics#density} 所造成的不良影响
     * 因为 dp、sp 这两个单位在其他系统控件或三方库控件中都非常常见, 但三个冷门单位却非常少见
     */
    private fun configUnits() {
        //AndroidAutoSize 默认开启对 dp 的支持, 调用 UnitsManager.setSupportDP(false); 可以关闭对 dp 的支持
        //主单位 dp 和 副单位可以同时开启的原因是, 对于旧项目中已经使用了 dp 进行布局的页面的兼容
        //让开发者的旧项目可以渐进式的从 dp 切换到副单位, 即新页面用副单位进行布局, 然后抽时间逐渐的将旧页面的布局单位从 dp 改为副单位
        //最后将 dp 全部改为副单位后, 再使用 UnitsManager.setSupportDP(false); 将 dp 的支持关闭, 彻底隔离修改 density 所造成的不良影响
        //如果项目完全使用副单位, 则可以直接以像素为单位填写 AndroidManifest 中需要填写的设计图尺寸, 不需再把像素转化为 dp
        AutoSizeConfig.getInstance()
            .setBaseOnWidth(true)
            .unitsManager
            .setSupportDP(false)
            //当使用者想将旧项目从主单位过渡到副单位, 或从副单位过渡到主单位时
            //因为在使用主单位时, 建议在 AndroidManifest 中填写设计图的 dp 尺寸, 比如 360 * 640
            //而副单位有一个特性是可以直接在 AndroidManifest 中填写设计图的 px 尺寸, 比如 1080 * 1920
            //但在 AndroidManifest 中却只能填写一套设计图尺寸, 并且已经填写了主单位的设计图尺寸
            //所以当项目中同时存在副单位和主单位, 并且副单位的设计图尺寸与主单位的设计图尺寸不同时, 可以通过 UnitsManager#setDesignSize() 方法配置
            //如果副单位的设计图尺寸与主单位的设计图尺寸相同, 则不需要调用 UnitsManager#setDesignSize(), 框架会自动使用 AndroidManifest 中填写的设计图尺寸
            //.setDesignSize(2160, 3840)
            //AndroidAutoSize 默认开启对 sp 的支持, 调用 UnitsManager.setSupportSP(false); 可以关闭对 sp 的支持
            //如果关闭对 sp 的支持, 在布局时就应该使用副单位填写字体的尺寸
            //如果开启 sp, 对其他三方库控件影响不大, 也可以不关闭对 sp 的支持, 这里我就继续开启 sp, 请自行斟酌自己的项目是否需要关闭对 sp 的支持
            .setSupportSP(false)
            //AndroidAutoSize 默认不支持副单位, 调用 UnitsManager#setSupportSubunits() 可选择一个自己心仪的副单位, 并开启对副单位的支持
            //只能在 pt、in、mm 这三个冷门单位中选择一个作为副单位, 三个单位的适配效果其实都是一样的, 您觉的哪个单位看起顺眼就用哪个
            //副单位是用于规避修改 DisplayMetrics#density 所造成的对于其他使用 dp 布局的系统控件或三方库控件的不良影响
            //您选择什么单位就在 layout 文件中用什么单位进行布局
            .supportSubunits = Subunits.MM
        //开启支持 Fragment 自定义参数的功能
        AutoSizeConfig.getInstance().isCustomFragment = true
    }

}