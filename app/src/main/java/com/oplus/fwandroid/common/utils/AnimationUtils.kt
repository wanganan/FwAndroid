package com.oplus.fwandroid.common.utils

import android.view.animation.*
import android.view.animation.Animation.AnimationListener

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：动画工具类
 * version: 1.0
 */
object AnimationUtils {

    /**
     * 默认动画持续时间
     */
    const val DEFAULT_ANIMATION_DURATION: Long = 400


    /**
     * 获取一个旋转动画
     *
     * @param fromDegrees       开始角度
     * @param toDegrees         结束角度
     * @param pivotXType        旋转中心点X轴坐标相对类型
     * @param pivotXValue       旋转中心点X轴坐标
     * @param pivotYType        旋转中心点Y轴坐标相对类型
     * @param pivotYValue       旋转中心点Y轴坐标
     * @param durationMillis    持续时间
     * @param animationListener 动画监听器
     * @return 一个旋转动画
     */
    fun getRotateAnimation(
        fromDegrees: Float,
        toDegrees: Float,
        pivotXType: Int,
        pivotXValue: Float,
        pivotYType: Int,
        pivotYValue: Float,
        durationMillis: Long,
        animationListener: AnimationListener?
    ): RotateAnimation? {
        val rotateAnimation = RotateAnimation(
            fromDegrees,
            toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue
        )
        rotateAnimation.duration = durationMillis
        if (animationListener != null) {
            rotateAnimation.setAnimationListener(animationListener)
        }
        return rotateAnimation
    }


    /**
     * 获取一个根据视图自身中心点旋转的动画
     *
     * @param durationMillis    动画持续时间
     * @param animationListener 动画监听器
     * @return 一个根据中心点旋转的动画
     */
    fun getRotateAnimationByCenter(
        durationMillis: Long,
        animationListener: AnimationListener?
    ): RotateAnimation? {
        return getRotateAnimation(
            0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f, durationMillis,
            animationListener
        )
    }


    /**
     * 获取一个根据中心点旋转的动画
     *
     * @param duration 动画持续时间
     * @return 一个根据中心点旋转的动画
     */
    fun getRotateAnimationByCenter(duration: Long): RotateAnimation? {
        return getRotateAnimationByCenter(duration, null)
    }


    /**
     * 获取一个根据视图自身中心点旋转的动画
     *
     * @param animationListener 动画监听器
     * @return 一个根据中心点旋转的动画
     */
    fun getRotateAnimationByCenter(animationListener: AnimationListener?): RotateAnimation? {
        return getRotateAnimationByCenter(
            DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }


    /**
     * 获取一个根据中心点旋转的动画
     *
     * @return 一个根据中心点旋转的动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getRotateAnimationByCenter(): RotateAnimation? {
        return getRotateAnimationByCenter(DEFAULT_ANIMATION_DURATION, null)
    }


    /**
     * 获取一个透明度渐变动画
     *
     * @param fromAlpha         开始时的透明度
     * @param toAlpha           结束时的透明度都
     * @param durationMillis    持续时间
     * @param animationListener 动画监听器
     * @return 一个透明度渐变动画
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        durationMillis: Long,
        animationListener: AnimationListener?
    ): AlphaAnimation? {
        val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
        alphaAnimation.duration = durationMillis
        if (animationListener != null) {
            alphaAnimation.setAnimationListener(animationListener)
        }
        return alphaAnimation
    }


    /**
     * 获取一个透明度渐变动画
     *
     * @param fromAlpha      开始时的透明度
     * @param toAlpha        结束时的透明度都
     * @param durationMillis 持续时间
     * @return 一个透明度渐变动画
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        durationMillis: Long
    ): AlphaAnimation? {
        return getAlphaAnimation(fromAlpha, toAlpha, durationMillis, null)
    }


    /**
     * 获取一个透明度渐变动画
     *
     * @param fromAlpha         开始时的透明度
     * @param toAlpha           结束时的透明度都
     * @param animationListener 动画监听器
     * @return 一个透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getAlphaAnimation(
        fromAlpha: Float,
        toAlpha: Float,
        animationListener: AnimationListener?
    ): AlphaAnimation? {
        return getAlphaAnimation(
            fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }


    /**
     * 获取一个透明度渐变动画
     *
     * @param fromAlpha 开始时的透明度
     * @param toAlpha   结束时的透明度都
     * @return 一个透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getAlphaAnimation(fromAlpha: Float, toAlpha: Float): AlphaAnimation? {
        return getAlphaAnimation(
            fromAlpha, toAlpha, DEFAULT_ANIMATION_DURATION,
            null
        )
    }


    /**
     * 获取一个由完全显示变为不可见的透明度渐变动画
     *
     * @param durationMillis    持续时间
     * @param animationListener 动画监听器
     * @return 一个由完全显示变为不可见的透明度渐变动画
     */
    fun getHiddenAlphaAnimation(
        durationMillis: Long,
        animationListener: AnimationListener?
    ): AlphaAnimation? {
        return getAlphaAnimation(1.0f, 0.0f, durationMillis, animationListener)
    }


    /**
     * 获取一个由完全显示变为不可见的透明度渐变动画
     *
     * @param durationMillis 持续时间
     * @return 一个由完全显示变为不可见的透明度渐变动画
     */
    fun getHiddenAlphaAnimation(durationMillis: Long): AlphaAnimation? {
        return getHiddenAlphaAnimation(durationMillis, null)
    }


    /**
     * 获取一个由完全显示变为不可见的透明度渐变动画
     *
     * @param animationListener 动画监听器
     * @return 一个由完全显示变为不可见的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getHiddenAlphaAnimation(animationListener: AnimationListener?): AlphaAnimation? {
        return getHiddenAlphaAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }


    /**
     * 获取一个由完全显示变为不可见的透明度渐变动画
     *
     * @return 一个由完全显示变为不可见的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getHiddenAlphaAnimation(): AlphaAnimation? {
        return getHiddenAlphaAnimation(DEFAULT_ANIMATION_DURATION, null)
    }


    /**
     * 获取一个由不可见变为完全显示的透明度渐变动画
     *
     * @param durationMillis    持续时间
     * @param animationListener 动画监听器
     * @return 一个由不可见变为完全显示的透明度渐变动画
     */
    fun getShowAlphaAnimation(
        durationMillis: Long,
        animationListener: AnimationListener?
    ): AlphaAnimation? {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, animationListener)
    }


