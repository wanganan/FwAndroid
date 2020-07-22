package com.oplus.fwandroid.common.net

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.oplus.fwandroid.BuildConfig
import com.oplus.fwandroid.common.Global
import com.oplus.fwandroid.common.base.BaseApplication
import com.oplus.fwandroid.common.utils.PreferencesUtil
import com.oplus.fwandroid.common.utils.StringUtils
import com.orhanobut.logger.Logger
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * @author Sinaan
 * @date 2020/7/3
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：Retrofit全局配置
 * version: 1.0
 */
object RetrofitHelper {

    private const val DEFAULT_TIME_OUT = 10 //连接超时时间s
    private const val DEFAULT_READ_TIME_OUT = 15//读写超时时间s
    private const val DEFAULT_CACHE_TIME_OUT = 3 * 24 * 60 * 60//缓存有效期s，默认缓存3天，只缓存Get请求
    private const val DEFAULT_CACHE_SIZE: Long = 10 * 1024 * 1024//缓存文件大小byte
    private var retrofit: Retrofit? = null
    private var body: String? = null
    private var code = 0

    /**
     * 获取对应的Service
     */
    fun build(): Retrofit {

        //添加公共Header
        val headerInterceptor = Interceptor { chain ->
            val request: Request = chain.request() //获取原始请求
            val builder: Request.Builder = request.newBuilder() //建立新的请求
                /**
                 * 可接受的响应内容类型,告诉服务端消息主体是序列化的JSON字符串。
                 * "application/x-www-form-urlencoded",HTTP中默认的提交数据的方式。提交的数据按照 key1=val1&key2=val2 的方式进行编码，key和val都进行了URL转码。
                 */
                .addHeader("Accept", "application/vnd.github.v3.full+json")
                /**
                 * 请求体的MIME类型 （用于POST和PUT请求中)
                 */
                .addHeader("Content-Type", "application/vnd.github.v3.full+json; charset=utf-8")
                /**
                 * 实现client到web-server能支持长连接，默认keep-alive
                 */
                //.addHeader("Connection", "keep-alive")
                /**
                 * 以8进制表示的HTTP消息实体的传输长度,告知服务端需要接受的数据包大小。
                 */
                //.addHeader("Content-Length","1076")
                /**
                 * 用来指定当前的请求/回复中的，是否使用缓存机制。默认no-cache，即不直接使用缓存。
                 * max-age告知服务器客户端希望接收一个存在时间不大于多少秒的资源。
                 * maxAge:没有超出maxAge,不管怎么样都是返回缓存数据，超过了maxAge,发起新的请求获取数据更新，请求失败返回缓存数据。
                 * maxStale:没有超过maxStale，不管怎么样都返回缓存数据，超过了maxStale,发起请求获取更新数据，请求失败返回失败
                 * 缓存指令是单向的，即请求中存在一个指令并不意味着响应中将存在同一个指令
                 */
                //.addHeader("Cache-Control", "public,max-age=640000")
                /**
                 * 用于请求头中，指定第一个字节的位置和最后一个字节的位置
                 * 表示头500个字节：Range: bytes=0-499
                 * 表示第二个500字节：Range: bytes=500-999
                 * 表示最后500个字节：Range: bytes=-500
                 * 表示500字节以后的范围：Range: bytes=500-
                 * 第一个和最后一个字节：Range: bytes=0-0,-1
                 * 同时指定几个范围：Range: bytes=500-600,601-999
                 */
                //.addHeader("Range", "bytes=0-499")
                /**
                 * 用于响应头，指定整个实体中的一部分的插入位置，他也指示了整个实体的长度。在服务器向客户返回一个部分响应，它必须描述响应覆盖的范围和整个实体长度。
                 */
                //.addHeader("Content-Range", "bytes 0-800/801")
                .removeHeader("User-Agent")
                /**
                 * 浏览器的身份标识字符串
                 */
                .addHeader("User-Agent", BuildConfig.APPLICATION_ID)
                /**
                 * Authorization认证
                 * 假设服务端有一个收藏的接口，该接口的调用必须要先判断用户是否已经登录，但是我们不能在每一次接口调用的时候都携带上用户登录信息，这样太麻烦了，
                 * 最好能有一种方式能够自动携带上这些东西，那就是Authorization认证。
                 *
                 * 在构建OkHttpClient的时候，有一个方法authenticator，该方法接收一个Authenticator对象，该对象中有一个authenticate方法。
                 * 当我们发起一个网络请求的时候，OkHttp会自动重试未验证的请求。当响应是401 Not Authorized时，Authenticator会被要求提供证书。
                 * 这个时候系统就会调用该方法来获取用户信息并重新发起请求。Authenticator的实现中需要建立一个新的包含证书的请求。如果没有证书可用, 返回null来跳过尝试。
                 * Authorization的值是一个字符串，但是这个字符串我们一般使用Base64对其进行编码，所以这里使用Credentials类中的basic方法来构建这个字符串，实际就是对字符串进行Base64编码。
                 * final String credential = Credentials.basic(api_key, api_secret);
                 */
//                .addHeader("Authorization", "credential")
                .method(request.method, request.body)
            var token = PreferencesUtil.getString(Global.User.SHPNAME, Global.User.token)
            if (StringUtils.isEmpty(token)) {
                return@Interceptor chain.proceed(builder.build())
            }
            var tokenRequest: Request = builder
                .header("token", token)
                .build()
            chain.proceed(tokenRequest)
        }

        //添加公共参数(GET+POST)
        val parameterInterceptor = Interceptor { chain ->
            val request: Request = chain.request() //获取原始请求
            val httpUrl: HttpUrl = request.url.newBuilder() //建立新的请求
                .addQueryParameter("platform", "android")
                .addQueryParameter("version", "1.0.0")
                .addQueryParameter("device", "huawei")
                .build()
            chain.proceed(request.newBuilder().url(httpUrl).build());
        }

        /**
         * 缓存原理
         * OkHttp请求过程：OkHttp的缓存机制（CacheInterceptor）会自动判断我们提交的Request中的Cache-Control头。
         * 如果是only-if-cache（FORCE_CACHE），则只能从缓存中获取，不能进行网络请求，如果获取缓存失败，则返回一个504的错误响应码；如果是no-cache则只从网络中获取。
         * OkHttp响应过程：当正常的网络请求返回之后，CacheInterceptor会自动判断Response的Cache-Control头，
         * 如果是only-if-cache，则会缓存到本地；如果是no-cache，则不缓存。
         * OkHttp只缓存GET请求，而且缓存是以URL作为键值（key）存储在本地，如果url改变（包括url参数改变），都会重新发起网络请求而不会使用缓存。
         */
        //设置断网缓存
        val offlineCacheInterceptor = Interceptor { chain ->
            //实现网络未连接时一直使用缓存数据
            var builder = chain.request().newBuilder()
            /**
             * 表示在无网络的情况下，在多少秒内使用缓存数据。如果已经强制使用缓存CacheControl.FORCE_CACHE，则缓存时间设置无效。源码内部已经设为无限大
             * public static final CacheControl FORCE_CACHE = new Builder()
             * .onlyIfCached()
             * .maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS)
             * .build();
             */
            if (!isNetworkAvailable()) {
                //无网下强制使用缓存
                builder.cacheControl(CacheControl.FORCE_CACHE); //等同于添加only-if-cache
            }
            chain.proceed(builder.build())
        }
        //设置联网缓存
        val netCacheInterceptor = Interceptor { chain ->
            //实现maxAge时间内使用缓存数据（资讯详情），超过maxAge时间后，若有网，则从网络上获取最新数据然后缓存，若没有网，则继续使用缓存。
            //获得请求
            var request = chain.request()
            //交给责任链下一环执行，最后回传一个响应
            val response = chain.proceed(request)
            //获取不同接口自定义的缓存时间（天），即addHeader("cache",XXX)中的数据。
            //因为不同接口需要的缓存策略可能会不一样，及时更新的数据不需要缓存，基本不变的数据才需要缓存。
            var cache = request.header("cache")
            // 有网络时，设置缓存超时时间，表示希望在多少秒内不去重新获取网络数据，而是直接使用缓存数据
            val maxAge =
                if (cache.isNullOrEmpty()) DEFAULT_CACHE_TIME_OUT else TimeUnit.SECONDS.convert(
                    cache.toLong(),
                    TimeUnit.DAYS
                )
            response.newBuilder()
                /**
                 * Cache-Control，缓存控制
                 * public，所有网页信息都缓存
                 * max-age，缓存有效期限，在这个期限内，不去再去进行网络访问。在超出max-age的情况下向服务端发起新的请求，请求失败的情况下返回缓存数据，否则向服务端重新发起请求。
                 * max-stale，能容忍的最大过期时间。max-stale指令标示了客户端愿意接收一个已经过期了的响应。如果指定了max-stale的值，则最大容忍时间为对应的秒数。
                 * max-stale在请求设置中有效，在响应设置中无效
                 * only-if-cached，只接受缓存的内容
                 * removeHeader去除相关Cache-Control（缓存控制）的HTTP头信息，一般都是固定的格式
                 * removeHeader因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                 */
                .removeHeader("Pragma")//移除影响，保证缓存的有效性，用于向后兼容HTTP/1.1缓存，而Cache-Control HTTP/1.1缓存不存在。
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$maxAge")//max-age表示缓存的内容将在maxAge秒后失效
                .addHeader("Cache-Control", CacheControl.FORCE_CACHE.toString())//每次请求网络数据时强制要求缓存
                .build()
        }

        //打印请求日志
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Logger.e(message)
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)

