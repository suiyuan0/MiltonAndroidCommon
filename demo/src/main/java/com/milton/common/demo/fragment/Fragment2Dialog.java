
package com.milton.common.demo.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.milton.common.demo.R;
import com.milton.common.demo.adapter.CheckTextAdapter;
import com.milton.common.dialog.LoadingDialog;
import com.milton.common.util.ScreenUtil;
import com.milton.common.widget.MaxHeightListView;
import com.milton.common.widget.common.CheckItem;

import java.util.ArrayList;


public class Fragment2Dialog extends Fragment2Base {

    ProgressDialog pd;
    LoadingDialog loadingDialog;
    AlertDialog mSecretarySeviceDialog = null;
    AlertDialog mBuySecretarySeviceDialog = null;
    private PopupWindow mPopupWindow;

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
                "pop",
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
                showFilterPopupWindow(view);
                break;
            case 1:
                showDialog();
                break;
            case 2:
                showDialog2();
                break;
            case 3:
                showProgressDialog();
                break;
            case 4:
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

    private void showFilterPopupWindow(View view) {
        if (mPopupWindow == null) {
            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.window_notification_filter, null);
            final CheckItem selectAll = (CheckItem) contentView.findViewById(R.id.ci_select_all);
            final MaxHeightListView listview = (MaxHeightListView) contentView.findViewById(R.id.lv_list);

            selectAll.setTextName(R.string.select_all);
            selectAll.setOnCkeckItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CheckTextAdapter) listview.getAdapter()).selectAll(selectAll.isChecked());
                }
            });

            listview.setListViewHeight(ScreenUtil.getScreenHeight(getActivity()) * 2 / 3);
            listview.setAdapter(new CheckTextAdapter(getActivity(), getFilterDataList(), selectAll));


            Button sure = (Button) contentView.findViewById(R.id.btn_sure);
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow.dismiss();
                    CheckTextAdapter adapter = (CheckTextAdapter) listview.getAdapter();
                    adapter.backupData();
//                    filterNotification(adapter.getCheckedList());
                }
            });


            mPopupWindow = new PopupWindow(contentView, View.MeasureSpec.makeMeasureSpec(ScreenUtil.getScreenWidth(getActivity()) * 2 / 3, View.MeasureSpec.AT_MOST), ViewGroup.LayoutParams.WRAP_CONTENT, true);

            mPopupWindow.setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            ((CheckTextAdapter) ((ListView) mPopupWindow.getContentView().findViewById(R.id.lv_list)).getAdapter()).restoreData();
            mPopupWindow.showAsDropDown(view, 0, 0);
        }
    }

    private ArrayList<CheckTextAdapter.ItemBean> getFilterDataList() {
        ArrayList<CheckTextAdapter.ItemBean> result = new ArrayList<>();
        result.add(new CheckTextAdapter.ItemBean(getString(R.string.zone_alarm_fire), "", false));
        result.add(new CheckTextAdapter.ItemBean(getString(R.string.zone_alarm_help), "", false));
        result.add(new CheckTextAdapter.ItemBean(getString(R.string.zone_alarm_gas), "", false));
        result.add(new CheckTextAdapter.ItemBean(getString(R.string.zone_alarm_thief), "", false));
        result.add(new CheckTextAdapter.ItemBean(getString(R.string.zone_alarm_emergency), "", false));
        return result;
    }
}