    /**
     * 获取一个由不可见变为完全显示的透明度渐变动画
     *
     * @param durationMillis 持续时间
     * @return 一个由不可见变为完全显示的透明度渐变动画
     */
    fun getShowAlphaAnimation(durationMillis: Long): AlphaAnimation? {
        return getAlphaAnimation(0.0f, 1.0f, durationMillis, null)
    }


    /**
     * 获取一个由不可见变为完全显示的透明度渐变动画
     *
     * @param animationListener 动画监听器
     * @return 一个由不可见变为完全显示的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getShowAlphaAnimation(animationListener: AnimationListener?): AlphaAnimation? {
        return getAlphaAnimation(
            0.0f, 1.0f, DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }


    /**
     * 获取一个由不可见变为完全显示的透明度渐变动画
     *
     * @return 一个由不可见变为完全显示的透明度渐变动画，默认持续时间为DEFAULT_ANIMATION_DURATION
     */
    fun getShowAlphaAnimation(): AlphaAnimation? {
        return getAlphaAnimation(0.0f, 1.0f, DEFAULT_ANIMATION_DURATION, null)
    }

    /**
     * 获取一个缩小动画
     *
     * @param durationMillis   时间
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    fun getLessenScaleAnimation(
        fromX: Float,
        toX: Float,
        fromY: Float,
        toY: Float,
        durationMillis: Long,
        animationListener: AnimationListener?
    ): ScaleAnimation? {
        val scaleAnimation = ScaleAnimation(
            fromX, toX, fromY,
            toY, ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
            ScaleAnimation.RELATIVE_TO_SELF.toFloat()
        )
        scaleAnimation.duration = durationMillis
        scaleAnimation.setAnimationListener(animationListener)
        return scaleAnimation
    }

    /**
     * 获取一个缩小动画
     *
     * @param durationMillis   时间
     * @return 一个缩小动画
     */
    fun getLessenScaleAnimation(
        fromX: Float,
        toX: Float,
        fromY: Float,
        toY: Float,
        durationMillis: Long
    ): ScaleAnimation? {
        return getLessenScaleAnimation(
            fromX, toX, fromY,
            toY, durationMillis, null
        )
    }

    /**
     * 获取一个缩小动画
     *
     * @return 一个缩小动画
     */
    fun getLessenScaleAnimation(
        fromX: Float,
        toX: Float,
        fromY: Float,
        toY: Float
    ): ScaleAnimation? {
        return getLessenScaleAnimation(
            fromX, toX, fromY,
            toY, 1000, null
        )
    }