        //设置缓存文件
        //设置后会在文件夹下生成3个文件，以.0结尾的文件缓存了http的响应头信息，以.1结尾的文件则缓存了下载的json数据，journal则是一个日志文件。
        val cacheFile = File(BaseApplication.instance.cacheDir, "responses")
        val cache = Cache(cacheFile, DEFAULT_CACHE_SIZE) //当缓存区的数据大小超过默认值的时候会依据LRU算法自动删除已缓存的数据

        //从asserts中获取证书的流
//        var sslParams: TLSSocketFactory.SSLParams? = null;
//        try {
//            sslParams =
//                TLSSocketFactory.getSslSocketFactory(BaseApplication.instance.assets.open("DigiCert.cer"))
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }

        val client = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS) //连接超时时间
            .writeTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS) //读写超时
            .readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) //失败重连
//            .authenticator { route, response ->
//                val credential = Credentials.basic("api_key", "api_secret")
//                response.request().newBuilder()
//                    .header("Authorization", credential)
//                    .build()
//            }  //授权认证
            /**
             * addNetworkInterceptor添加的是网络拦截器，他会在在request和resposne时分别被调用一次，能够操作中间过程的响应,如重定向和重试；
             * Network Interceptor多用于对请求体Request或者响应体Response的改变，缓存处理用这个
             * 而addInterceptor添加的是application拦截器，他只会在response被调用一次，且总是只调用一次，不需要担心中间过程的响应。
             * Application Interceptor主要多用于查看请求信息或者返回信息，如链接地址，头信息，参数等
             */
            .addInterceptor(headerInterceptor)
            .addInterceptor(parameterInterceptor)
            .addNetworkInterceptor(loggingInterceptor)
            .addInterceptor(offlineCacheInterceptor)//在用户端添加请求拦截器
            .addNetworkInterceptor(netCacheInterceptor)//在网络端添加响应拦截器（注意和用户端的区别）
            /**
             * 证书分两种：
             * 1、花钱向认证机构购买的证书，服务器如果使用了此类证书的话，那对于移动端来说，直接可以忽略此证书，直接用https访问。与之不同的是ios内置了很多信任的证书，所以他们不需要做任何操作
             * 2、另一种是自己制作的证书，使用此类证书的话是不受信任的，也不需要花钱，所以需要我们在代码中将此类证书设置为信任证书
             * 服务器的哥们如果加上了证书的话，那么你们的网络请求的url将从http:xx改成https:xx,如果你直接也将http改成https的话而什么也不做的话，客户端将直接报错
             * java.security.cert.CertPathValidatorException:Trust anchor for certification path not found
             * 报错意思就是没有找到本地证书，那就开始构建一个SSL来信任所有的证书。
             */
