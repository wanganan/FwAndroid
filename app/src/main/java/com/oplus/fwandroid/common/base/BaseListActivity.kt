package com.oplus.fwandroid.common.base

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.glide.GlideApp
import com.oplus.fwandroid.common.utils.DensityUtil
import kotlinx.android.synthetic.main.activity_base_list.*


/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：纯ListView或者GridView实现该类。需传入Bean类型T
 * version: 1.0
 */
abstract class BaseListActivity<T> : BaseLoadActivity() {
    var mAdapter: BaseQuickAdapter<T, BaseViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refreshLayout.setOnRefreshListener { refresh() }
        refreshLayout.setOnLoadMoreListener { loadMore() }

        recyclerView.isFocusableInTouchMode = false
        recyclerView.requestFocus()
        var layoutManager = GridLayoutManager(this, 1)
        layoutManager!!.orientation = when (style()) {
            0 -> {
                //横向禁用刷新
                refreshLayout.setEnableLoadMore(false)
                refreshLayout.setEnableRefresh(false)
                LinearLayoutManager.HORIZONTAL
            }
            else -> {
                layoutManager.spanCount = style()
                LinearLayoutManager.VERTICAL
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        //解决数据加载不完的问题
        recyclerView.isNestedScrollingEnabled = false
        //当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        recyclerView.setHasFixedSize(true)
        //解决数据加载完成后, 没有停留在顶部的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            recyclerView.focusable = View.NOT_FOCUSABLE

        //添加分割线
        if (showDivider()) recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        mAdapter = adapter()
        recyclerView.adapter = mAdapter
        mAdapter!!.animationEnable = true//是否打开动画,默认false
        mAdapter!!.isAnimationFirstOnly = true//动画是否仅第一次执行
        mAdapter!!.setAnimationWithDefault(BaseQuickAdapter.AnimationType.ScaleIn)//设置使用内置默认动画，有5种。渐显、缩放、从下到上，从左到右、从右到左
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            itemClick(position)
        }
        mAdapter!!.setOnItemLongClickListener { adapter, view, position ->
            itemLongClick(position)
            false
        }
        mAdapter!!.addChildClickViewIds(*childClickRegisterIds())//注册需要点击的子控件id（注意，请不要写在convert方法里）
        mAdapter!!.setOnItemChildClickListener { adapter, view, position ->
            itemChildClick(view, position)
        }
        mAdapter!!.addChildLongClickViewIds(*childLongClickRegisterIds())// 先注册需要长按的子控件id（注意，请不要写在convert方法里）
        mAdapter!!.setOnItemChildLongClickListener { adapter, view, position ->
            itemChildLongClick(view, position)
            false
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    //在列表滑动过程中可以调用Glide的pauseRequests方法来是图片暂停加载，当滑动结束后再调用resumeRequests来恢复加载
                    2 -> { // SCROLL_STATE_FLING
                        GlideApp.with(applicationContext).pauseRequests()
                    }
                    0 -> { // SCROLL_STATE_IDLE
                        GlideApp.with(applicationContext).resumeRequests()
                    }
                    1 -> { // SCROLL_STATE_TOUCH_SCROLL
                        GlideApp.with(applicationContext).resumeRequests()
                    }
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val height = DensityUtil.getScreenHeight(this@BaseListActivity)
                if (getScrollYDistance(layoutManager) > height * 0.8) {
                    top.visibility = View.VISIBLE
                } else {
                    top.visibility = View.GONE
                }
            }
        })
        top.setOnClickListener {
            recyclerView.smoothScrollToPosition(0)
            top.visibility = View.GONE
        }
    }

    /**
     * 返回需要的适配器，泛型是一个实体类bean
     */
    abstract fun adapter(): BaseQuickAdapter<T, BaseViewHolder>

    /**
     * 刷新加载布局
     */
    override fun layoutLoad(): Int {
        return R.layout.activity_base_list
    }

    /**
     * 首次加载数据
     */
    abstract override fun loadInitialData()

    /**
     * 下拉刷新
     */
    abstract fun refresh()

    /**
     * 上拉加载更多
     */
    abstract fun loadMore()

    /**
     * 界面风格，返回列数，值建议写Style常量。如果是HORIZONTAL，则是水平方向的列表。否则是竖直方向的
     */
    abstract fun style(): Int
    class Style {
        companion object {
            const val HORIZONTAL = 0
            const val SINGLE = 1
            const val DOUBLE = 2
            const val TRIPLE = 3
        }
    }

    /**
     * 是否显示分割线
     */
    abstract fun showDivider(): Boolean

    /**
     * item的点击事件
     */
    abstract fun itemClick(position: Int)

    /**
     * item的长按事件
     */
    abstract fun itemLongClick(position: Int)

    /**
     * 和itemChildClick照应，想要实现点击效果的item子view的Ids
     * 添加方法intArrayOf(id1,id2,id3)
     */
    abstract fun childClickRegisterIds(): IntArray

    /**
     * item里子view的点击事件，需要先添加在childClickRegisterIds中
     */
    abstract fun itemChildClick(view: View, position: Int)

    /**
     * 和itemChildLongClick照应，想要实现长按效果的item子view的Ids
     */
    abstract fun childLongClickRegisterIds(): IntArray

    /**
     * item里子view的长按事件，需要先添加在childLongClickRegisterIds中
     */
    abstract fun itemChildLongClick(view: View, position: Int)

    /**
     * 获取RecyclerView纵向的滑动距离
     */
    private fun getScrollYDistance(layoutManager: GridLayoutManager): Int {
        val position = layoutManager.findFirstVisibleItemPosition()
        val firstVisibleChildView = layoutManager.findViewByPosition(position)
        val itemHeight = firstVisibleChildView!!.height
        return position * itemHeight - firstVisibleChildView!!.top
    }
}