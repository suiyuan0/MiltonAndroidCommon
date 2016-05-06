
package com.milton.common.demo.activity.listview;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.milton.common.activity.BaseActivity;
import com.milton.common.adapter.ExpandableListAdapter;
import com.milton.common.demo.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ExpandableListItemActivity extends BaseActivity {

    private List<String> items = new LinkedList<String>();
    private ListView list;
    private MyAdapter adapter;
    private Toast toast;

    private void showToast(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    private void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_expandable_listitem);
        list = (ListView) this.findViewById(R.id.list);

        // fill the list with data
        // list.setAdapter(buildDummyData());
        File f = new File("/");
        File[] files = f.listFiles();
        if (f != null && f.listFiles() != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                items.add(file.getName());
            }
        }
        adapter = new MyAdapter(items);
        list.setAdapter(new ExpandableListAdapter(adapter,
                R.id.expandable_toggle_button, R.id.item, R.id.expandable));
        // listen for events in the two buttons for every list item.
        // the 'position' var will tell which list item is clicked
    }

    public class MyAdapter extends BaseAdapter {
        private List<String> values;
        private LayoutInflater inflater;

        public MyAdapter(List<String> values) {
            // TODO Auto-generated constructor stub
            this.values = values;
            inflater = LayoutInflater.from(ExpandableListItemActivity.this);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return values.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return values.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listitem_expandable,
                        null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.text);
            tv.setText(values.get(position));
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showToast("item click position = " + position);
                }
            });
            return convertView;
        }

    }

}