//            .sslSocketFactory(sslParams?.sSLSocketFactory!!,sslParams.trustManager!!)//管理证书和信任证书
//            .hostnameVerifier(TLSSocketFactory.UnSafeHostnameVerifier)
            .cache(cache)
            .build()
        retrofit = Retrofit.Builder()
            .client(client)
            /**
             * Android P系统[9.0(SDK28)]以后限制了明文流量的网络请求，http请求会报错
             * CLEARTEXT communication to {hostname} not permitted by network security policy
             * 解决办法(或关系)：
             * 1.服务器和本地应用都改用 https
             * 2.targetSdkVersion 降级回到 27
             * 3.res->xml->network_security_config.xml
             * <network-security-config><base-config cleartextTrafficPermitted="true" /></network-security-config>
             * AndroidManifest.xml->application
             * android:networkSecurityConfig="@xml/network_security_config"
             */
            .baseUrl(BuildConfig.HostName)
            /**
             * 添加 RxJava 适配器
             */
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            /**
             * 将服务器的数据做统一的处理
             * 返回String类型需引入ScalarsConverterFactory.create()
             * 返回Gson类型需引入GsonConverterFactory.create()
             */
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit!!
    }

    /**
     * 判断网络
     */
    private fun isNetworkAvailable(): Boolean {
        val context: Context = BaseApplication.instance
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager == null) {
            return false
        } else {
            val networkInfo = connectivityManager.allNetworkInfo
            if (networkInfo != null && networkInfo.isNotEmpty()) {
                for (i in networkInfo.indices) {
                    if (networkInfo[i].state == NetworkInfo.State.CONNECTED) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
