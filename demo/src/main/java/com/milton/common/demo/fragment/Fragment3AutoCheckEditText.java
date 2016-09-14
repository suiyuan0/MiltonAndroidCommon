
package com.milton.common.demo.fragment;


import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.widget.EditText;

import com.milton.common.demo.R;
import com.milton.common.util.ViewUtils;
import com.milton.common.widget.AutoCheckEditText;

public class Fragment3AutoCheckEditText extends BaseFragment {

    private AutoCheckEditText mobileET;
    private AutoCheckEditText telET;
    private AutoCheckEditText emailET;
    private AutoCheckEditText urlET;
    private AutoCheckEditText chzET;
    private AutoCheckEditText usernameET;
    private AutoCheckEditText inputRegexET;
    private EditText inputET;
    private Drawable successDrable;
    private Drawable unsuccessDrable;

    @Override
    protected int getContentView() {
        return R.layout.fragment3_autocheckedittext;
    }

    @Override
    protected void initView() {
        super.initView();
        successDrable = ResourcesCompat.getDrawable(getResources(), R.drawable.success, getActivity().getApplicationContext().getTheme());
        unsuccessDrable = ResourcesCompat.getDrawable(getResources(), R.drawable.unsuccess, getActivity().getApplicationContext().getTheme());
        mobileET = ViewUtils.$(rootView, R.id.et_ac_mobile);
        telET = ViewUtils.$(rootView, R.id.et_ac_tel);
        emailET = ViewUtils.$(rootView, R.id.et_ac_email);
        urlET = ViewUtils.$(rootView, R.id.et_ac_url);
        chzET = ViewUtils.$(rootView, R.id.et_ac_chz);
        usernameET = ViewUtils.$(rootView, R.id.et_ac_username);
        inputET = ViewUtils.$(rootView, R.id.et_ac_input);
        inputRegexET = ViewUtils.$(rootView, R.id.et_ac_input_regex);

        mobileET.creatCheck(AutoCheckEditText.TYPE_OF_MOBILE, successDrable, unsuccessDrable);
        telET.creatCheck(AutoCheckEditText.TYPE_OF_TEL, successDrable, unsuccessDrable);
        emailET.creatCheck(AutoCheckEditText.TYPE_OF_EMAIL, successDrable, unsuccessDrable);
        urlET.creatCheck(AutoCheckEditText.TYPE_OF_URL, successDrable, unsuccessDrable);
        chzET.creatCheck(AutoCheckEditText.TYPE_OF_CHZ, successDrable, unsuccessDrable);
        usernameET.creatCheck(AutoCheckEditText.TYPE_OF_USERNAME, successDrable, unsuccessDrable);
        inputRegexET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    inputRegexET.creatCheck(AutoCheckEditText.TYPE_OF_USER_DEFINE,
                            successDrable, unsuccessDrable,
                            inputET.getText().toString());
                }
            }
        });
    }


}
