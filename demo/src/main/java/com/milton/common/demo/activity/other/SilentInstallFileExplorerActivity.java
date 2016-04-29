
package com.milton.common.demo.activity.other;

import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SilentInstallFileExplorerActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    SimpleAdapter adapter;
    String rootPath = Environment.getExternalStorageDirectory().getPath();
    String currentPath = rootPath;
    List<Map<String, Object>> list = new ArrayList<>();

    @Override
    public void initView() {
        setContentView(R.layout.activity_silent_install_file_explorer);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new SimpleAdapter(this, list, R.layout.listitem_silent_install_file_explorer,
                new String[] {
                        "name", "img"
                }, new int[] {
                        R.id.name, R.id.img
                });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        refreshListItems(currentPath);

    }

    private void refreshListItems(String path) {
        setTitle(path);
        File[] files = new File(path).listFiles();
        list.clear();
        if (files != null) {
            for (File file : files) {
                Map<String, Object> map = new HashMap<>();
                if (file.isDirectory()) {
                    map.put("img", R.drawable.silent_install_directory);
                } else {
                    map.put("img", R.drawable.silent_install_file_doc);
                }
                map.put("name", file.getName());
                map.put("currentPath", file.getPath());
                list.add(map);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        currentPath = (String) list.get(position).get("currentPath");
        File file = new File(currentPath);
        if (file.isDirectory())
            refreshListItems(currentPath);
        else {
            Intent intent = new Intent();
            intent.putExtra("apk_path", file.getPath());
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if (rootPath.equals(currentPath)) {
            super.onBackPressed();
        } else {
            File file = new File(currentPath);
            currentPath = file.getParentFile().getPath();
            refreshListItems(currentPath);
        }
    }

}
