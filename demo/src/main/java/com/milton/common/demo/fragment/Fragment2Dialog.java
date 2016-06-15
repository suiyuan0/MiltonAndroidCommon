
package com.milton.common.demo.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.milton.common.demo.R;
import com.milton.common.dialog.LoadingDialog;


public class Fragment2Dialog extends Fragment2Base {

    public Class[] getItemClass() {
        return new Class[]{
                // UtilsScreenActivity.class,
                // UtilsContextActivity.class,
                // UtilsResourceActivity.class,
                // UtilsStringActivity.class,
                // UtilsNetActivity.class,
                // UtilsNotificationActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "底部",
                "中间",
                "等待",
                "加载框"

        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        // getActivity().startActivity(new Intent(getActivity(), mClassList[i]));
        switch (i) {
            case 0:
                showDialog();
                break;
            case 1:
                showDialog2();
                break;
            case 2:
                showProgressDialog();
                break;
            case 3:
                showLoadingDialog();
                break;
//            case 4:
//                showIntentApkNotify();
//                break;
            case 5:

                break;

            default:
                break;
        }
    }

    ProgressDialog pd;

    private void showProgressDialog() {
        if (null == pd) {
            pd = new ProgressDialog(getActivity());
        }
        if (pd.isShowing()) {
            pd.hide();
        } else {
            pd.show();
        }
    }

    LoadingDialog loadingDialog;

    private void showLoadingDialog() {
        if (null == loadingDialog) {
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.setCanceledOnTouchOutside(false);
        }
        if (loadingDialog.isShowing()) {
            loadingDialog.hide();
        } else {
            loadingDialog.show();
        }
    }

    AlertDialog mSecretarySeviceDialog = null;

    private void showDialog() {
        if (mSecretarySeviceDialog == null) {
            mSecretarySeviceDialog = new AlertDialog.Builder(getActivity()).create();
            mSecretarySeviceDialog.show();
            Window window = mSecretarySeviceDialog.getWindow();
            window.setContentView(R.layout.dialog_secretary_service);
            window.setLayout(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.AnimBottom);
            Button ok = (Button) window.findViewById(R.id.btn_ok);
            Button cancel = (Button) window.findViewById(R.id.btn_cancel);
            ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mSecretarySeviceDialog.cancel();
                }
            });
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mSecretarySeviceDialog.cancel();
                }
            });
        } else {
            mSecretarySeviceDialog.show();
        }
    }

    AlertDialog mBuySecretarySeviceDialog = null;

    private void showDialog2() {
        if (mBuySecretarySeviceDialog == null) {
            mBuySecretarySeviceDialog = new AlertDialog.Builder(
                    getActivity()).create();
            mBuySecretarySeviceDialog.show();
            Window window = mBuySecretarySeviceDialog.getWindow();
            window.setContentView(R.layout.dialog_buy_secretary_service);
            window.setLayout(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.AnimBottom);
            Button ok = (Button) window.findViewById(R.id.btn_ok);
            Button cancel = (Button) window.findViewById(R.id.btn_cancel);
            ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mBuySecretarySeviceDialog.cancel();
                }
            });
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mBuySecretarySeviceDialog.cancel();
                }
            });
        } else {
            mBuySecretarySeviceDialog.show();
        }
    }
}
