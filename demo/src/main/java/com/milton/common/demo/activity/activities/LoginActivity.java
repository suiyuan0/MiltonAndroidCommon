
package com.milton.common.demo.activity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.milton.common.demo.R;

/** 登陆界面activity */
public class LoginActivity extends Activity implements OnClickListener {
    /** 更多登陆项的显示View */
    private View view_more;
    /** 更多菜单，默认收起，点击后展开，再点击收起 */
    private View menu_more;
    private ImageView img_more_up;// 更多登陆项箭头图片
    private Button btn_login_regist;// 注册按钮
    /** 更所登陆项的菜单是否展开，默认收起 */
    private boolean isShowMenu = false;

    public static final int MENU_PWD_BACK = 1;
    public static final int MENU_HELP = 2;
    public static final int MENU_EXIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        img_more_up = (ImageView) findViewById(R.id.img_more_up);

        btn_login_regist = (Button) findViewById(R.id.btn_login_regist);
        btn_login_regist.setOnClickListener(this);

        menu_more = findViewById(R.id.menu_more);
        view_more = findViewById(R.id.view_more);
        view_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.view_more:
                showMoreMenu(isShowMenu);
                break;
            case R.id.btn_login_regist:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * 展示更多菜单的方法,boolean的默认值是false
     * 
     * @param show
     */
    public void showMoreMenu(boolean show) {
        if (show) {// 如果菜单不展开的时候
            menu_more.setVisibility(View.GONE);
            img_more_up.setImageResource(R.drawable.login_more_up);
            isShowMenu = false;
        } else {// 菜单展开的时候
            menu_more.setVisibility(View.VISIBLE);
            img_more_up.setImageResource(R.drawable.login_more);
            isShowMenu = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {// 创建系统功能菜单
//        // TODO Auto-generated method stub
//        menu.add(0, MENU_PWD_BACK, 1, "密码找回").setIcon(R.drawable.menu_findkey);
//        menu.add(0, MENU_HELP, 2, "帮助").setIcon(R.drawable.menu_setting);
//        menu.add(0, MENU_EXIT, 3, "退出").setIcon(R.drawable.menu_exit);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case MENU_PWD_BACK:
                break;
            case MENU_HELP:
                break;
            case MENU_EXIT:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
