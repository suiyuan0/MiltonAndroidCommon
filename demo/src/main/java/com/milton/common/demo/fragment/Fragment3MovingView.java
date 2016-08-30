
package com.milton.common.demo.fragment;


import com.milton.common.demo.R;
import com.milton.common.widget.movingview.MovingDotView;
import com.milton.common.widget.movingview.OnAnimatorChangeListener;

public class Fragment3MovingView extends BaseFragment {
    MovingDotView mMovingDotView;

    @Override
    protected int getContentView() {
        return R.layout.fragment3_movingview;
    }

    @Override
    protected void initView() {
        super.initView();
        mMovingDotView = (MovingDotView) rootView.findViewById(R.id.main_movingView);
        mMovingDotView.setProgress(50);
        mMovingDotView.setToProgress(10);
        mMovingDotView.setChangeListener(new OnAnimatorChangeListener() {
            @Override
            public void onProgressChanged(float progress) {
                if (progress == 1) {
                    showToastShort("清理成功");
                }
            }
        });

    }

//    public void onBackPressed() {
//        if (mMovingDotView.isCleaned()) {
//            mMovingDotView.backClean();
//        } else {
//            super.onBackPressed();
//        }
//    }


}
