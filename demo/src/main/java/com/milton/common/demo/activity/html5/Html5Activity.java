
package com.milton.common.demo.activity.html5;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.milton.common.activity.BaseActivity;
import com.milton.common.demo.R;

public class Html5Activity extends BaseActivity {
    private WebView mWebView;

    private WebViewClient mWebViewClient = new WebViewClient() {
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // switch (errorCode)
            // {
            // case HttpStatus.SC_NOT_FOUND:
            view.loadUrl("file:///android_assets/error_handle.html");
            // break;
            // }
        }

        // 处理页面导航
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e("alinmi2", "WebViewClient shouldOverrideUrlLoading url = " + url);
            mWebView.loadUrl(url);
            // 记得消耗掉这个事件。给不知道的朋友再解释一下，
            // Android中返回True的意思就是到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("alinmi2", "WebViewClient onPageFinished url = " + url);
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.e("alinmi2", "WebViewClient onPageStarted url = " + url);
            super.onPageStarted(view, url, favicon);
        }
    };
    private WebChromeClient mChromeClient = new WebChromeClient() {

        private View myView = null;
        private CustomViewCallback myCallback = null;

        // 配置权限 （在WebChromeClinet中实现）
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                GeolocationPermissions.Callback callback) {
            Log.e("alinmi2", "WebChromeClient onGeolocationPermissionsShowPrompt origin = " + origin);
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        // 扩充数据库的容量（在WebChromeClinet中实现）
        @Override
        public void onExceededDatabaseQuota(String url,
                String databaseIdentifier, long currentQuota,
                long estimatedSize, long totalUsedQuota,
                WebStorage.QuotaUpdater quotaUpdater) {
            Log.e("alinmi2", "WebChromeClient onExceededDatabaseQuota url = " + url);
            quotaUpdater.updateQuota(estimatedSize * 2);
        }

        // 扩充缓存的容量
        @Override
        public void onReachedMaxAppCacheSize(long spaceNeeded,
                long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            Log.e("alinmi2", "WebChromeClient onReachedMaxAppCacheSize spaceNeeded = " + spaceNeeded);
            quotaUpdater.updateQuota(spaceNeeded * 2);
        }

        // Android 使WebView支持HTML5 Video（全屏）播放的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Log.e("alinmi2", "WebChromeClient onShowCustomView");
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }

            ViewGroup parent = (ViewGroup) mWebView.getParent();
            parent.removeView(mWebView);
            parent.addView(view);
            myView = view;
            myCallback = callback;
            mChromeClient = this;
        }

        @Override
        public void onHideCustomView() {
            Log.e("alinmi2", "WebChromeClient onHideCustomView ");
            if (myView != null) {
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }

                ViewGroup parent = (ViewGroup) myView.getParent();
                parent.removeView(myView);
                parent.addView(mWebView);
                myView = null;
            }
        }
    };

    @Override
    public void initView() {
        initSettings();

        // mWebView.loadUrl("http://www.baidu.com");
        mWebView.loadUrl("http://blog.csdn.net/lmj623565791/article/details/39102591");
        Log.e("alinmi2", " mWebView.loadUrl");
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initSettings() {

        // requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置标题栏样式
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏

        setContentView(R.layout.activity_html5);
        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        // 开启Javascript脚本
        webSettings.setJavaScriptEnabled(true);

        // 启用localStorage 和 essionStorage
        webSettings.setDomStorageEnabled(true);

        // 开启应用程序缓存
        webSettings.setAppCacheEnabled(true);
        String appCacheDir = this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCacheDir);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小，我设的是10M
        webSettings.setAllowFileAccess(true);

        // 启用Webdatabase数据库
        webSettings.setDatabaseEnabled(true);
        String databaseDir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(databaseDir);// 设置数据库路径

        // 启用地理定位
        webSettings.setGeolocationEnabled(true);
        // 设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(databaseDir);

        // 开启插件（对flash的支持）
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setRenderPriority(RenderPriority.HIGH);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.setWebChromeClient(mChromeClient);
        mWebView.setWebViewClient(mWebViewClient);
    }

    // 浏览网页历史记录
    // goBack()和goForward()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("alinmi2", "onKeyDown keyCode = " + keyCode + ", mWebView.canGoBack() = " + mWebView.canGoBack());
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
