package com.milton.common.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.milton.common.lib.R;

/**
 * Created by shushunsakai on 16/6/22.
 */
public class PicSelectDialog extends Dialog {

    private Button mButton;
    private ClickListener clickListener;

    public PicSelectDialog(Context context,ClickListener clickListener) {
        super(context, R.style.bottomDialogStyle);
        this.clickListener = clickListener;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.setContentView(R.layout.dialog_pic_select);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.AnimBottom);
        initView();

    }

    private void initView() {
        mButton = (Button) findViewById(R.id.btn_select_pic);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickListener.PicSelectOnClik();
                PicSelectDialog.this.dismiss();

//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//调用android的图库
//                getContext().startActivity(intent);
            }
        });
        findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.CamerOnClick();
                PicSelectDialog.this.dismiss();
            }
        });
    }


    public interface ClickListener {
        void PicSelectOnClik();

        void CamerOnClick();

    }


}
