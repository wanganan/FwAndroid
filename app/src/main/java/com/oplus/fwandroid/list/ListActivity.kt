package com.oplus.fwandroid.list

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.base.BaseListActivity
import com.oplus.fwandroid.common.bean.GoodsList
import kotlinx.android.synthetic.main.activity_base_list.*

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：纯RecyclerView显示，可控制横向或纵向
 * version: 1.0
 */
class ListActivity : BaseListActivity<GoodsList>(),
    IListContract.IListView {
    var presenter: ListPresenter? = null
    var pageIndex = 1

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun refresh() {
        loadInitialData()
    }

    override fun loadMore() {
        presenter?.loadData(13370, 20, ++pageIndex)
    }

    override fun style(): Int {
        return Style.DOUBLE
    }

    override fun showDivider(): Boolean {
        return false
    }

    override fun adapter(): BaseQuickAdapter<GoodsList, BaseViewHolder> {
        return ListAdapter(R.layout.item, mutableListOf())
    }

    override fun itemClick(position: Int) {
        showShortToast("第$position")
    }

    override fun itemLongClick(position: Int) {
        showShortToast("长按第$position")
    }

    override fun childClickRegisterIds(): IntArray {
        return intArrayOf(R.id.iv_logo, R.id.tv_m, R.id.tv_title)
    }

    override fun itemChildClick(view: View, position: Int) {
        when (view.id) {
            R.id.iv_logo -> showShortToast("点击图片第$position")
            R.id.tv_m -> showShortToast("点击原价第$position")
            R.id.tv_title -> showShortToast("点击标题第$position")
        }
    }

    override fun childLongClickRegisterIds(): IntArray {
        return intArrayOf(R.id.tv_title)
    }

    override fun itemChildLongClick(view: View, position: Int) {
        when (view.id) {
            R.id.tv_title -> showShortToast("长按标题第$position")
        }
    }

    override fun content(): String {
        return "List列表"
    }

    override fun next(): String {
        return "保存"
    }

    override fun nextAction() {
    }

    override fun initView() {
        presenter = ListPresenter()
        presenter?.attachView(this)
    }

    override fun loadInitialData() {
        pageIndex = 1
        presenter?.loadData(13370, 20, 1)
    }

    override fun loadSuccess(list: ArrayList<GoodsList>?, isRefresh: Boolean) {
        if (list!!.size > 0) {
            if (isRefresh) {
                mAdapter?.setNewInstance(list)
                refreshLayout.finishRefresh()
            } else {
                mAdapter?.addData(list!!)
                //这里要刷新一下，addData未有有效刷新
                mAdapter?.notifyDataSetChanged()
                refreshLayout.finishLoadMore()
            }
            loadingTerminate(LoadStatus.OK)
        } else {
            loadingTerminate(LoadStatus.EMPTY)
        }
    }

    override fun loadFailure(errorMsg: String?) {
        showShortToast(errorMsg)
        loadingTerminate(LoadStatus.ERROR)
    }
}