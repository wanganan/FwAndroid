package com.oplus.fwandroid.common.utils

import java.util.regex.Pattern

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：正则表达式工具类，提供一些常用的正则表达式
 * version: 1.0
 */
object RegexUtils {
    /**
     * 匹配全网IP的正则表达式
     */
    const val IP_REGEX =
        "^((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))$"

    /**
     * 匹配手机号码的正则表达式
     */
    const val PHONE_NUMBER_REGEX = "1[345789]\\d{9}"

    /**
     * 匹配只能输入数字和字母的正则表达式
     */
    const val PWD_REGEX = "^[A-Za-z0-9]+$"

    /**
     * 匹配只能输入数字和字母组合的正则表达式
     */
    const val PWD_COMBINE_REGEX = ".*[a-zA-Z].*[0-9]|.*[0-9].*[a-zA-Z]"

    /**
     * 匹配邮箱的正则表达式
     * <br></br>"www."可省略不写
     */
    const val EMAIL_REGEX = "^(www\\.)?\\w+@\\w+(\\.\\w+)+$"

    /**
     * 匹配汉子的正则表达式，个数限制为一个或多个
     */
    const val CHINESE_REGEX = "^[\u4e00-\u9f5a]+$"

    /**
     * 匹配正整数的正则表达式，个数限制为一个或多个
     */
    const val POSITIVE_INTEGER_REGEX = "^\\d+$"

    /**
     * 匹配身份证号的正则表达式
     */
    const val ID_CARD =
        "^(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)$"

    /**
     * 匹配邮编的正则表达式
     */
    const val ZIP_CODE = "^\\d{6}$"

    /**
     * 匹配URL的正则表达式
     */
    const val URL =
        "^(([hH][tT]{2}[pP][sS]?)|([fF][tT][pP]))\\:\\/\\/[wW]{3}\\.[\\w-]+\\.\\w{2,4}(\\/.*)?$"

    /**
     * 匹配金额的正则表达式(判断小数点后2位的数字的正则表达式)
     */
    const val AMOUNT_REGEX = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"

    /**
     * 匹配给定的字符串是否是一个邮箱账号，"www."可省略不写
     * @param string 给定的字符串
     * @return true：是
     */
    fun isEmail(string: String): Boolean {
        return string.matches(EMAIL_REGEX)
    }

    /**
     * 匹配给定的字符串是否是一个手机号码，支持130——139、150——153、155——159、180、183、185、186、188、189号段
     * @param string 给定的字符串
     * @return true：是
     */
    fun isMobilePhoneNumber(string: String): Boolean {
        return string.matches(PHONE_NUMBER_REGEX)
    }

    /**
     * 验证密码是否是字母和数字的特殊字符
     * @param pro 密码
     * @return true: 是
     */
    fun isPasWord(pro: String): Boolean {
        return pro.matches(PWD_REGEX)
    }

    /**
     * 验证密码是否是字母和数字组合的特殊字符
     * @param pro 密码
     * @return true: 是
     */
    fun isCombinePasWord(pro: String): Boolean {
        return pro.matches(PWD_COMBINE_REGEX)
    }

    /**
     * 匹配给定的字符串是否是一个全网IP
     * @param string 给定的字符串
     * @return true：是
     */
    fun isIp(string: String): Boolean {
        return string.matches(IP_REGEX)
    }

    /**
     * 匹配给定的字符串是否全部由汉子组成
     * @param string 给定的字符串
     * @return true：是
     */
    fun isChinese(string: String): Boolean {
        return string.matches(CHINESE_REGEX)
    }

    /**
     * 验证给定的字符串是否全部由正整数组成
     * @param string 给定的字符串
     * @return true：是
     */
    fun isPositiveInteger(string: String): Boolean {
        return string.matches(POSITIVE_INTEGER_REGEX)
    }

    /**
     * 验证给定的字符串是否是身份证号
     * <br></br>
     * <br></br>身份证15位编码规则：dddddd yymmdd xx p
     * <br></br>dddddd：6位地区编码
     * <br></br>yymmdd：出生年(两位年)月日，如：910215
     * <br></br>xx：顺序编码，系统产生，无法确定
     * <br></br>p：性别，奇数为男，偶数为女
     * <br></br>
     * <br></br>
     * <br></br>身份证18位编码规则：dddddd yyyymmdd xxx y
     * <br></br>dddddd：6位地区编码
     * <br></br>yyyymmdd：出生年(四位年)月日，如：19910215
     * <br></br>xxx：顺序编码，系统产生，无法确定，奇数为男，偶数为女
     * <br></br>y：校验码，该位数值可通过前17位计算获得
     * <br></br>前17位号码加权因子为 Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 ]
     * <br></br>验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]
     * <br></br>如果验证码恰好是10，为了保证身份证是十八位，那么第十八位将用X来代替 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )
     * <br></br>i为身份证号码1...17 位; Y_P为校验码Y所在校验码数组位置
     * @param string
     * @return
     */
    fun isIdCard(string: String): Boolean {
        return string.matches(ID_CARD)
    }

    /**
     * 验证给定的字符串是否是邮编
     * @param string
     * @return
     */
    fun isZipCode(string: String): Boolean {
        return string.matches(ZIP_CODE)
    }

    /**
     * 验证给定的字符串是否是URL，仅支持http、https、ftp
     * @param string
     * @return
     */
    fun isURL(string: String): Boolean {
        return string.matches(URL)
    }

    fun isNumber(str: String): Boolean { //判断整型
        return str.matches("[\\d]+")
    }

    fun isDecimalsNumber(str: String): Boolean { //判断小数，与判断整型的区别在与d后面的小数点（红色）
        return str.matches("[\\d.]+")
    }

    /**
     * 是否是金额类型
     * @param str
     * @return
     */
    fun isAmount(str: String): Boolean { //判断整型
        return str.matches(AMOUNT_REGEX)
    }

    /**
     * 验证密码只能输入字母和数字的特殊字符,这个返回的是过滤之后的字符串
     */
    fun checkPasWord(pro: String?): String? {
        try {
            // 只允许字母、数字和汉字
            val regEx = "[^a-zA-Z0-9\u4E00-\u9FA5]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(pro)
            return m.replaceAll("").trim { it <= ' ' }
        } catch (e: Exception) {
        }
        return ""
    }

    /**
     * 只能输入字母和汉字 这个返回的是过滤之后的字符串
     */
    fun checkInputPro(pro: String?): String? {
        try {
            val regEx = "[^a-zA-Z\u4E00-\u9FA5]"
            val p = Pattern.compile(regEx)
            val m = p.matcher(pro)
            return m.replaceAll("").trim { it <= ' ' }
        } catch (e: Exception) {
        }
        return ""
    }

    private fun String.matches(regex: String): Boolean {
        return this.matches(Regex(regex))
    }

}
