package com.oplus.fwandroid.common.utils

import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：随机数生成工具
 * version: 1.0
 */
object RandomUtils {
    const val NUMBERS_AND_LETTERS =
        "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val NUMBERS = "0123456789"
    const val LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val CAPITAL_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    const val LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz"

    /**
     * get a fixed-length random string, its a mixture of uppercase, lowercase letters and numbers
     *
     * @param length  length
     * @return  RandomUtils
     */
    fun getRandomNumbersAndLetters(length: Int): String? {
        return getRandom(NUMBERS_AND_LETTERS, length)
    }

    /**
     * get a fixed-length random string, its a mixture of numbers
     *
     * @param length  length
     * @return  RandomUtils
     */
    fun getRandomNumbers(length: Int): String? {
        return getRandom(NUMBERS, length)
    }

    /**
     * get a fixed-length random string, its a mixture of uppercase and lowercase letters
     *
     * @param length  length
     * @return  RandomUtils
     */
    fun getRandomLetters(length: Int): String? {
        return getRandom(LETTERS, length)
    }

    /**
     * get a fixed-length random string, its a mixture of uppercase letters
     *
     * @param length  length
     * @return   CapitalLetters
     */
    fun getRandomCapitalLetters(length: Int): String? {
        return getRandom(CAPITAL_LETTERS, length)
    }

    /**
     * get a fixed-length random string, its a mixture of lowercase letters
     *
     * @param length  length
     * @return  get a fixed-length random string, its a mixture of lowercase letters
     */
    fun getRandomLowerCaseLetters(length: Int): String? {
        return getRandom(LOWER_CASE_LETTERS, length)
    }

    /**
     * get a fixed-length random string, its a mixture of chars in source
     *
     * @param source  source
     * @param length  length
     * @return  get a fixed-length random string, its a mixture of chars in source
     */
    fun getRandom(source: String, length: Int): String? {
        return if (StringUtils.isEmpty(source)) null else getRandom(
            source.toCharArray(),
            length
        )
    }

    /**
     * get a fixed-length random string, its a mixture of chars in sourceChar
     *
     * @param sourceChar    sourceChar
     * @param length  length
     * @return   get a fixed-length random string, its a mixture of chars in sourceChar
     */
    fun getRandom(sourceChar: CharArray?, length: Int): String? {
        if (sourceChar == null || sourceChar.size == 0 || length < 0) {
            return null
        }
        val str = StringBuilder(length)
        val random = Random()
        for (i in 0 until length) {
            str.append(sourceChar[random.nextInt(sourceChar.size)])
        }
        return str.toString()
    }


    /**
     *
     * @param max  接受的数值
     * @return  返回一个随机的数值
     */
    fun getRandom(max: Int): Int {
        return getRandom(0, max)
    }


    /**
     *
     * @param min  最小
     * @param max  最大
     * @return  返回一个范围的数值
     */
    fun getRandom(min: Int, max: Int): Int {
        if (min > max) {
            return 0
        }
        return if (min == max) {
            min
        } else min + Random().nextInt(max - min)
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified array using a default source of randomness
     *
     * @param objArray  数组
     * @return 从新的数组
     */
    fun shuffle(objArray: Array<Any?>?): Boolean {
        return if (objArray == null) {
            false
        } else shuffle(objArray, getRandom(objArray.size))
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified array
     *
     * @param objArray  数组
     * @param shuffleCount  洗的个数
     * @return  是否成功
     */
    fun shuffle(objArray: Array<Any?>?, shuffleCount: Int): Boolean {
        var length: Int = 0
        if (objArray == null || shuffleCount < 0 || objArray.size.also {
                length = it
            } < shuffleCount) {
            return false
        }
        for (i in 1..shuffleCount) {
            val random = getRandom(length - i)
            val temp = objArray[length - i]
            objArray[length - i] = objArray[random]
            objArray[random] = temp
        }
        return true
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified int array using a default source of randomness
     *
     * @param intArray  数组
     * @return  洗牌之后
     */
    fun shuffle(intArray: IntArray?): IntArray? {
        return if (intArray == null) {
            null
        } else shuffle(intArray, getRandom(intArray.size))
    }

    /**
     * Shuffling algorithm, Randomly permutes the specified int array
     *
     * @param intArray   数组
     * @param shuffleCount  范围
     * @return  新的数组
     */
    fun shuffle(intArray: IntArray?, shuffleCount: Int): IntArray? {
        var length: Int = 0
        if (intArray == null || shuffleCount < 0 || intArray.size.also {
                length = it
            } < shuffleCount) {
            return null
        }
        val out = IntArray(shuffleCount)
        for (i in 1..shuffleCount) {
            val random = getRandom(length - i)
            out[i - 1] = intArray[random]
            val temp = intArray[length - i]
            intArray[length - i] = intArray[random]
            intArray[random] = temp
        }
        return out
    }
}