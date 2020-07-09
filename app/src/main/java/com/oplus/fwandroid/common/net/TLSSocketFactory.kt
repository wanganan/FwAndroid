package com.oplus.fwandroid.common.net

import com.orhanobut.logger.Logger
import java.io.IOException
import java.io.InputStream
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author Sinaan
 * @date 2020/7/8
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：摘自jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 12306.cn的用户登录是需要HTTPS的访问的：https://kyfw.12306.cn/otn/regist/init，可用来测试
 * HTTPS的传输过程涉及到了对称加密和非对称加密，对称加密加密的是实际的数据，非对称加密加密的是对称加密所需要的客户端的密钥。
 * 为了确保客户端能够确认公钥就是想要访问的网站的公钥，引入了数字证书的概念，由于证书存在一级一级的签发过程，所以就出现了证书链，在证书链中的顶端的就是根CA。
 * Android客户端不信任服务器证书的原因主要是因为客户端不信任证书链中的根证书CA，我们应该让我们的App去信任该根证书CA，而不是直接信任网站的自身的数字证书，因为网站的数字证书可能会发生变化。
 * DigiCert.cer为12306的根证书(以前是SRCA),GlobalSign.cer是百度的根证书。证书导出方法见https://blog.csdn.net/iispring/article/details/51615631
 * version: 1.0
 */
object TLSSocketFactory {
    private const val CLIENT_TRUST_PASSWORD = "*******" //信任证书密码
    private const val CLIENT_TRUST_MANAGER = "X509"
    private const val CLIENT_AGREEMENT = "TLS" //使用协议
    private const val CLIENT_TRUST_KEYSTORE = "BKS"

    class SSLParams {
        var sSLSocketFactory: SSLSocketFactory? = null
        var trustManager: X509TrustManager? = null
    }

    fun getSslSocketFactory(): SSLParams? {
        return getSslSocketFactoryBase(null, null, null)
    }

    /**
     *
     * https单向认证
     *
     * 可以额外配置信任服务端的证书策略，否则默认是按CA证书去验证的，若不是CA可信任的证书，则无法通过验证
     *
     */
    fun getSslSocketFactory(trustManager: X509TrustManager?): SSLParams? {
        return getSslSocketFactoryBase(trustManager, null, null)
    }

    /**
     *
     * https单向认证
     *
     * 用含有服务端公钥的证书校验服务端证书
     *
     */
    fun getSslSocketFactory(vararg certificates: InputStream): SSLParams? {
        return getSslSocketFactoryBase(null, null, null, *certificates)
    }

    /**
     *
     * https双向认证
     *
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     *
     * certificates -> 用含有服务端公钥的证书校验服务端证书
     *
     */
    fun getSslSocketFactory(
        bksFile: InputStream?,
        password: String?,
        vararg certificates: InputStream
    ): SSLParams? {
        return getSslSocketFactoryBase(null, bksFile, password, *certificates)
    }

    /**
     *
     * https双向认证
     *
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     *
     * X509TrustManager -> 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
     *
     */
    fun getSslSocketFactory(
        bksFile: InputStream?,
        password: String?,
        trustManager: X509TrustManager?
    ): SSLParams? {
        return getSslSocketFactoryBase(trustManager, bksFile, password)
    }

