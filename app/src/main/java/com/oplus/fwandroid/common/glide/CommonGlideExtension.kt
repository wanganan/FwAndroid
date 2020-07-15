package com.oplus.fwandroid.common.glide


/**
 * @author Sinaan
 * @date 2020/7/15
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Generated API对现有的API进行的扩展，定制的自己的API。这里是对Glide的封装。
 * version: 1.0
 *
 * 定制自己的API需要借助@GlideExtension和@GlideOption这两个注解。类的构造函数需要声明成private，这都是必须要求的写法。
 * 自定义API的方法都必须是静态方法，而且第一个参数必须是RequestOptions，后面你可以加入任意多个你想自定义的参数。
 * 定制完后同样需要在Android Studio中点击菜单栏Build -> Rebuild Project，然后就可以类似这样使用了。
 * GlideApp.with(this).load(url).cacheSource().into(imageView);
 */
//@GlideExtension
//open class CommonGlideExtension  {
//    private constructor()
//    @GlideOption
//    fun cacheSource(options: RequestOptions) {
//        options.diskCacheStrategy(DiskCacheStrategy.DATA)
//    }
//}