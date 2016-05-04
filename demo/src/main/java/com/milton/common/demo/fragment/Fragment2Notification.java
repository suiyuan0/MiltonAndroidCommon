
package com.milton.common.demo.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.milton.common.demo.R;
import com.milton.common.util.LogUtil;
import com.milton.common.demo.activity.notification.CustomActivity;
import com.milton.common.demo.activity.notification.NotificationActivity;
import com.milton.common.demo.activity.notification.ProgressAcitivty;

import java.io.File;

public class Fragment2Notification extends Fragment2Base {
    /**
     * Notification构造器
     */
    NotificationCompat.Builder mBuilder;
    /**
     * Notification的ID
     */
    int notifyId = 100;
    /**
     * Notification管理
     */
    public NotificationManager mNotificationManager;

    public Class[] getItemClass() {
        return new Class[]{
                // UtilsScreenActivity.class,
                // UtilsContextActivity.class,
                // UtilsResourceActivity.class,
                // UtilsStringActivity.class,
                // UtilsNetActivity.class,
                // UtilsNotificationActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "显示通知栏",
                "显示大视图风格通知栏（4.1以上才生效）",
                "显示常驻通知栏",
                "显示通知，点击跳转到指定Activity",
                "显示通知，点击打开APK",
                "清除指定通知",
                "清除所有通知",
                "显示自定义通知栏",
                "显示带进度条通知栏",
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initService();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        // getActivity().startActivity(new Intent(getActivity(), mClassList[i]));
        switch (i) {
            case 0:
                showNotify();
                break;
            case 1:
                showBigStyleNotify();
                break;
            case 2:
                showCzNotify();
                break;
            case 3:
                showIntentActivityNotify();
                break;
            case 4:
                showIntentApkNotify();
                break;
            case 5:
                clearNotify(notifyId);
                break;
            case 6:
                clearAllNotify();
                break;
            case 7:
                startActivity(new Intent(getActivity(), CustomActivity.class));
                break;
            case 8:
                startActivity(new Intent(getActivity(), ProgressAcitivty.class));
                break;
            case 9:

                break;

            default:
                break;
        }
    }

    /**
     * 初始化要用到的系统服务
     */
    private void initService() {
        mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
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
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                // .setNumber(number)//显示数量
                .setTicker("测试通知来啦")// 通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher);
    }

    /**
     * 显示通知栏
     */
    public void showNotify() {
        LogUtil.e("alinmi", "showNotify", true);
        Intent intent = new Intent(getActivity(), NotificationActivity.class);
        intent.putExtra("flag", "content");
        Intent intent2 = new Intent(getActivity(), NotificationActivity.class);
        intent2.putExtra("flag", "delete");
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 1, intent,
                Notification.FLAG_AUTO_CANCEL
                // Notification.FLAG_SHOW_LIGHTS
        );
        PendingIntent deleteIntent = PendingIntent.getActivity(getActivity(), 1, intent2,
                Notification.FLAG_AUTO_CANCEL
                // Notification.FLAG_SHOW_LIGHTS
        );
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.drawable.icon_meassage_sel)// 设置通知小ICON
                .setContentTitle("测试标题")
                .setContentText("测试内容")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                // .setColor(0XFFFF0000)
                // .setContentInfo("aaaaaaaaaa")
                // .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                // .setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap())
                // .setLights(0XFF00FF00, 500, 500)
                // .setLocalOnly(true)
                // .setOngoing(true)
                // .setOnlyAlertOnce(true)
                // .setPriority(Notification.PRIORITY_MAX)
                // .setProgress(100, 50, true)
                // .setShowWhen(true)
                // .setSortKey("aa")
                // // .setSound(Uri.parse("file:///sdcard/xx/xx.mp3"))
                // // .setSound(Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "5"))
                .setVibrate(new long[]{
                        0, 100, 500, 200
                })
                // .setNumber(number)//显示数量
                .setContentIntent(contentIntent) // 设置通知栏点击意图
                .setDeleteIntent(deleteIntent)
                .setTicker("测试通知来啦")// 通知首次出现在通知栏，带上升动画效果的
        ;
        // mNotificationManager.notify(notifyId, mBuilder.build());
        Notification notification = mBuilder.build();
        // notification.icon=R.drawable.biz_news_list_tag_video;
        // notification.largeIcon = ((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap();
        mNotificationManager.notify(getResources().getString(R.string.app_name), notifyId, notification);
    }

    /**
     * 显示大视图风格通知栏
     */
    public void showBigStyleNotify() {
        mBuilder = new NotificationCompat.Builder(getActivity());
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[5];
        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("大视图内容:");
        // Moves events into the big view
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        mBuilder.setContentTitle("测试标题")
                .setContentText("测试内容")
                // .setNumber(number)//显示数量
                .setStyle(inboxStyle)// 设置风格
                .setSmallIcon(R.drawable.ic_launcher)// 设置通知小ICON
                .setTicker("测试通知来啦");// 通知首次出现在通知栏，带上升动画效果的
        mNotificationManager.notify(notifyId, mBuilder.build());
        // mNotification.notify(getResources().getString(R.string.app_name),
        // notiId, mBuilder.build());
    }

    /**
     * 显示常驻通知栏
     */
    public void showCzNotify() {

        // Notification mNotification = new Notification();//为了兼容问题，不用该方法，所以都采用BUILD方式建立
        // Notification mNotification = new Notification.Builder(this).getNotification();//这种方式已经过时
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        // //PendingIntent 跳转动作
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, getActivity().getIntent(), 0);
        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setTicker("常驻通知来了")
                .setContentTitle("常驻测试")
                .setContentText("使用cancel()方法才可以把我去掉哦")
                .setContentIntent(pendingIntent);
        Notification mNotification = mBuilder.build();
        // 设置通知 消息 图标
        mNotification.icon = R.drawable.ic_launcher;
        // 在通知栏上点击此通知后自动清除此通知
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT 在顶部常驻，可以调用下面的清除方法去除 FLAG_AUTO_CANCEL 点击和清理可以去调
        // 设置显示通知时的默认的发声、震动、Light效果
        mNotification.defaults = Notification.DEFAULT_VIBRATE;
        // 设置发出消息的内容
        mNotification.tickerText = "通知来了";
        // 设置发出通知的时间
        mNotification.when = System.currentTimeMillis();
        // mNotification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        // mNotification.setLatestEventInfo(this, "常驻测试", "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息 ,这个方法现在已经不用了
        mNotificationManager.notify(notifyId, mNotification);
    }

    /**
     * 显示通知栏点击跳转到指定Activity
     */
    public void showIntentActivityNotify() {
        mBuilder = new NotificationCompat.Builder(getActivity());
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
        // notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)// 点击后让通知将消失
                .setContentTitle("测试标题")
                .setContentText("点击跳转")
                .setTicker("点我");
        // 点击的意图ACTION是跳转到Intent
        Intent resultIntent = new Intent(getActivity(), NotificationActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * 显示通知栏点击打开Apk
     */
    public void showIntentApkNotify() {
        Notification nf = new Notification();
        nf.defaults = Notification.DEFAULT_ALL;
        mBuilder = new NotificationCompat.Builder(getActivity());
        // Notification.FLAG_ONGOING_EVENT --设置常驻 Flag;Notification.FLAG_AUTO_CANCEL 通知栏上点击此通知后自动清除此通知
        // notification.flags = Notification.FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
        mBuilder.setAutoCancel(true)// 点击后让通知将消失
                .setContentTitle("下载完成")
                .setContentText("点击安装")
                .setTicker("下载完成！");
        // 我们这里需要做的是打开一个安装包
        Intent apkIntent = new Intent();
        apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        apkIntent.setAction(Intent.ACTION_VIEW);
        // 注意：这里的这个APK是放在assets文件夹下，获取路径不能直接读取的，要通过COYP出去在读或者直接读取自己本地的PATH，这边只是做一个跳转APK，实际打不开的
        String apk_path = "file:///android_asset/cs.apk";
        // Uri uri = Uri.parse(apk_path);
        Uri uri = Uri.fromFile(new File(apk_path));
        apkIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        // context.startActivity(intent);
        PendingIntent contextIntent = PendingIntent.getActivity(getActivity(), 0, apkIntent, 0);
        mBuilder.setContentIntent(contextIntent);
        mNotificationManager.notify(notifyId, mBuilder.build());
    }
}
