package com.oplus.fwandroid.common.utils;/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.oplus.fwandroid.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * 对话框工具箱
 *
 * @author xiaopan
 */
public class DialogUtils {

    private static AlertDialog alertDialog;

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
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String centerButton, DialogInterface.OnClickListener centerButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener, DialogInterface.OnShowListener onShowListener, boolean cancelable, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context);
        if (title != null) {
            promptBuilder.setTitle(title);
        }
        if (message != null) {
            promptBuilder.setMessage(message);
        }
        if (confirmButton != null) {
            promptBuilder.setPositiveButton(confirmButton,
                    confirmButtonClickListener);
        }
        if (centerButton != null) {
            promptBuilder.setNeutralButton(centerButton,
                    centerButtonClickListener);
        }
        if (cancelButton != null) {
            promptBuilder.setNegativeButton(cancelButton,
                    cancelButtonClickListener);
        } else {
            promptBuilder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
        }
        promptBuilder.setCancelable(true);
        if (cancelable) {
            promptBuilder.setOnCancelListener(onCancelListener);
        }
        alertDialog = promptBuilder.create();
        if (!(context instanceof Activity)) {
            alertDialog.getWindow()
                    .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        alertDialog.setOnDismissListener(onDismissListener);
        alertDialog.setOnShowListener(onShowListener);
        alertDialog.show();
        return alertDialog;
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
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener) {
        return showAlert(context, title, message, confirmButton,
                confirmButtonClickListener, null, null, cancelButton,
                cancelButtonClickListener, null, true, null, null);
    }

    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    public static AlertDialog showAlert(Context context, String message) {
        return showAlert(context, null, message, null, null, null,
                null, null, null, null, true, null, null);
    }

    /**
     * 显示一个对话框
     *
     * @param context                    上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message                    消息
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @return 对话框
     */
    public static AlertDialog showAlert(Context context, String message, DialogInterface.OnClickListener confirmButtonClickListener) {
        return showAlert(context, "温馨提示", message, "确认",
                confirmButtonClickListener, null, null, null,
                null, null, true, null, null);
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
    public static AlertDialog showAlert(Context context, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener) {
        return showAlert(context, "温馨提示", message, confirmButton,
                confirmButtonClickListener, null, null, null,
                null, null, true, null, null);
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
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener) {
        return showAlert(context, title, message, confirmButton,
                confirmButtonClickListener, null, null, null,
                null, null, true, null, null);
    }

    /**
     * 显示一个提示框
     *
     * @param context       上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message       提示的消息
     * @param confirmButton 确定按钮的名字
     */
    public static AlertDialog showPrompt(Context context, String message, String confirmButton) {
        return showAlert(context, null, message, confirmButton, null, null,
                null, null, null, null, true, null, null);
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    public static AlertDialog showPrompt(Context context, String message) {
        return showAlert(context, null, message, "OK", null, null, null, null,
                null, null, true, null, null);
    }

    /**
     * 从底部弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    public static AlertDialog showDialogFromBottom(Activity context, View view) {
        AlertDialog popupLog = new AlertDialog.Builder(context).create();
        Window win = popupLog.getWindow();
        win.setWindowAnimations(R.style.dialog_style);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.x = 0;
        wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        popupLog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        popupLog.setCanceledOnTouchOutside(true);
        popupLog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = popupLog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        popupLog.getWindow().setAttributes(lp);
        if (view != null) {
            win.setContentView(view);
        }
        return popupLog;
    }

    /**
     * 从底部弹出一个dialog
     *
     * @param context      当前Activity对象
     * @param view         需要弹起的视图
     * @param editLayoutId 包含edit的布局id,AlertDialog中的EdtText默认是不会弹出软键盘的
     * @return AlertDialog
     */
    public static AlertDialog showDialogFromBottom(Activity context, View view, int editLayoutId) {
        AlertDialog popupLog = new AlertDialog.Builder(context).create();
        popupLog.setView(SystemService.getLayoutInflater(context).inflate(editLayoutId, null));
        Window win = popupLog.getWindow();
        win.setWindowAnimations(R.style.dialog_style);
        WindowManager.LayoutParams wl = win.getAttributes();
        wl.x = 0;
        wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        popupLog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        popupLog.setCanceledOnTouchOutside(true);
        popupLog.show();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = popupLog.getWindow().getAttributes();
        lp.width = display.getWidth(); // 设置宽度
        popupLog.getWindow().setAttributes(lp);
        if (view != null) {
            win.setContentView(view);
        }
        return popupLog;
    }

    /**
     * 在中间弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    public static AlertDialog showDialogInCenter(Activity context, View view) {
        AlertDialog popupLog = new AlertDialog.Builder(context).create();
        popupLog.setView(view);
        popupLog.show();
        return popupLog;
    }

    /**
     * 在中间弹出一个dialog
     *
     * @param context 当前Activity对象
     * @param view    需要弹起的视图
     * @return AlertDialog
     */
    public static AlertDialog showForceDialogInCenter(Activity context, View view) {
        AlertDialog popupLog = new AlertDialog.Builder(context).create();
        popupLog.setView(view);
        popupLog.setCanceledOnTouchOutside(false);
        popupLog.show();
        return popupLog;
    }

    public static AlertDialog showEditDialog(final TextView tv, final EditDialogConfirmButtonClickListener confirmButtonClickListener) {
        Activity context = ((Activity) tv.getContext());
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);
        String s = tv.getText().toString();
        edit.setText("无".equals(s) ? "" : s);
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        final Activity context2 = context;
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit.getText().toString();
                tv.setText(StringUtils.isEmpty(content.trim()) ? "无" : content);
                confirmButtonClickListener.editConfirm(v, content);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        return editDialog;
    }

    public static AlertDialog showEditDialog(Activity context, final EditDialogConfirmButtonClickListener confirmButtonClickListener) {
        return showEditDialog(context, "", confirmButtonClickListener);
    }

    public static AlertDialog showEditDialog(Activity context, String defaultContent, final EditDialogConfirmButtonClickListener confirmButtonClickListener) {
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        edit.setText(defaultContent);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit.getText().toString();
                confirmButtonClickListener.editConfirm(v, content);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        return editDialog;
    }

    public static AlertDialog showEditDialog(Activity context, String defaultContent, final EditDialogConfirmButtonClickListener confirmButtonClickListener, final boolean dismissDialog) {
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        edit.setText(defaultContent);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit.getText().toString();
                confirmButtonClickListener.editConfirm(v, content);
                if (dismissDialog) editDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        return editDialog;
    }

    public static AlertDialog showEditDialog(Activity context, String defaultTitle, String defaultContent, final EditDialogConfirmButtonClickListener confirmButtonClickListener, final boolean dismissDialog) {
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final TextView title = (TextView) editView.findViewById(R.id.title);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        title.setText(defaultTitle);
        edit.setText(defaultContent);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit.getText().toString();
                confirmButtonClickListener.editConfirm(v, content);
                if (dismissDialog) editDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        InputMethodUtils.openSoftKeyboard(context, edit);
        return editDialog;
    }

    public static String showEditDialog(final View tv) {
        Activity context = ((Activity) tv.getContext());
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);

        if (tv instanceof TextView)
            edit.setText(((TextView) tv).getText());
        if (tv instanceof EditText)
            edit.setText(((EditText) tv).getText());
        final String[] finalContent = {""};
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalContent[0] = edit.getText().toString();
                if (tv instanceof TextView)
                    ((TextView) tv).setText(finalContent[0]);
                if (tv instanceof EditText)
                    ((EditText) tv).setText(finalContent[0]);
                editDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        return finalContent[0];
    }

    public static String showEditDigitsDialog(final View tv) {
        Activity context = ((Activity) tv.getContext());
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit, null);
        final EditText edit = (EditText) editView.findViewById(R.id.edit);
        edit.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        Button confirm = (Button) editView.findViewById(R.id.confirm);
        Button cancel = (Button) editView.findViewById(R.id.cancel);

        if (tv instanceof TextView)
            edit.setText(((TextView) tv).getText());
        if (tv instanceof EditText)
            edit.setText(((EditText) tv).getText());
        final String[] finalContent = {""};
        final AlertDialog editDialog = showDialogInCenter(context, editView);
        editDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalContent[0] = edit.getText().toString();
                if (tv instanceof TextView)
                    ((TextView) tv).setText(finalContent[0]);
                if (tv instanceof EditText)
                    ((EditText) tv).setText(finalContent[0]);
                editDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        return finalContent[0];
    }

    public static void showTextDialog(Activity context, String content, final View.OnClickListener confirmButtonClickListener) {
        showTextDialog(context, content, confirmButtonClickListener, null);
    }

    public static void showTextDialog(Activity context, String content, String confirmWord, final View.OnClickListener confirmButtonClickListener) {
        showTextDialog(context, content, confirmWord, confirmButtonClickListener, null);
    }

    public static void showTextDialog(Activity context, String content, final View.OnClickListener confirmButtonClickListener, final View.OnLongClickListener cancelButtonLongClickListener) {
        showTextDialog(context, content, null, confirmButtonClickListener, cancelButtonLongClickListener);
    }

    public static void showTextDialog(Activity context, String content, String confirmWord, final View.OnClickListener confirmButtonClickListener, final View.OnLongClickListener cancelButtonLongClickListener) {
        View textView = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);
        final TextView text = (TextView) textView.findViewById(R.id.text);
        //设置文本可以滚动
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button confirm = (Button) textView.findViewById(R.id.confirm);
        confirm.setText(StringUtils.isEmpty(confirmWord) ? "确认" : confirmWord);
        Button cancel = (Button) textView.findViewById(R.id.cancel);
        text.setText(content);
        final AlertDialog textDialog = showDialogInCenter(context, textView);
        textDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
                confirmButtonClickListener.onClick(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
            }
        });
        if (cancelButtonLongClickListener != null)
            cancel.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    textDialog.dismiss();
                    cancelButtonLongClickListener.onLongClick(view);
                    return false;
                }
            });
    }

    public static void showTextDialog(Activity context, final String content) {
        View textView = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);
        final TextView text = (TextView) textView.findViewById(R.id.text);
        //设置文本可以滚动
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button confirm = (Button) textView.findViewById(R.id.confirm);
        Button cancel = (Button) textView.findViewById(R.id.cancel);
        text.setText(content);
        final AlertDialog textDialog = showDialogInCenter(context, textView);
        textDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
            }
        });
    }

    public static void showForceTextDialog(Activity context, String content, final View.OnClickListener confirmButtonClickListener) {
        View textView = LayoutInflater.from(context).inflate(R.layout.dialog_text, null);
        final TextView text = (TextView) textView.findViewById(R.id.text);
        //设置文本可以滚动
        text.setMovementMethod(ScrollingMovementMethod.getInstance());
        Button confirm = (Button) textView.findViewById(R.id.confirm);
        Button cancel = (Button) textView.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        text.setText(content);
        final AlertDialog textDialog = showForceDialogInCenter(context, textView);
        textDialog.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
                confirmButtonClickListener.onClick(v);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textDialog.dismiss();
            }
        });
    }

    public static void showPopupViewAsDropDown(final Activity activity, View mountView, View popupView) {
        PopupWindow mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setWidth(DensityUtil.getDeviceInfo(activity)[0] / 3);
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.5f;
        activity.getWindow().setAttributes(lp);
        mPopupWindow.showAsDropDown(mountView);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow()
                        .getAttributes();
                lp.alpha = 1.0f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }

    public static class EditDialogConfirmButtonClickListener implements View.OnClickListener {
        public void editConfirm(View v, String content) {
            onClick(v);
        }

        @Override
        public void onClick(View v) {

        }
    }
}