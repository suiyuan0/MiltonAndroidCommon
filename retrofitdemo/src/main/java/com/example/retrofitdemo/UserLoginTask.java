package com.example.retrofitdemo;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by milton on 16/11/2.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
    private final String mEmail;
    private final String mPassword;

    UserLoginTask(String email, String password) {
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        String result = "";
        try {
            // Simulate network access.

            BufferedReader in = null;
            String path = "https://qa.acscloud.honeywell.com.cn/v1/00100001/upgrade/00100001/android_app/1.0.6";
            String path3 = "https://qa.acscloud.honeywell.com.cn/v1/00100001/user/?language=zh-CN&password=qwer1234&phoneNumber=+8613816799755&phoneType=android&phoneUuid=861916033724456&pushId=e022ea878d06d29d448ca307007fdc60e84d2ad7&pushVendor=Xinge&type=LoginUser";
            String path2 = "http://localhost:8080/login/?" + "email =" + mEmail + "& password =" + mPassword;
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            InputStream inStream = conn.getInputStream();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\n" + line;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //在这里我们还要对返回的json数据进行 要主动映射到modle上
        Log.e("alinmi222", "result = " + result);
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
    }

    @Override
    protected void onCancelled() {
    }
}
