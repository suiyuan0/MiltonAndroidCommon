package com.milton.common.widget.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milton.common.dialogs.BottomDialog;
import com.milton.common.lib.R;
import com.milton.common.pickerview.NumberPickerView;
import com.milton.common.util.ResourceUtil;
import com.milton.common.widget.GridViewGallery;

import java.util.List;


/**
 * Created by milton on 16/6/7.
 */
public class SelectItem extends RelativeLayout {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_INTEGER = 1;
    public static final int TYPE_ICON = 2;
    private int mType = TYPE_NORMAL;
    private List<BottomDialog.ItemBean> mDataList;
    private BottomDialog mSelectDialog;
    private NumberPickerView mNumberPickerView;
    //    private IconDialog mIconDialog;
    private TextView mName;
    private TextView mContent;
    private int mSelectedPosition = -1;
    private OnItemClickListener mOnItemClickListener;
    private OnNumberSelectedListener mOnNumberSelectedListener;
    private OnCustomClickListener mOnCustomClickListener;
    private OnContentChangedListener mOnContentChangedListener;
    private ImageView mSelectIcon;
    private int mIntegerValue;
    private String mDialogTitile;

    public SelectItem(Context context) {
        super(context);
    }

    public SelectItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.widget_select_item, this);

    }

    public SelectItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mName = (TextView) findViewById(R.id.tv_name);
        mContent = (TextView) findViewById(R.id.tv_content);
        mSelectIcon = (ImageView) findViewById(R.id.iv_icon);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCustomClickListener != null) {
                    mOnCustomClickListener.click(v);
                }
                showSelectDialog();
            }
        });
    }

    public void setName(int resId) {
        setName(getResources().getString(resId));
    }

    public void setName(String name) {
        if (mName == null) {
            mName = (TextView) findViewById(R.id.tv_name);
        }
        if (mName != null) {
            mName.setText(name);
        }
    }

    public void setContent(int postion) {
        setContent(postion, false);
    }

    public void setContent(int position, boolean triggerChangListener) {
        initContent();
        if (mContent != null) {
            mContent.setText(mDataList.get(position).mText);
            mSelectedPosition = position;
        }
        if (triggerChangListener && mOnContentChangedListener != null) {
            mOnContentChangedListener.contentChanged(mDataList.get(position));
        }
    }

    public int getContentValue() {
        return mIntegerValue;
    }

    public void setContentValue(int value) {
        initContent();
        if (mContent != null) {
            mIntegerValue = value;
            mContent.setText(mIntegerValue + "");
        }
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    private int calculatePosition(String name) {
        if (mDataList != null && mDataList.size() > 0) {
            final int size = mDataList.size();
            for (int i = 0; i < size; i++) {
                if (mDataList.get(i).mText.equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public TextView getContent() {
        return mContent;
    }

    public void setContent(String name) {
        initContent();
        if (mContent != null) {
            mContent.setText(name);
            mSelectedPosition = calculatePosition(name);
        }
    }

    public String getContentText() {
        return mContent == null ? "" : mContent.getText().toString();
    }

    public Object getContentObject() {
        if (mDataList != null && mSelectedPosition != -1) {
            return mDataList.get(mSelectedPosition).mData;
        } else {
            return null;
        }
    }

    public List<BottomDialog.ItemBean> getDataList() {
        return mDataList;
    }

    public void setDataList(List<BottomDialog.ItemBean> list) {
        mNumberPickerView = null;
        mSelectDialog = null;
        mDataList = list;
    }

    public Object getSeletedData() {
        if (mDataList != null && mSelectedPosition > 0 && mSelectedPosition < mDataList.size()) {
            return mDataList.get(mSelectedPosition).mData;
        } else {
            return null;
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void showSelectDialog() {
        if (mType == TYPE_NORMAL) {
            if (mSelectIcon.getVisibility() == VISIBLE) {
                if (null == mSelectDialog) {
                    mSelectDialog = new BottomDialog(getContext());
                    mSelectDialog.setViewCreateListener(new BottomDialog.ViewCreateListener() {
                        @Override
                        public void initTop(TextView top) {
                            top.setVisibility(View.GONE);
                        }

                        @Override
                        public void initContent(ListView content) {
                            content.setAdapter(new BottomDialog.ListAdapter(getContext(), -1, mDataList, false, new BottomDialog.ListAdapter.OnItemClickListener() {
                                @Override
                                public void itemClick(View view, int position, int index) {
                                    if (mOnItemClickListener != null) {
                                        mOnItemClickListener.itemClick(view, position);
                                    } else {
                                        setContent(position, true);
                                    }
                                    mSelectDialog.dismiss();
                                }
                            }));
                        }
                    });
                }
                if (!mSelectDialog.isShowing()) {
                    mSelectDialog.show();
                }
            }
        } else if (mType == TYPE_INTEGER) {
            if (null == mNumberPickerView) {
                mNumberPickerView = new NumberPickerView(getContext());
                mNumberPickerView.setCyclic(false);
                if (mDataList != null && mDataList.size() == 2) {
                    mNumberPickerView.setRange((int) mDataList.get(0).mData, (int) mDataList.get(1).mData);
                }
                mNumberPickerView.setCancelable(true);
                mNumberPickerView.setOnNubmerSelectListener(new NumberPickerView.OnNumberSelectListener() {
                    @Override
                    public void onNumberSelect(int num) {
                        if (mOnNumberSelectedListener != null) {
                            mOnNumberSelectedListener.numberSelected(num);
                        }
                        setContentValue(num);
                    }
                });
            }
            if (!mNumberPickerView.isShowing()) {
                mNumberPickerView.setNumber(mIntegerValue);
                mNumberPickerView.show();
            }

        } else if (mType == TYPE_ICON) {
//            if (null == mIconDialog) {
//                mIconDialog = new IconDialog(getContext());
//                mIconDialog.setViewCreateListener(new IconDialog.ViewCreateListener() {
//                    @Override
//                    public void initTop(TextView top) {
//                        top.setText(mDialogTitile);
//                    }
//
//                    @Override
//                    public void initContent(GridViewGallery content) {
//                        content.setVerticalSpacing(ResourceUtil.dp2px(getContext(), 0));
//                        content.setAdapter(new IconDialog.IconAdapter(getContext(), mDataList,
//                                new IconDialog.IconAdapter.OnItemClickListener() {
//                                    @Override
//                                    public void itemClick(View view, int position, int index) {
//                                        if (mOnItemClickListener != null) {
//                                            mOnItemClickListener.itemClick(view, position);
//                                        } else {
//                                            setContent(position, true);
//                                        }
//                                        mIconDialog.dismiss();
//                                    }
//                                }
//                        ));
//                        content.setNumColumns(4);
//                        content.setNumRows(4);
//                    }
//                });
//            }
//            if (!mIconDialog.isShowing()) {
//                mIconDialog.show();
//            }
        }
    }

    public void setOnContentChangedListener(OnContentChangedListener listener) {
        mOnContentChangedListener = listener;
    }

    public void setOnNumberSelectedListener(OnNumberSelectedListener listener) {
        mOnNumberSelectedListener = listener;
    }

    public void setSelectable(boolean selectable) {
        mSelectIcon.setVisibility(selectable ? View.VISIBLE : View.GONE);

    }

    public void setOnCustomClickListener(OnCustomClickListener listener) {
        mOnCustomClickListener = listener;
    }

    private void initContent() {
        if (mContent == null) {
            mContent = (TextView) findViewById(R.id.tv_content);
        }
    }

    public void setType(int type) {
        mType = type;
    }

    public void setDialogTitile(String title) {
        mDialogTitile = title;
    }

    public BottomDialog.ItemBean getItem(int position) {
        return mDataList.get(position);
    }

    public interface OnContentChangedListener {
        public void contentChanged(BottomDialog.ItemBean item);
    }

    public interface OnItemClickListener {
        public void itemClick(View view, int position);
    }

    public interface OnNumberSelectedListener {
        public void numberSelected(int number);
    }

    public interface OnCustomClickListener {
        public void click(View view);
    }
}
