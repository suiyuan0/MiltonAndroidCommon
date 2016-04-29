
package com.milton.common.demo.activity.util;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.milton.common.activity.BaseActivity;
import com.milton.common.adapter.OneLineTextListAdapter;
import com.milton.common.demo.R;

public class UtilsBaseActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mList;
    private String mItemName[] = {
            "getScreenPhysicalSize",
            "isTablet",
    };

    @Override
    public void setView() {
        setContentView(R.layout.activity_util);
    }

    @Override
    public void initView() {
        mList = (ListView) findViewById(R.id.util_list);
    }

    @Override
    public void setListener() {
        mList.setOnItemClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mList.setAdapter(new OneLineTextListAdapter(getItemNames()));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
    }

    public String[] getItemNames() {
        return new String[] {
                "getScreenPhysicalSize",
                "isTablet",
        };
    }
}
