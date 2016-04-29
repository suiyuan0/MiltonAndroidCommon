
package com.milton.common.demo.activity.activities;

import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;
import com.milton.common.demo.adapter.CityAdapter;
import com.milton.common.demo.bean.CityEntity;
import com.milton.common.demo.util.Constants;
import com.milton.common.demo.view.HeadListView;

public class CityListActivity extends BaseActivity {
    private TextView title;
    private HeadListView mListView;
    private ArrayList<CityEntity> cityList;
    private CityAdapter mAdapter;

    public void initView() {
        setContentView(R.layout.activity_city);
        title = (TextView) findViewById(R.id.title);
        mListView = (HeadListView) findViewById(R.id.cityListView);
    }

    public void initData() {
        title.setText("当前城市-杭州");
        cityList = Constants.getCityList();
        mAdapter = new CityAdapter(this, cityList);
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(mAdapter);
        mListView.setPinnedHeaderView(LayoutInflater.from(this).inflate(R.layout.city_item_section, mListView, false));
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // Intent intent = new Intent(getApplicationContext(),
                // DetailsActivity.class);
                // startActivity(intent);
                // overridePendingTransition(R.anim.slide_in_right,
                // R.anim.slide_out_left);
            }
        });
    }

    public void doBack(View view) {
        onBackPressed();
    }
}
