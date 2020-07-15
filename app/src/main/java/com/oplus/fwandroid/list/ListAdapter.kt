package com.oplus.fwandroid.list

import android.graphics.Paint
import android.os.Build
import android.text.SpannableString
import android.text.style.ImageSpan
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.oplus.fwandroid.R
import com.oplus.fwandroid.common.bean.GoodsList
import com.oplus.fwandroid.common.glide.GlideHelper
import com.oplus.fwandroid.common.utils.NumberFormat
import com.oplus.fwandroid.common.widget.CenterAlignImageSpan

/**
 * @author Sinaan
 * @date 2020/6/24
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
class ListAdapter(layoutId: Int, data: MutableList<GoodsList>? = null) :
    BaseQuickAdapter<GoodsList, BaseViewHolder>(layoutId, data) {
    override fun convert(helper: BaseViewHolder, item: GoodsList) {
        item?.let {
            helper.setText(R.id.tv_m, "¥" + NumberFormat.formatStringToString2(item.originalPrice))
                .setText(R.id.tv_coupon_m, item.actualPrice)
                .setText(R.id.tv_coupon, "¥" + NumberFormat.formatStringToString2(item.couponPrice))
                .setText(R.id.tv_profit, NumberFormat.formatStringToString2(item.integral) + "铜板")
                .setText(
                    R.id.tv_brokerage,
                    "¥" + NumberFormat.formatStringToString2(item.brokerage)
                )
        }

        //文本中间划横线
        var tvM = helper.getView(R.id.tv_m) as TextView
        tvM.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG

        //文本显示样式
        var tvTitle = helper.getView(R.id.tv_title) as TextView
        val sp = SpannableString("  " + item.title)
        //获取一张图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val drawable = context.getDrawable(R.drawable.ic_tb_label)
            drawable!!.setBounds(0, 0, drawable!!.minimumWidth, drawable!!.minimumHeight)
            //居中对齐imageSpan
            val imageSpan = CenterAlignImageSpan(drawable)
            sp.setSpan(imageSpan, 0, 1, ImageSpan.ALIGN_BASELINE)
            tvTitle.text = sp
        }

        //显示图片
        var ivLogo = helper.getView(R.id.iv_logo) as ImageView
        GlideHelper.load(item.mainPic!!,ivLogo)
    }
}