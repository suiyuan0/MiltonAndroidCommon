package com.example.retrofitdemo.model;

/**
 * Created by milton on 16/11/3.
 */
public class UpdateResult {
    private AndroidAppBean android_app;

    public AndroidAppBean getAndroid_app() {
        return android_app;
    }

    public void setAndroid_app(AndroidAppBean android_app) {
        this.android_app = android_app;
    }

    @Override
    public String toString() {
        return "{\"android_app\":{\"url\":\"" + android_app.url + ",\"version\":\"" + android_app.version + "}}";
    }

    public static class AndroidAppBean {
        private String url;
        private String version;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
