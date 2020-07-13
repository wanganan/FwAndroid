package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnShowListener
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import com.oplus.fwandroid.R

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：对话框工具箱
 * version: 1.0
 */
object DialogUtils {

    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title                      标题
     * @param message                    消息
     * @param confirmButton              确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param centerButton               中间按钮
     * @param centerButtonClickListener  中间按钮点击监听器
     * @param cancelButton               取消按钮
     * @param cancelButtonClickListener  取消按钮点击监听器
     * @param onShowListener             显示监听器
     * @param cancelable                 是否允许通过点击返回按钮或者点击对话框之外的位置关闭对话框
     * @param onCancelListener           取消监听器
     * @param onDismissListener          销毁监听器
     * @return 对话框
     */
    fun showAlert(
        context: Context?,
        title: String?,
        message: String?,
        confirmButton: String?,
        confirmButtonClickListener: DialogInterface.OnClickListener?,
        centerButton: String?,
        centerButtonClickListener: DialogInterface.OnClickListener?,
        cancelButton: String?,
        cancelButtonClickListener: DialogInterface.OnClickListener?,
        onShowListener: OnShowListener?,
        cancelable: Boolean,
        onCancelListener: DialogInterface.OnCancelListener?,
        onDismissListener: DialogInterface.OnDismissListener?
    ): AlertDialog? {
        var alertDialog: AlertDialog? = null
        val promptBuilder =
            AlertDialog.Builder(context)
        if (title != null) {
            promptBuilder.setTitle(title)
        }
        if (message != null) {
            promptBuilder.setMessage(message)
        }
        if (confirmButton != null) {
            promptBuilder.setPositiveButton(
                confirmButton,
                confirmButtonClickListener
            )
        }
        if (centerButton != null) {
            promptBuilder.setNeutralButton(
                centerButton,
                centerButtonClickListener
            )
        }
        if (cancelButton != null) {
            promptBuilder.setNegativeButton(
                cancelButton,
                cancelButtonClickListener
            )
        } else {
            promptBuilder.setNegativeButton(
                "取消"
            ) { dialog, which -> alertDialog!!.dismiss() }
        }
        promptBuilder.setCancelable(true)
        if (cancelable) {
            promptBuilder.setOnCancelListener(onCancelListener)
        }
        alertDialog = promptBuilder.create()
        if (context !is Activity) {
            alertDialog.window
                ?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        alertDialog.setOnDismissListener(onDismissListener)
        alertDialog.setOnShowListener(onShowListener)
        alertDialog.show()
        return alertDialog
    }


    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title                      标题
     * @param message                    消息
     * @param confirmButton              确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param cancelButton               取消按钮
     * @param cancelButtonClickListener  取消按钮点击监听器
     * @return 对话框
     */
    fun showAlert(
        context: Context?,
        title: String?,
        message: String?,
        confirmButton: String?,
        confirmButtonClickListener: DialogInterface.OnClickListener?,
        cancelButton: String?,
        cancelButtonClickListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        return showAlert(
            context, title, message, confirmButton,
            confirmButtonClickListener, null, null, cancelButton,
            cancelButtonClickListener, null, true, null, null
        )
    }

    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    fun showAlert(
        context: Context?,
        message: String?
    ): AlertDialog? {
        return showAlert(
            context, null, message, null, null, null,
            null, null, null, null, true, null, null
        )
    }

    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message                    消息
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @return 对话框
     */
    fun showAlert(
        context: Context?,
        message: String?,
        confirmButtonClickListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        return showAlert(
            context, "温馨提示", message, "确认",
            confirmButtonClickListener, null, null, null,
            null, null, true, null, null
        )
    }

    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message                    消息
     * @param confirmButton              确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @return 对话框
     */
    fun showAlert(
        context: Context?,
        message: String?,
        confirmButton: String?,
        confirmButtonClickListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        return showAlert(
            context, "温馨提示", message, confirmButton,
            confirmButtonClickListener, null, null, null,
            null, null, true, null, null
        )
    }

    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message                    消息
     * @param confirmButton              确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @return 对话框
     */
    fun showAlert(
        context: Context?,
        title: String?,
        message: String?,
        confirmButton: String?,
        confirmButtonClickListener: DialogInterface.OnClickListener?
    ): AlertDialog? {
        return showAlert(
            context, title, message, confirmButton,
            confirmButtonClickListener, null, null, null,
            null, null, true, null, null
        )
    }

    /**
     * 显示一个提示框
     *
     * @param context       上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message       提示的消息
     * @param confirmButton 确定按钮的名字
     */
    fun showPrompt(
        context: Context?,
        message: String?,
        confirmButton: String?
    ): AlertDialog? {
        return showAlert(
            context, null, message, confirmButton, null, null,
            null, null, null, null, true, null, null
        )
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    fun showPrompt(
        context: Context?,
        message: String?
    ): AlertDialog? {
        return showAlert(
            context, null, message, "OK", null, null, null, null,
            null, null, true, null, null
        )
    }

    /**
     * 从底部弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    fun showDialogFromBottom(
        context: Activity,
        view: View?
    ): AlertDialog? {
        val popupLog = AlertDialog.Builder(context).create()
        val win = popupLog.window
        win!!.setWindowAnimations(R.style.dialog_style)
        val wl = win.attributes
        wl.x = 0
        wl.y = context.windowManager.defaultDisplay.height
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置显示位置
        popupLog.onWindowAttributesChanged(wl)
        // 设置点击外围解散
        popupLog.setCanceledOnTouchOutside(true)
        popupLog.show()
        val windowManager = context.windowManager
        val display = windowManager.defaultDisplay
        val lp = popupLog.window!!.attributes
        lp.width = display.width // 设置宽度
        popupLog.window!!.attributes = lp
        if (view != null) {
            win.setContentView(view)
        }
        return popupLog
    }

    /**
     * 从底部弹出一个dialog
     *
     * @param context      当前Activity对象
     * @param view         需要弹起的视图
     * @param editLayoutId 包含edit的布局id,AlertDialog中的EdtText默认是不会弹出软键盘的
     * @return AlertDialog
     */
    fun showDialogFromBottom(
        context: Activity,
        view: View?,
        editLayoutId: Int
    ): AlertDialog? {
        val popupLog = AlertDialog.Builder(context).create()
        popupLog.setView(SystemService.getLayoutInflater(context)?.inflate(editLayoutId, null))
        val win = popupLog.window
        win!!.setWindowAnimations(R.style.dialog_style)
        val wl = win.attributes
        wl.x = 0
        wl.y = context.windowManager.defaultDisplay.height
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置显示位置
        popupLog.onWindowAttributesChanged(wl)
        // 设置点击外围解散
        popupLog.setCanceledOnTouchOutside(true)
        popupLog.show()
        val windowManager = context.windowManager
        val display = windowManager.defaultDisplay
        val lp = popupLog.window!!.attributes
        lp.width = display.width // 设置宽度
        popupLog.window!!.attributes = lp
        if (view != null) {
            win.setContentView(view)
        }
        return popupLog
    }

    /**
     * 在中间弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    fun showDialogInCenter(
        context: Activity?,
        view: View?
    ): AlertDialog {
        val popupLog = AlertDialog.Builder(context).create()
        popupLog.setView(view)
        popupLog.show()
        return popupLog
    }

    /**
     * 在中间弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    fun showForceDialogInCenter(
        context: Activity?,
        view: View?
    ): AlertDialog {
        val popupLog = AlertDialog.Builder(context).create()
        popupLog.setView(view)
        popupLog.setCanceledOnTouchOutside(false)
        popupLog.show()
        return popupLog
    }

    fun showEditDialog(
        tv: TextView,
        confirmButtonClickListener: EditDialogConfirmButtonClickListener
    ): AlertDialog? {
        val context = tv.context as Activity
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        val s = tv.text.toString()
        edit.setText(if ("无" == s) "" else s)
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        val context2 = context
        confirm.setOnClickListener { v ->
            val content = edit.text.toString()
            tv.text = if (StringUtils.isEmpty(content.trim { it <= ' ' })) "无" else content
            confirmButtonClickListener.editConfirm(v, content)
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        return editDialog
    }

    fun showEditDialog(
        context: Activity?,
        confirmButtonClickListener: EditDialogConfirmButtonClickListener
    ): AlertDialog? {
        return showEditDialog(context, "", confirmButtonClickListener)
    }

    fun showEditDialog(
        context: Activity?,
        defaultContent: String?,
        confirmButtonClickListener: EditDialogConfirmButtonClickListener
    ): AlertDialog? {
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        edit.setText(defaultContent)
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        confirm.setOnClickListener { v ->
            val content = edit.text.toString()
            confirmButtonClickListener.editConfirm(v, content)
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        return editDialog
    }

    fun showEditDialog(
        context: Activity?,
        defaultContent: String?,
        confirmButtonClickListener: EditDialogConfirmButtonClickListener,
        dismissDialog: Boolean
    ): AlertDialog? {
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        edit.setText(defaultContent)
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        confirm.setOnClickListener { v ->
            val content = edit.text.toString()
            confirmButtonClickListener.editConfirm(v, content)
            if (dismissDialog) editDialog.dismiss()
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        return editDialog
    }

    fun showEditDialog(
        context: Activity?,
        defaultTitle: String?,
        defaultContent: String?,
        confirmButtonClickListener: EditDialogConfirmButtonClickListener,
        dismissDialog: Boolean
    ): AlertDialog? {
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val title = editView.findViewById<View>(
            R.id.title
        ) as TextView
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        title.text = defaultTitle
        edit.setText(defaultContent)
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        confirm.setOnClickListener { v ->
            val content = edit.text.toString()
            confirmButtonClickListener.editConfirm(v, content)
            if (dismissDialog) editDialog.dismiss()
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        InputMethodUtils.openSoftKeyboard(context!!, edit)
        return editDialog
    }

    fun showEditDialog(tv: View): String? {
        val context = tv.context as Activity
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        if (tv is TextView) edit.setText(tv.text)
        if (tv is EditText) edit.text = tv.text
        val finalContent = arrayOf("")
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        confirm.setOnClickListener {
            finalContent[0] = edit.text.toString()
            if (tv is TextView) tv.text = finalContent[0]
            if (tv is EditText) tv.setText(finalContent[0])
            editDialog.dismiss()
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        return finalContent[0]
    }

    fun showEditDigitsDialog(tv: View): String? {
        val context = tv.context as Activity
        val editView =
            LayoutInflater.from(context).inflate(R.layout.dialog_edit, null)
        val edit = editView.findViewById<View>(R.id.edit) as EditText
        edit.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        val confirm = editView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = editView.findViewById<View>(
            R.id.cancel
        ) as Button
        if (tv is TextView) edit.setText(tv.text)
        if (tv is EditText) edit.text = tv.text
        val finalContent = arrayOf("")
        val editDialog = showDialogInCenter(context, editView)
        editDialog.show()
        confirm.setOnClickListener {
            finalContent[0] = edit.text.toString()
            if (tv is TextView) tv.text = finalContent[0]
            if (tv is EditText) tv.setText(finalContent[0])
            editDialog.dismiss()
        }
        cancel.setOnClickListener { editDialog.dismiss() }
        return finalContent[0]
    }

    fun showTextDialog(
        context: Activity?,
        content: String?,
        confirmButtonClickListener: View.OnClickListener
    ) {
        showTextDialog(context, content, confirmButtonClickListener, null)
    }

    fun showTextDialog(
        context: Activity?,
        content: String?,
        confirmWord: String?,
        confirmButtonClickListener: View.OnClickListener
    ) {
        showTextDialog(context, content, confirmWord, confirmButtonClickListener, null)
    }

    fun showTextDialog(
        context: Activity?,
        content: String?,
        confirmButtonClickListener: View.OnClickListener,
        cancelButtonLongClickListener: OnLongClickListener?
    ) {
        showTextDialog(
            context,
            content,
            null,
            confirmButtonClickListener,
            cancelButtonLongClickListener
        )
    }

    fun showTextDialog(
        context: Activity?,
        content: String?,
        confirmWord: String?,
        confirmButtonClickListener: View.OnClickListener,
        cancelButtonLongClickListener: OnLongClickListener?
    ) {
        val textView =
            LayoutInflater.from(context).inflate(R.layout.dialog_text, null)
        val text = textView.findViewById<View>(R.id.text) as TextView
        //设置文本可以滚动
        text.movementMethod = ScrollingMovementMethod.getInstance()
        val confirm = textView.findViewById<View>(
            R.id.confirm
        ) as Button
        confirm.text = if (StringUtils.isEmpty(confirmWord)) "确认" else confirmWord
        val cancel = textView.findViewById<View>(
            R.id.cancel
        ) as Button
        text.text = content
        val textDialog = showDialogInCenter(context, textView)
        textDialog.show()
        confirm.setOnClickListener { v ->
            textDialog.dismiss()
            confirmButtonClickListener.onClick(v)
        }
        cancel.setOnClickListener { textDialog.dismiss() }
        if (cancelButtonLongClickListener != null) cancel.setOnLongClickListener { view ->
            textDialog.dismiss()
            cancelButtonLongClickListener.onLongClick(view)
            false
        }
    }

    fun showTextDialog(context: Activity?, content: String?) {
        val textView =
            LayoutInflater.from(context).inflate(R.layout.dialog_text, null)
        val text = textView.findViewById<View>(R.id.text) as TextView
        //设置文本可以滚动
        text.movementMethod = ScrollingMovementMethod.getInstance()
        val confirm = textView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = textView.findViewById<View>(
            R.id.cancel
        ) as Button
        text.text = content
        val textDialog = showDialogInCenter(context, textView)
        textDialog.show()
        confirm.setOnClickListener { textDialog.dismiss() }
        cancel.setOnClickListener { textDialog.dismiss() }
    }

    fun showForceTextDialog(
        context: Activity?,
        content: String?,
        confirmButtonClickListener: View.OnClickListener
    ) {
        val textView =
            LayoutInflater.from(context).inflate(R.layout.dialog_text, null)
        val text = textView.findViewById<View>(R.id.text) as TextView
        //设置文本可以滚动
        text.movementMethod = ScrollingMovementMethod.getInstance()
        val confirm = textView.findViewById<View>(
            R.id.confirm
        ) as Button
        val cancel = textView.findViewById<View>(
            R.id.cancel
        ) as Button
        cancel.visibility = View.GONE
        text.text = content
        val textDialog = showForceDialogInCenter(context, textView)
        textDialog.show()
        confirm.setOnClickListener { v ->
            textDialog.dismiss()
            confirmButtonClickListener.onClick(v)
        }
        cancel.setOnClickListener { textDialog.dismiss() }
    }

    fun showPopupViewAsDropDown(
        activity: Activity,
        mountView: View?,
        popupView: View?
    ) {
        val mPopupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        mPopupWindow.isTouchable = true
        mPopupWindow.isOutsideTouchable = true
        mPopupWindow.width = DensityUtil.getDeviceInfo(activity)!![0] / 3
        val lp = activity.window
            .attributes
        lp.alpha = 0.5f
        activity.window.attributes = lp
        mPopupWindow.showAsDropDown(mountView)
        mPopupWindow.setOnDismissListener {
            val lp = activity.window
                .attributes
            lp.alpha = 1.0f
            activity.window.attributes = lp
        }
    }

    class EditDialogConfirmButtonClickListener : View.OnClickListener {
        fun editConfirm(v: View, content: String?) {
            onClick(v)
        }

        override fun onClick(v: View) {}
    }
}