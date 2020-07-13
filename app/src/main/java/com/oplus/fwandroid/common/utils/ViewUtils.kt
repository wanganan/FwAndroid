package com.oplus.fwandroid.common.utils

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.text.Selection
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.*
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：视图工具箱
 * version: 1.0
 */
object ViewUtils {
    private const val CLASS_NAME_GRID_VIEW = "android.widget.GridView"
    private const val FIELD_NAME_VERTICAL_SPACING = "mVerticalSpacing"

    /**
     * get ListView height according to every children
     *
     * @param view view
     * @return int
     */
    fun getListViewHeightBasedOnChildren(view: ListView?): Int {
        var height = getAbsListViewHeightBasedOnChildren(view)
        var adapter: ListAdapter? = null
        var adapterCount: Int = 1
        if (view != null && view.adapter.also {
                adapter = it
            } != null && adapter!!.count.also { adapterCount = it } > 0
        ) {
            height += view.dividerHeight * (adapterCount - 1)
        }
        return height
    }


    /**
     * get GridView vertical spacing
     *
     * @param view view
     * @return int
     */
    fun getGridViewVerticalSpacing(view: GridView?): Int {
        // get mVerticalSpacing by android.widget.GridView
        var demo: Class<*>? = null
        var verticalSpacing = 0
        try {
            demo = Class.forName(CLASS_NAME_GRID_VIEW)
            val field = demo.getDeclaredField(FIELD_NAME_VERTICAL_SPACING)
            field.isAccessible = true
            verticalSpacing = field[view] as Int
            return verticalSpacing
        } catch (e: Exception) {
            /**
             * accept all exception, include ClassNotFoundException, NoSuchFieldException, InstantiationException,
             * IllegalArgumentException, IllegalAccessException, NullPointException
             */
            e.printStackTrace()
        }
        return verticalSpacing
    }


    /**
     * get AbsListView height according to every children
     *
     * @param view view
     * @return int
     */
    fun getAbsListViewHeightBasedOnChildren(view: AbsListView?): Int {
        var adapter: ListAdapter? = null
        if (view == null || view.adapter.also { adapter = it } == null) {
            return 0
        }
        var height = 0
        for (i in 0 until adapter!!.count) {
            val item = adapter!!.getView(i, null, view)
            (item as? ViewGroup)?.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            item.measure(0, 0)
            height += item.measuredHeight
        }
        height += view.paddingTop + view.paddingBottom
        return height
    }


    /**
     * set view height
     *
     * @param view view
     * @param height height
     */
    fun setViewHeight(view: View?, height: Int) {
        if (view == null) {
            return
        }
        val params = view.layoutParams
        params.height = height
    }


    /**
     * @param view listview
     */
    fun setListViewHeightBasedOnChildren(view: ListView?) {
        setViewHeight(view, getListViewHeightBasedOnChildren(view))
    }


    /**
     * @param view AbsListView
     */
    fun setAbsListViewHeightBasedOnChildren(view: AbsListView?) {
        setViewHeight(view, getAbsListViewHeightBasedOnChildren(view))
    }


    /**
     * set SearchView OnClickListener
     *
     * @param v set SearchView OnClickListener
     * @param listener set SearchView OnClickListener
     */
    fun setSearchViewOnClickListener(
        v: View?,
        listener: View.OnClickListener?
    ) {
        if (v is ViewGroup) {
            val group = v
            val count = group.childCount
            for (i in 0 until count) {
                val child = group.getChildAt(i)
                if (child is LinearLayout ||
                    child is RelativeLayout
                ) {
                    setSearchViewOnClickListener(child, listener)
                }
                if (child is TextView) {
                    child.isFocusable = false
                }
                child.setOnClickListener(listener)
            }
        }
    }


