package com.oplus.fwandroid.common.glide

import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.request.BaseRequestOptions


/**
 * @author Sinaan
 * @date 2020/7/15
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Generated API对现有的API进行的扩展，定制的自己的API。这里是对Glide的封装。
 * version: 1.0
 *
 * 定制自己的API需要借助@GlideExtension和@GlideOption这两个注解。
 * 被 @GlideExtension 注解的类应以工具类的思维编写。这种类应该有一个私有的、空的构造方法，应为 final 类型，并且仅包含静态方法。被注解的类可以含有静态变量，可以引用其他的类或对象。
 * 使用 @GlideOption 标记的方法应该为静态方法，并且返回值为 BaseRequestOptions<?>。
 * 自定义API的方法都必须是静态方法，而且第一个参数必须是RequestOptions，后面你可以加入任意多个你想自定义的参数。
 * 定制完后同样需要在Android Studio中点击菜单栏Build -> Rebuild Project，然后就可以类似这样使用了。
 * GlideApp.with(this).load(url).cacheSource().into(imageView);
 * 这个类可以当做一个工具类，里面可以封装公共配置或者自定义你需要的Glide没有的方法。
 */
@GlideExtension
object GlideApi {
    //缩略图的最小尺寸，单位：px
    private const val MINI_THUMB_SIZE = 100

    //生成一个最小缩略图
    @JvmStatic
    @JvmOverloads
    @GlideOption
    fun miniThumb(options: BaseRequestOptions<*>): BaseRequestOptions<*> {
        return options.fitCenter().override(MINI_THUMB_SIZE)
    }
}