package com.oplus.fwandroid.common.utils

import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import com.oplus.fwandroid.common.utils.AnimationUtils.getHiddenAlphaAnimation
import com.oplus.fwandroid.common.utils.AnimationUtils.getShowAlphaAnimation

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：视图动画工具箱，提供简单的控制视图的动画的工具方法
 * version: 1.0
 */
object ViewAnimationUtils {

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)）
     *
     * @param view              被处理的视图
     * @param isBanClick        在执行动画的过程中是否禁止点击
     * @param durationMillis    持续时间，毫秒
     * @param animationListener 动画监听器
     */
    private fun invisibleViewByAlpha(
        view: View,
        durationMillis: Long, isBanClick: Boolean,
        animationListener: AnimationListener?
    ) {
        if (view.visibility != View.INVISIBLE) {
            view.visibility = View.INVISIBLE
            val hiddenAlphaAnimation =
                getHiddenAlphaAnimation(durationMillis)
            hiddenAlphaAnimation!!.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = false
                    }
                    animationListener?.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    animationListener?.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = true
                    }
                    animationListener?.onAnimationEnd(animation)
                }
            })
            view.startAnimation(hiddenAlphaAnimation)
        }
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)）
     *
     * @param view              被处理的视图
     * @param durationMillis    持续时间，毫秒
     * @param animationListener 动画监听器
     */
    fun invisibleViewByAlpha(
        view: View,
        durationMillis: Long, animationListener: AnimationListener?
    ) {
        invisibleViewByAlpha(view, durationMillis, false, animationListener)
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun invisibleViewByAlpha(
        view: View,
        durationMillis: Long, isBanClick: Boolean
    ) {
        invisibleViewByAlpha(view, durationMillis, isBanClick, null)
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     */
    fun invisibleViewByAlpha(
        view: View,
        durationMillis: Long
    ) {
        invisibleViewByAlpha(view, durationMillis, false, null)
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param isBanClick        在执行动画的过程中是否禁止点击
     * @param animationListener 动画监听器
     */
    fun invisibleViewByAlpha(
        view: View,
        isBanClick: Boolean, animationListener: AnimationListener?
    ) {
        invisibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, animationListener
        )
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param animationListener 动画监听器
     */
    fun invisibleViewByAlpha(
        view: View,
        animationListener: AnimationListener?
    ) {
        invisibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            false, animationListener
        )
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view       被处理的视图
     * @param isBanClick 在执行动画的过程中是否禁止点击
     */
    fun invisibleViewByAlpha(
        view: View,
        isBanClick: Boolean
    ) {
        invisibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, null
        )
    }

    /**
     * 将给定视图渐渐隐去（view.setVisibility(View.INVISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view 被处理的视图
     */
    fun invisibleViewByAlpha(view: View) {
        invisibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            false, null
        )
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)）
     *
     * @param view              被处理的视图
     * @param durationMillis    持续时间，毫秒
     * @param isBanClick        在执行动画的过程中是否禁止点击
     * @param animationListener 动画监听器
     */
    fun goneViewByAlpha(
        view: View, durationMillis: Long,
        isBanClick: Boolean, animationListener: AnimationListener?
    ) {
        if (view.visibility != View.GONE) {
            view.visibility = View.GONE
            val hiddenAlphaAnimation =
                getHiddenAlphaAnimation(durationMillis)
            hiddenAlphaAnimation!!.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = false
                    }
                    animationListener?.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    animationListener?.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = true
                    }
                    animationListener?.onAnimationEnd(animation)
                }
            })
            view.startAnimation(hiddenAlphaAnimation)
        }
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)）
     *
     * @param view              被处理的视图
     * @param durationMillis    持续时间，毫秒
     * @param animationListener 动画监听器
     */
    fun goneViewByAlpha(
        view: View, durationMillis: Long,
        animationListener: AnimationListener?
    ) {
        goneViewByAlpha(view, durationMillis, false, animationListener)
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun goneViewByAlpha(
        view: View, durationMillis: Long,
        isBanClick: Boolean
    ) {
        goneViewByAlpha(view, durationMillis, isBanClick, null)
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     */
    fun goneViewByAlpha(view: View, durationMillis: Long) {
        goneViewByAlpha(view, durationMillis, false, null)
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param isBanClick        在执行动画的过程中是否禁止点击
     * @param animationListener 动画监听器
     */
    fun goneViewByAlpha(
        view: View,
        isBanClick: Boolean, animationListener: AnimationListener?
    ) {
        goneViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, animationListener
        )
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param animationListener 动画监听器
     */
    fun goneViewByAlpha(
        view: View,
        animationListener: AnimationListener?
    ) {
        goneViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION, false,
            animationListener
        )
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view       被处理的视图
     * @param isBanClick 在执行动画的过程中是否禁止点击
     */
    fun goneViewByAlpha(view: View, isBanClick: Boolean) {
        goneViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, null
        )
    }

    /**
     * 将给定视图渐渐隐去最后从界面中移除（view.setVisibility(View.GONE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view 被处理的视图
     */
    fun goneViewByAlpha(view: View) {
        goneViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION, false,
            null
        )
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)）
     *
     * @param view              被处理的视图
     * @param durationMillis    持续时间，毫秒
     * @param isBanClick        在执行动画的过程中是否禁止点击
     * @param animationListener 动画监听器
     */
    fun visibleViewByAlpha(
        view: View, durationMillis: Long,
        isBanClick: Boolean, animationListener: AnimationListener?
    ) {
        if (view.visibility != View.VISIBLE) {
            view.visibility = View.VISIBLE
            val showAlphaAnimation =
                getShowAlphaAnimation(durationMillis)
            showAlphaAnimation!!.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = false
                    }
                    animationListener?.onAnimationStart(animation)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    animationListener?.onAnimationRepeat(animation)
                }

                override fun onAnimationEnd(animation: Animation) {
                    if (isBanClick) {
                        view.isClickable = true
                    }
                    animationListener?.onAnimationEnd(animation)
                }
            })
            view.startAnimation(showAlphaAnimation)
        }
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)）
     *
     * @param view              被处理的视图
     * @param durationMillis    持续时间，毫秒
     * @param animationListener 动画监听器
     */
    fun visibleViewByAlpha(
        view: View, durationMillis: Long,
        animationListener: AnimationListener?
    ) {
        visibleViewByAlpha(view, durationMillis, false, animationListener)
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun visibleViewByAlpha(
        view: View, durationMillis: Long,
        isBanClick: Boolean
    ) {
        visibleViewByAlpha(view, durationMillis, isBanClick, null)
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)）
     *
     * @param view           被处理的视图
     * @param durationMillis 持续时间，毫秒
     */
    fun visibleViewByAlpha(view: View, durationMillis: Long) {
        visibleViewByAlpha(view, durationMillis, false, null)
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param animationListener 动画监听器
     * @param isBanClick        在执行动画的过程中是否禁止点击
     */
    fun visibleViewByAlpha(
        view: View,
        isBanClick: Boolean, animationListener: AnimationListener?
    ) {
        visibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, animationListener
        )
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view              被处理的视图
     * @param animationListener 动画监听器
     */
    fun visibleViewByAlpha(
        view: View,
        animationListener: AnimationListener?
    ) {
        visibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            false, animationListener
        )
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view       被处理的视图
     * @param isBanClick 在执行动画的过程中是否禁止点击
     */
    fun visibleViewByAlpha(
        view: View,
        isBanClick: Boolean
    ) {
        visibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            isBanClick, null
        )
    }

    /**
     * 将给定视图渐渐显示出来（view.setVisibility(View.VISIBLE)），
     * 默认的持续时间为DEFAULT_ALPHA_ANIMATION_DURATION
     *
     * @param view 被处理的视图
     */
    fun visibleViewByAlpha(view: View) {
        visibleViewByAlpha(
            view, AnimationUtils.DEFAULT_ANIMATION_DURATION,
            false, null
        )
    }

    /*
	 *  ************************************************************* 视图移动动画
	 * ********************************************************************
	 */

    /*
	 *  ************************************************************* 视图移动动画
	 * ********************************************************************
	 */
    /**
     * 视图移动
     *
     * @param view           要移动的视图
     * @param fromXDelta     X轴开始坐标
     * @param toXDelta       X轴结束坐标
     * @param fromYDelta     Y轴开始坐标
     * @param toYDelta       Y轴结束坐标
     * @param cycles         重复
     * @param durationMillis 持续时间
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun translate(
        view: View,
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        cycles: Float,
        durationMillis: Long,
        isBanClick: Boolean
    ) {
        val translateAnimation = TranslateAnimation(
            fromXDelta, toXDelta, fromYDelta, toYDelta
        )
        translateAnimation.duration = durationMillis
        if (cycles > 0.0) {
            translateAnimation.interpolator = CycleInterpolator(cycles)
        }
        translateAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (isBanClick) {
                    view.isClickable = false
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (isBanClick) {
                    view.isClickable = true
                }
            }
        })
        view.startAnimation(translateAnimation)
    }

    /**
     * 视图移动
     *
     * @param view           要移动的视图
     * @param fromXDelta     X轴开始坐标
     * @param toXDelta       X轴结束坐标
     * @param fromYDelta     Y轴开始坐标
     * @param toYDelta       Y轴结束坐标
     * @param cycles         重复
     * @param durationMillis 持续时间
     */
    fun translate(
        view: View,
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        cycles: Float,
        durationMillis: Long
    ) {
        translate(
            view, fromXDelta, toXDelta, fromYDelta, toYDelta, cycles,
            durationMillis, false
        )
    }

    /**
     * 视图摇晃
     *
     * @param view           要摇动的视图
     * @param fromXDelta     X轴开始坐标
     * @param toXDelta       X轴结束坐标
     * @param cycles         重复次数
     * @param durationMillis 持续时间
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun shake(
        view: View, fromXDelta: Float, toXDelta: Float,
        cycles: Float, durationMillis: Long, isBanClick: Boolean
    ) {
        translate(
            view, fromXDelta, toXDelta, 0.0f, 0.0f, cycles,
            durationMillis, isBanClick
        )
    }

    /**
     * 视图摇晃
     *
     * @param view           要摇动的视图
     * @param fromXDelta     X轴开始坐标
     * @param toXDelta       X轴结束坐标
     * @param cycles         重复次数
     * @param durationMillis 持续时间
     */
    fun shake(
        view: View, fromXDelta: Float, toXDelta: Float,
        cycles: Float, durationMillis: Long
    ) {
        translate(
            view, fromXDelta, toXDelta, 0.0f, 0.0f, cycles,
            durationMillis, false
        )
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，重复7次
     *
     * @param view     view
     * @param cycles         重复次数
     * @param durationMillis 持续时间
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun shake(
        view: View, cycles: Float, durationMillis: Long,
        isBanClick: Boolean
    ) {
        translate(
            view, 0.0f, 10.0f, 0.0f, 0.0f, cycles, durationMillis,
            isBanClick
        )
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，持续700毫秒
     *
     * @param view    view
     * @param cycles         重复次数
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun shake(
        view: View,
        cycles: Float,
        isBanClick: Boolean
    ) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, cycles, 700, isBanClick)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10
     *
     * @param view            view
     * @param cycles         重复次数
     * @param durationMillis 持续时间
     */
    fun shake(
        view: View,
        cycles: Float,
        durationMillis: Long
    ) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, cycles, durationMillis, false)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，重复7次
     *
     * @param view      view
     * @param durationMillis 持续时间
     * @param isBanClick     在执行动画的过程中是否禁止点击
     */
    fun shake(
        view: View, durationMillis: Long,
        isBanClick: Boolean
    ) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, 7f, durationMillis, isBanClick)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，持续700毫秒
     *
     * @param view   要摇动的视图
     * @param cycles 重复次数
     */
    fun shake(view: View, cycles: Float) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, cycles, 700, false)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，重复7次
     *
     * @param view     view
     * @param durationMillis 持续时间
     */
    fun shake(view: View, durationMillis: Long) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, 7f, durationMillis, false)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，重复7次，持续700毫秒
     *
     * @param view      view
     * @param isBanClick 在执行动画的过程中是否禁止点击
     */
    fun shake(view: View, isBanClick: Boolean) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, 7f, 700, isBanClick)
    }

    /**
     * 视图摇晃，默认摇晃幅度为10，重复7次，持续700毫秒
     *
     * @param view  view
     */
    fun shake(view: View) {
        translate(view, 0.0f, 10.0f, 0.0f, 0.0f, 7f, 700, false)
    }

}