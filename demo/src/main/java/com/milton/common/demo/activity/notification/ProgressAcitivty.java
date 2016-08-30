
package com.milton.common.demo.activity.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;

import com.milton.common.demo.R;
import com.milton.common.demo.activity.util.UtilsBaseActivity;

public class ProgressAcitivty extends UtilsBaseActivity {
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;
    /**
     * 下载线程是否暂停
     */
    public boolean isPause = false;
    /**
     * 通知栏内是否是自定义的
     */
    public boolean isCustom = false;
    /**
     * true:为不确定样式的 false:确定样式
     */
    public Boolean indeterminate = false;
    /**
     * Notification的ID
     */
    int notifyId = 102;
    /**
     * Notification的进度条数值
     */
    int progress = 0;
    NotificationCompat.Builder mBuilder;
    DownloadThread downloadThread;

    @Override
    public String[] getItemNames() {
        return new String[]{
                "显示带默认样式进度条通知栏",
                "显示带（不确定）默认样式进度条通知栏",
                "显示自定义 带进度条的通知栏",
                "开始下载",
                "暂停下载",
                "取消下载进度",
        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int position, long l) {
        switch (position) {
            case 0:
                downloadThread = null;
                isCustom = false;
                indeterminate = false;
                showProgressNotify();
                break;
            case 1:
                downloadThread = null;
                isCustom = false;
                indeterminate = true;
                showProgressNotify();
                break;
            case 2:
                downloadThread = null;
                isCustom = true;
                indeterminate = false;
                showCustomProgressNotify("等待下载..");
                break;
            case 3:
                startDownloadNotify();
                break;
            case 4:
                pauseDownloadNotify();
                break;
            case 5:
                stopDownloadNotify();
                break;
            default:
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        initService();
        initNotify();
    }

    /**
     * 初始化要用到的系统服务
     */
    private void initService() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    /**
     * 清除当前创建的通知栏
     */
    public void clearNotify(int notifyId) {
        mNotificationManager.cancel(notifyId);// 删除一个特定的通知ID对应的通知
        // mNotification.cancel(getResources().getString(R.string.app_name));
    }

    /**
     * 清除所有通知栏
     */
    public void clearAllNotify() {
        mNotificationManager.cancelAll();// 删除你发的所有通知
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除： Notification.FLAG_AUTO_CANCEL
     */
    public PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setContentIntent(getDefalutIntent(0))
                // .setNumber(number)//显示数量
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 //
                // requires VIBRATE permission
                .setSmallIcon(R.drawable.notification_icon);
    }

    /**
     * 显示带进度条通知栏
     */
    public void showProgressNotify() {
        mBuilder.setContentTitle("等待下载")
                .setContentText("进度:")
                .setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的
        if (indeterminate) {
            // 不确定进度的
            mBuilder.setProgress(0, 0, true);
        } else {
            // 确定进度的
            mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条 设置为true就是不确定的那种进度条效果
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * 显示自定义的带进度条通知栏
     */
    private void showCustomProgressNotify(String status) {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.notification_view_custom_progress);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.drawable.notification_icon);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "今日头条");
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, status);
        if (progress >= 100 || downloadThread == null) {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 0, 0, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.GONE);
        } else {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, progress, false);
            mRemoteViews.setViewVisibility(R.id.custom_progressbar, View.VISIBLE);
        }
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent(0))
                .setTicker("头条更新");
        Notification nitify = mBuilder.build();
        nitify.contentView = mRemoteViews;
        mNotificationManager.notify(notifyId, nitify);
    }

    /**
     * 开始下载
     */
    public void startDownloadNotify() {
        isPause = false;
        if (downloadThread != null && downloadThread.isAlive()) {
            // downloadThread.start();
        } else {
            downloadThread = new DownloadThread();
            downloadThread.start();
        }
    }

    /**
     * 暂停下载
     */
    public void pauseDownloadNotify() {
        isPause = true;
        if (!isCustom) {
            mBuilder.setContentTitle("已暂停");
            setNotify(progress);
        } else {
            showCustomProgressNotify("已暂停");
        }
    }

    /**
     * 取消下载
     */
    public void stopDownloadNotify() {
        if (downloadThread != null) {
            downloadThread.interrupt();
        }
        downloadThread = null;
        if (!isCustom) {
            mBuilder.setContentTitle("下载已取消").setProgress(0, 0, false);
            mNotificationManager.notify(notifyId, mBuilder.build());
        } else {
            showCustomProgressNotify("下载已取消");
        }
    }

    /**
     * 设置下载进度
     */
    public void setNotify(int progress) {
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * 下载线程
     */
    class DownloadThread extends Thread {

        @Override
        public void run() {
            int now_progress = 0;
            // Do the "lengthy" operation 20 times
            while (now_progress <= 100) {
                // Sets the progress indicator to a max value, the
                // current completion percentage, and "determinate"
                if (downloadThread == null) {
                    break;
                }
                if (!isPause) {
                    progress = now_progress;
                    if (!isCustom) {
                        mBuilder.setContentTitle("下载中");
                        if (!indeterminate) {
                            setNotify(progress);
                        }
                    } else {
                        showCustomProgressNotify("下载中");
                    }
                    now_progress += 10;
                }
                try {
                    // Sleep for 1 seconds
                    Thread.sleep(1 * 1000);
                } catch (InterruptedException e) {
                }
            }
            // When the loop is finished, updates the notification
            if (downloadThread != null) {
                if (!isCustom) {
                    mBuilder.setContentText("下载完成")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    mNotificationManager.notify(notifyId, mBuilder.build());
                } else {
                    showCustomProgressNotify("下载完成");
                }
            }
        }
    }

}
