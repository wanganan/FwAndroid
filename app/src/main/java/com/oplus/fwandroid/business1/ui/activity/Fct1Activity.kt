package com.oplus.fwandroid.business1.ui.activity

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.oplus.fwandroid.R
import com.oplus.fwandroid.business1.contract.IFct1Contract
import com.oplus.fwandroid.business1.di.component.DaggerFct1Component
import com.oplus.fwandroid.business1.di.module.Fct1UIModule
import com.oplus.fwandroid.business1.model.entity.Fct1Entity
import com.oplus.fwandroid.business1.presenter.Fct1Presenter
import com.oplus.fwandroid.business1.ui.adapter.Fct1Adapter
import com.oplus.fwandroid.common.base.BaseListActivity
import kotlinx.android.synthetic.main.activity_base_list.*
import javax.inject.Inject

/**
 * @author Sinaan
 * @date 2020/6/23
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：纯RecyclerView显示，可控制横向或纵向。
 * version: 1.0
 */
class Fct1Activity : BaseListActivity<Fct1Entity>(),
    IFct1Contract.IFct1View {
    /**
     * 字段注入
     * 由于某些 Android 框架类（如 Activity 和 Fragment）由系统实例化，因此 Dagger 无法为您创建这些类。
     * 具体而言，对于 Activity，任何初始化代码都需要放入 onCreate() 方法中。这意味着，无法像在前面的示例中那样，在类的构造函数中使用 @Inject 注释（构造函数注入）。必须改为使用字段注入。
     */
    @Inject
    lateinit var presenter: Fct1Presenter

    @Inject
    lateinit var listAdapter: Fct1Adapter
    private var pageIndex = 1

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

    override fun adapter(): BaseQuickAdapter<Fct1Entity, BaseViewHolder> {
        return listAdapter
    }

    override fun itemClick(position: Int) {
        showToast("第$position")
    }

    override fun itemLongClick(position: Int) {
        showToast("长按第$position")
    }

    override fun childClickRegisterIds(): IntArray {
        return intArrayOf(R.id.iv_logo, R.id.tv_m, R.id.tv_title)
    }

    override fun itemChildClick(view: View, position: Int) {
        when (view.id) {
            R.id.iv_logo -> showToast("点击图片第$position")
            R.id.tv_m -> showToast("点击原价第$position")
            R.id.tv_title -> showToast("点击标题第$position")
        }
    }

    override fun childLongClickRegisterIds(): IntArray {
        return intArrayOf(R.id.tv_title)
    }

    override fun itemChildLongClick(view: View, position: Int) {
        when (view.id) {
            R.id.tv_title -> showToast("长按标题第$position")
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
        //告知 Dagger 要求注入依赖项的对象，以使用 Dagger 图中 @Component 接口下的一些对象和函数。
        //需要提供一个函数，让该函数将请求注入的对象作为参数。
        DaggerFct1Component.builder().fct1UIModule(Fct1UIModule(this, R.layout.item)).build()
            .inject(this)
    }

    override fun loadInitialData() {
        pageIndex = 1
        presenter?.loadData(13370, 20, 1)
    }

    override fun loadSuccess(list: ArrayList<Fct1Entity>?, isRefresh: Boolean) {
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
        showToast(errorMsg)
        loadingTerminate(LoadStatus.ERROR)
    }
}