    /**
     * get descended views from parent.
     *
     * @param <T> 泛型
     * @param parent ViewGroup
     * @param filter Type of views which will be returned.
     * @param includeSubClass Whether returned list will include views which
     * are
     * subclass of filter or not.
     * @return View
    </T> */
    fun <T : View?> getDescendants(
        parent: ViewGroup,
        filter: Class<T?>,
        includeSubClass: Boolean
    ): List<T?>? {
        val descendedViewList: MutableList<T?> = ArrayList()
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val childsClass: Class<out View> = child.javaClass
            if (includeSubClass && filter.isAssignableFrom(childsClass) ||
                !includeSubClass && childsClass == filter
            ) {
                descendedViewList.add(filter.cast(child))
            }
            if (child is ViewGroup) {
                descendedViewList.addAll(
                    getDescendants<T?>(
                        child, filter,
                        includeSubClass
                    )!!
                )
            }
        }
        return descendedViewList
    }


    /**
     * 手动测量布局大小
     *
     * @param view 被测量的布局
     * @param width 布局默认宽度
     * @param height 布局默认高度
     * 示例： measureView(view, ViewGroup.LayoutParams.MATCH_PARENT,
     * ViewGroup.LayoutParams.WRAP_CONTENT);
     */
    fun measureView(view: View, width: Int, height: Int) {
        var params = view.layoutParams
        if (params == null) {
            params = ViewGroup.LayoutParams(width, height)
        }
        val mWidth = ViewGroup.getChildMeasureSpec(0, 0, params.width)
        val mHeight: Int
        val tempHeight = params.height
        mHeight = if (tempHeight > 0) {
            View.MeasureSpec.makeMeasureSpec(
                tempHeight,
                View.MeasureSpec.EXACTLY
            )
        } else {
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        }
        view.measure(mWidth, mHeight)
    }

    //*****设置外边距相关函数*******************************************************************************


    //*****设置外边距相关函数*******************************************************************************
    /**
     * 设置View的左侧外边距
     *
     * @param view 要设置外边距的View
     * @param left 左侧外边距
     */
    fun setMarginLeft(view: View, left: Int) {
        setMargins(view, left, 0, 0, 0)
    }


    /**
     * 设置View的顶部外边距
     *
     * @param view 要设置外边距的View
     * @param top 顶部外边距
     */
    fun setMarginTop(view: View, top: Int) {
        setMargins(view, 0, top, 0, 0)
    }


    /**
     * 设置View的右侧外边距
     *
     * @param view 要设置外边距的View
     * @param right 右侧外边距
     */
    fun setMarginRight(view: View, right: Int) {
        setMargins(view, 0, 0, right, 0)
    }


    /**
     * 设置View的底部外边距
     *
     * @param view 要设置外边距的View
     * @param bottom 底部外边距
     */
    fun setMarginBottom(view: View, bottom: Int) {
        setMargins(view, 0, 0, 0, bottom)
    }


    /**
     * 设置View的外边距(Margins)
     *
     * @param view 要设置外边距的View
     * @param left 左侧外边距
     * @param top 顶部外边距
     * @param right 右侧外边距
     * @param bottom 底部外边距
     */
    fun setMargins(
        view: View,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view
                .layoutParams as MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout() //请求重绘
        }
    }


    /**
     * 获取一个LinearLayout
     *
     * @param context 上下文
     * @param orientation 流向
     * @param width 宽
     * @param height 高
     * @return LinearLayout
     */
    fun createLinearLayout(
        context: Context?,
        orientation: Int,
        width: Int,
        height: Int
    ): LinearLayout? {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = orientation
        linearLayout.layoutParams = LinearLayout.LayoutParams(width, height)
        return linearLayout
    }


    /**
     * 获取一个LinearLayout
     *
     * @param context 上下文
     * @param orientation 流向
     * @param width 宽
     * @param height 高
     * @param weight 权重
     * @return LinearLayout
     */
    fun createLinearLayout(
        context: Context?,
        orientation: Int,
        width: Int,
        height: Int,
        weight: Int
    ): LinearLayout? {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = orientation
        linearLayout.layoutParams = LinearLayout.LayoutParams(width, height, weight.toFloat())
        return linearLayout
    }


    /**
     * 根据ListView的所有子项的高度设置其高度
     */
    fun setListViewHeightByAllChildrenViewHeight(listView: ListView) {
        val listAdapter = listView.adapter
        if (listAdapter != null) {
            var totalHeight = 0
            for (i in 0 until listAdapter.count) {
                val listItem = listAdapter.getView(i, null, listView)
                listItem.measure(0, 0)
                totalHeight += listItem.measuredHeight
            }
            val params = listView.layoutParams
            params.height = totalHeight + listView.dividerHeight *
                    (listAdapter.count - 1)
            (params as MarginLayoutParams).setMargins(10, 10, 10, 10)
            listView.layoutParams = params
        }
    }


    /**
     * 将给定视图的高度增加一点
     *
     * @param view 给定的视图
     * @param increasedAmount 增加多少
     */
    fun addViewHeight(view: View, increasedAmount: Int) {
        val headerLayoutParams = view.layoutParams
        headerLayoutParams.height += increasedAmount
        view.layoutParams = headerLayoutParams
    }


    /**
     * 设置给定视图的宽度
     *
     * @param view 给定的视图
     * @param newWidth 新的宽度
     */
    fun setViewWidth(view: View, newWidth: Int) {
        val headerLayoutParams = view.layoutParams
        headerLayoutParams.width = newWidth
        view.layoutParams = headerLayoutParams
    }


    /**
     * 将给定视图的宽度增加一点
     *
     * @param view 给定的视图
     * @param increasedAmount 增加多少
     */
    fun addViewWidth(view: View, increasedAmount: Int) {
        val headerLayoutParams = view.layoutParams
        headerLayoutParams.width += increasedAmount
        view.layoutParams = headerLayoutParams
    }


    /**
     * 获取流布局的底部外边距
     */
    fun getLinearLayoutBottomMargin(linearLayout: LinearLayout): Int {
        return (linearLayout.layoutParams as LinearLayout.LayoutParams).bottomMargin
    }


    /**
     * 设置流布局的底部外边距
     */
    fun setLinearLayoutBottomMargin(
        linearLayout: LinearLayout,
        newBottomMargin: Int
    ) {
        val lp =
            linearLayout.layoutParams as LinearLayout.LayoutParams
        lp.bottomMargin = newBottomMargin
        linearLayout.layoutParams = lp
    }


    /**
     * 获取流布局的高度
     */
    fun getLinearLayoutHiehgt(linearLayout: LinearLayout): Int {
        return (linearLayout.layoutParams as LinearLayout.LayoutParams).height
    }


    /**
     * 设置输入框的光标到末尾
     */
    fun setEditTextSelectionToEnd(editText: EditText) {
        val editable = editText.editableText
        Selection.setSelection(
            editable,
            editable.toString().length
        )
    }


    /**
     * 执行测量，执行完成之后只需调用View的getMeasuredXXX()方法即可获取测量结果
     */
    fun measure(view: View): View {
        var p = view.layoutParams
        if (p == null) {
            p = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        val childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width)
        val lpHeight = p.height
        val childHeightSpec: Int
        childHeightSpec = if (lpHeight > 0) {
            View.MeasureSpec.makeMeasureSpec(
                lpHeight,
                View.MeasureSpec.EXACTLY
            )
        } else {
            View.MeasureSpec.makeMeasureSpec(
                0,
                View.MeasureSpec.UNSPECIFIED
            )
        }
        view.measure(childWidthSpec, childHeightSpec)
        return view
    }


    /**
     * 获取给定视图的测量高度
     */
    fun getMeasuredHeight(view: View): Int {
        return measure(view).measuredHeight
    }


    /**
     * 获取给定视图的测量宽度
     */
    fun getMeasuredWidth(view: View): Int {
        return measure(view).measuredWidth
    }


    /**
     * 获取视图1相对于视图2的位置，注意在屏幕上看起来视图1应该被视图2包含，但是视图1和视图并不一定是绝对的父子关系也可以是兄弟关系，只是一个大一个小而已
     */
    fun getRelativeRect(view1: View, view2: View): Rect {
        val childViewGlobalRect = Rect()
        val parentViewGlobalRect = Rect()
        view1.getGlobalVisibleRect(childViewGlobalRect)
        view2.getGlobalVisibleRect(parentViewGlobalRect)
        return Rect(
            childViewGlobalRect.left - parentViewGlobalRect.left,
            childViewGlobalRect.top - parentViewGlobalRect.top,
            childViewGlobalRect.right - parentViewGlobalRect.left,
            childViewGlobalRect.bottom - parentViewGlobalRect.top
        )
    }


    /**
     * 删除监听器
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun removeOnGlobalLayoutListener(
        viewTreeObserver: ViewTreeObserver,
        onGlobalLayoutListener: OnGlobalLayoutListener?
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            viewTreeObserver.removeGlobalOnLayoutListener(
                onGlobalLayoutListener
            )
        } else {
            viewTreeObserver.removeOnGlobalLayoutListener(
                onGlobalLayoutListener
            )
        }
    }


    /**
     * 缩放视图
     */
    fun zoomView(
        view: View,
        scaleX: Float,
        scaleY: Float,
        originalSize: Point
    ) {
        val width = (originalSize.x * scaleX).toInt()
        val height = (originalSize.y * scaleY).toInt()
        var viewGroupParams = view.layoutParams
        if (viewGroupParams != null) {
            viewGroupParams.width = width
            viewGroupParams.height = height
        } else {
            viewGroupParams = ViewGroup.LayoutParams(width, height)
        }
        view.layoutParams = viewGroupParams
    }


    /**
     * 缩放视图
     */
    fun zoomView(
        view: View,
        scaleX: Float,
        scaleY: Float
    ) {
        zoomView(
            view, scaleX, scaleY,
            Point(view.width, view.height)
        )
    }


    /**
     * 缩放视图
     *
     * @param scale 比例
     */
    fun zoomView(
        view: View,
        scale: Float,
        originalSize: Point
    ) {
        zoomView(view, scale, scale, originalSize)
    }


    /**
     * 缩放视图
     */
    fun zoomView(view: View, scale: Float) {
        zoomView(
            view, scale, scale,
            Point(view.width, view.height)
        )
    }
}