    /**
     * 获取一个缩小动画
     *
     * @param durationMillis   时间
     * @param animationListener  监听
     * @return 一个缩小动画
     */
    fun getLessenScaleAnimation(
        durationMillis: Long,
        animationListener: AnimationListener?
    ): ScaleAnimation? {
        return getLessenScaleAnimation(
            1.0f, 0.0f, 1.0f,
            0.0f, durationMillis, animationListener
        )
    }


    /**
     * 获取一个缩小动画
     *
     * @param durationMillis 时间
     * @return 一个缩小动画
     */
    fun getLessenScaleAnimation(durationMillis: Long): ScaleAnimation? {
        return getLessenScaleAnimation(durationMillis, null)
    }


    /**
     * 获取一个缩小动画
     *
     * @param animationListener  监听
     * @return 返回一个缩小的动画
     */
    fun getLessenScaleAnimation(animationListener: AnimationListener?): ScaleAnimation? {
        return getLessenScaleAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }


    /**
     * 获取一个放大动画
     * @param durationMillis   时间
     * @param animationListener  监听
     *
     * @return 返回一个放大的效果
     */
    fun getAmplificationAnimation(
        durationMillis: Long,
        animationListener: AnimationListener?
    ): ScaleAnimation? {
        val scaleAnimation = ScaleAnimation(
            0.0f, 1.0f, 0.0f,
            1.0f, ScaleAnimation.RELATIVE_TO_SELF.toFloat(),
            ScaleAnimation.RELATIVE_TO_SELF.toFloat()
        )
        scaleAnimation.duration = durationMillis
        scaleAnimation.setAnimationListener(animationListener)
        return scaleAnimation
    }


    /**
     * 获取一个放大动画
     *
     * @param durationMillis   时间
     *
     * @return 返回一个放大的效果
     */
    fun getAmplificationAnimation(durationMillis: Long): ScaleAnimation? {
        return getAmplificationAnimation(durationMillis, null)
    }


    /**
     * 获取一个放大动画
     *
     * @param animationListener  监听
     * @return 返回一个放大的效果
     */
    fun getAmplificationAnimation(animationListener: AnimationListener?): ScaleAnimation? {
        return getAmplificationAnimation(
            DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }

    /**
     * 获取一个移动动画
     * @param fromXDelta 起始位置x绝对坐标
     * @param toXDelta 终点位置x绝对坐标
     * @param fromYDelta 起始位置y绝对坐标
     * @param toYDelta 终点位置y绝对坐标
     * @param durationMillis 持续时间
     * @param animationListener 动画监听器
     * @return
     */
    fun getTranslateAnimation(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        durationMillis: Long,
        animationListener: AnimationListener?
    ): TranslateAnimation? {
        val translateAnimation =
            TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
        translateAnimation.duration = durationMillis
        if (animationListener != null) {
            translateAnimation.setAnimationListener(animationListener)
        }
        return translateAnimation
    }

    /**
     * 获取一个移动动画
     * @param fromXDelta 起始位置x绝对坐标
     * @param toXDelta 终点位置x绝对坐标
     * @param fromYDelta 起始位置y绝对坐标
     * @param toYDelta 终点位置y绝对坐标
     * @param animationListener 动画监听器
     * @return
     */
    fun getTranslateAnimation(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        animationListener: AnimationListener?
    ): TranslateAnimation? {
        return getTranslateAnimation(
            fromXDelta,
            toXDelta,
            fromYDelta,
            toYDelta,
            DEFAULT_ANIMATION_DURATION,
            animationListener
        )
    }

    /**
     * 获取一个移动动画
     * @param fromXDelta 起始位置x绝对坐标
     * @param toXDelta 终点位置x绝对坐标
     * @param fromYDelta 起始位置y绝对坐标
     * @param toYDelta 终点位置y绝对坐标
     * @param durationMillis 持续时间
     * @return
     */
    fun getTranslateAnimation(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        durationMillis: Long
    ): TranslateAnimation? {
        return getTranslateAnimation(
            fromXDelta,
            toXDelta,
            fromYDelta,
            toYDelta,
            durationMillis,
            null
        )
    }

    /**
     * 获取一个移动动画
     * @param fromXDelta 起始位置x绝对坐标
     * @param toXDelta 终点位置x绝对坐标
     * @param fromYDelta 起始位置y绝对坐标
     * @param toYDelta 终点位置y绝对坐标
     * @return
     */
    fun getTranslateAnimation(
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float
    ): TranslateAnimation? {
        return getTranslateAnimation(
            fromXDelta,
            toXDelta,
            fromYDelta,
            toYDelta,
            DEFAULT_ANIMATION_DURATION
        )
    }
}