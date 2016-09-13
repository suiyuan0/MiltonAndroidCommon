
package com.milton.common.demo;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.milton.common.application.BaseApplication;
import com.milton.common.demo.db.SQLHelper;

public class AppApplication extends BaseApplication {
    private static AppApplication mAppApplication;
    private SQLHelper sqlHelper;

    /**
     * 获取Application
     */
    public static AppApplication getApp() {
        return mAppApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppApplication = this;
        initAV();


    }

    /**
     * 获取数据库Helper
     */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
        // 整体摧毁的时候调用这个方法
    }


    private void initAV() {
        //如果使用美国节点，请加上这行代码 AVOSCloud.useAVCloudUS();
        AVOSCloud.useAVCloudCN();
        AVOSCloud.initialize(this, "iVJlxRogsAX8A3TjwhVcOVA3-gzGzoHsz", "khzy4SS6lVPcIlYtmlV6KxJg");
        AVOSCloud.setDebugLogEnabled(true);
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }
}
