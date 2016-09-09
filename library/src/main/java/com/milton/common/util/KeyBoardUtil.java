
package com.milton.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyBoardUtil {
    private static KeyBoardUtil instance;
    private InputMethodManager mInputMethodManager;

    private KeyBoardUtil(Context context) {
        mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public static KeyBoardUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (KeyBoardUtil.class) {
                if (instance == null) {
                    instance = new KeyBoardUtil(context);
                }
            }
        }
        return instance;
    }

//    /**
//     * 强制显示输入法
//     */
//    public void show() {
//        show(mActivity.getWindow().getCurrentFocus());
//    }

    /**
     * 为给定的编辑器开启软键盘
     *
     * @param editText 给定的编辑器
     */
    public static void openSoftKeyboard(Context context, EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager
                = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText,
                InputMethodManager.SHOW_IMPLICIT);
        ViewUtils.setEditTextSelectionToEnd(editText);
    }

    /**
     * 关闭软键盘
     */
    public static void closeSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager
                = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        //如果软键盘已经开启
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 切换软键盘的状态
     */
    public static void toggleSoftKeyboardState(Context context) {
        ((InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void show(View view) {
        mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 强制关闭输入法
     */
    public void hide(Activity activity) {
        hide(activity.getWindow().getCurrentFocus());
    }

    public void hide(View view) {
        mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
     */
    public void showOrHide() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 打卡软键盘
     *
     * @param mEditText 输入框
     */
    public void openKeybord(EditText mEditText) {
        mInputMethodManager.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     *
     * @param mEditText 输入框
     */
    public void closeKeybord(EditText mEditText) {
        mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 判断隐藏软键盘是否弹出,弹出就隐藏
     *
     * @param mActivity
     * @return
     */
    public boolean keyBoxIsShow(Activity mActivity) {
        if (mActivity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            //隐藏软键盘
            mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            return true;
        } else {
            return false;
        }
    }
}