    private fun getSslSocketFactoryBase(
        trustManager: X509TrustManager?,
        bksFile: InputStream?,
        password: String?,
        vararg certificates: InputStream
    ): SSLParams? {
        val sslParams = SSLParams()
        return try {
            val keyManagers =
                prepareKeyManager(bksFile, password)
            val trustManagers =
                prepareTrustManager(*certificates)
            val manager: X509TrustManager
            manager = //优先使用用户自定义的TrustManager
                trustManager
                    ?: (trustManagers?.let {

                        //然后使用默认的TrustManager
                        chooseTrustManager(it)
                    } ?: //否则使用不安全的TrustManager
                    UnSafeTrustManager)

            // 创建TLS类型的SSLContext对象， that uses our TrustManager
            val sslContext = SSLContext.getInstance(CLIENT_AGREEMENT)

            // 用上面得到的trustManagers初始化SSLContext，这样sslContext就会信任keyStore中的证书
            // 第一个参数是授权的密钥管理器，用来授权验证，比如授权自签名的证书验证。第二个是被授权的证书管理器，用来验证服务器端的证书
            sslContext.init(keyManagers, arrayOf<TrustManager?>(manager), null)

            // 通过sslContext获取SSLSocketFactory对象
            sslParams.sSLSocketFactory = sslContext.socketFactory
            sslParams.trustManager = manager
            sslParams
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: KeyManagementException) {
            throw AssertionError(e)
        }
    }

    private fun prepareKeyManager(
        bksFile: InputStream?,
        password: String?
    ): Array<KeyManager>? {
        try {
            if (bksFile == null || password == null) return null
            val clientKeyStore = KeyStore.getInstance(CLIENT_TRUST_KEYSTORE)
            clientKeyStore.load(bksFile, password.toCharArray())
            val kmf =
                KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            kmf.init(clientKeyStore, password.toCharArray())
            return kmf.keyManagers
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun prepareTrustManager(vararg certificates: InputStream): Array<TrustManager>? {
        if (certificates == null || certificates.isEmpty()) return null
        try {
            //创建X.509格式的CertificateFactory
            val certificateFactory =
                CertificateFactory.getInstance("X.509")

            // 创建一个默认类型的KeyStore，存储我们信任的证书
            val keyStore =
                KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null)
            for ((index, certStream) in certificates.withIndex()) {
                val certificateAlias = index.toString()

                //cert是java.security.cert.Certificate，不是java.security.Certificate，
                //也不是javax.security.cert.Certificate
                // 证书工厂根据证书文件的流生成证书 cert
                val cert =
                    certificateFactory.generateCertificate(certStream)
                Logger.e("cert:" + (cert as X509Certificate).subjectDN)

                // 将证书 cert 作为可信证书放入到keyStore中
                keyStore.setCertificateEntry(certificateAlias, cert)
                try {
                    certStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            //TrustManagerFactory是用于生成TrustManager的，我们创建一个默认类型的TrustManagerFactory
            val tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())

            //用我们之前的keyStore实例初始化TrustManagerFactory，这样tmf就会信任keyStore中的证书
            tmf.init(keyStore)

            //通过tmf获取TrustManager数组，TrustManager也会信任keyStore中的证书
            return tmf.trustManagers
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun chooseTrustManager(trustManagers: Array<TrustManager>): X509TrustManager? {
        for (trustManager in trustManagers) {
            if (trustManager is X509TrustManager) {
                return trustManager
            }
        }
        return null
    }

    /**
     *
     * 为了解决客户端不信任服务器数字证书的问题，网络上大部分的解决方案都是让客户端不对证书做任何检查，
     *
     * 这是一种有很大安全漏洞的办法
     *
     */
    private var UnSafeTrustManager: X509TrustManager =
        object : X509TrustManager {
            //该方法检查客户端的证书，若不信任该证书则抛出异常。由于我们不需要对客户端进行认证，
            //因此我们只需要执行默认的信任管理器的这个方法。JSSE中，默认的信任管理器类为TrustManager。
            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            //该方法检查服务器的证书，若不信任该证书同样抛出异常。通过自己实现该方法，可以使之信任我们指定的任何证书。
            //在实现该方法时，也可以简单的不做任何处理，即一个空的函数体，由于不会抛出异常，它就会信任任何证书。
            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate>,
                authType: String
            ) {
            }

            //返回受信任的X509证书数组。
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

    /**
     *
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     *
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     *
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     *
     */
    var UnSafeHostnameVerifier =
        HostnameVerifier { hostname, session -> true }
}