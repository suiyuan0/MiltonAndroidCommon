
package com.milton.common.demo.activity.imageloader;

import android.widget.GridView;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;

public class ImageLoaderBaseActivity extends BaseActivity {
    protected GridView mGridImage;

    @Override
    public void initView() {
        setContentView(R.layout.activity_image_loader);
        mGridImage = (GridView) findViewById(R.id.gv_image);
        mGridImage.setNumColumns(3);
    }

}
