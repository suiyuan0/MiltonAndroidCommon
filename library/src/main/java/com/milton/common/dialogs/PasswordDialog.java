package com.milton.common.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.milton.common.lib.R;
import com.milton.common.widget.MaxHeightListView;

/**
 * Created by milton on 16/5/27.
 */
public class PasswordDialog extends Dialog implements View.OnClickListener {


    public interface ViewCreateListener {
        public void initTop(TextView top);

        public void initContent(ListView content);
    }

    public interface DataCallback {
        void getPassword(String password);
    }

    public PasswordDialog(Context context, DataCallback dataCallback) {
        super(context, R.style.bottomDialogStyle);
        this.mContext = context;
        this.mDataCallback = dataCallback;
    }

    private TextView mTop;
    private MaxHeightListView mContent;
    private ViewCreateListener mViewCreateListener;
    private Context mContext;
    private StringBuilder stringBuilder = new StringBuilder();
    private EditText mEditTextFirst;
    private EditText mEditTextSecond;
    private EditText mEditTextThird;
    private EditText mEditTextFourth;
    private EditText mEditTextFifth;
    private EditText mEdtiTextSixth;
    private DataCallback mDataCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();

        window.setContentView(R.layout.dialog_password);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimBottom);
        mTop = (TextView) findViewById(R.id.tv_title);

        if (null != mViewCreateListener) {
            mViewCreateListener.initTop(mTop);
            mViewCreateListener.initContent(mContent);
        }
        initView();

    }

    public ViewCreateListener getmViewCreateListener() {
        return mViewCreateListener;
    }

    public void setViewCreateListener(ViewCreateListener iewCreateListener) {
        this.mViewCreateListener = iewCreateListener;
    }

    public TextView getTop() {
        return mTop;
    }

    public ListView getContent() {
        return mContent;
    }

    private void initView() {
        findViewById(R.id.close_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);
        findViewById(R.id.btn_delete).setOnClickListener(this);

        mEditTextFirst = (EditText) findViewById(R.id.et_first);
        mEditTextSecond = (EditText) findViewById(R.id.et_second);
        mEditTextThird = (EditText) findViewById(R.id.et_third);
        mEditTextFourth = (EditText) findViewById(R.id.et_fourth);
        mEditTextFifth = (EditText) findViewById(R.id.et_fifth);
        mEdtiTextSixth = (EditText) findViewById(R.id.et_sixth);

    }

    private void initEvent() {

    }

    @Override
    public void onClick(View view) {

        String tag = (String) view.getTag();

        if (tag.equalsIgnoreCase("X")) {
            if (stringBuilder.length() > 0) {
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                setStringToEditText(stringBuilder.toString());
            } else {
                //无内容，则停止
            }

        } else {
            stringBuilder.append(tag);
            setStringToEditText(stringBuilder.toString());
        }
    }

    private void setStringToEditText(String string) {

        switch (string.length()) {
            case 0:
                mEditTextFirst.setText("");
                mEditTextSecond.setText("");
                mEditTextThird.setText("");
                mEditTextFourth.setText("");
                mEditTextFifth.setText("");
                mEdtiTextSixth.setText("");
                break;
            case 1:
                mEditTextFirst.setText(string.charAt(0) + "");
                mEditTextSecond.setText("");
                mEditTextThird.setText("");
                mEditTextFourth.setText("");
                mEditTextFifth.setText("");
                mEdtiTextSixth.setText("");
                break;
            case 2:
                mEditTextSecond.setText(string.charAt(1) + "");
                mEditTextThird.setText("");
                mEditTextFourth.setText("");
                mEditTextFifth.setText("");
                mEdtiTextSixth.setText("");
                break;
            case 3:
                mEditTextThird.setText(string.charAt(2) + "");
                mEditTextFourth.setText("");
                mEditTextFifth.setText("");
                mEdtiTextSixth.setText("");

                break;
            case 4:
                mEditTextFourth.setText(string.charAt(3) + "");
                mEditTextFifth.setText("");
                mEdtiTextSixth.setText("");
                break;
            case 5:
                mEditTextFifth.setText(string.charAt(4) + "");
                mEdtiTextSixth.setText("");
                break;
            case 6:
                mEdtiTextSixth.setText(string.charAt(5) + "");
                mDataCallback.getPassword(string);
                this.dismiss();
            default:
                break;
        }


    }

}
