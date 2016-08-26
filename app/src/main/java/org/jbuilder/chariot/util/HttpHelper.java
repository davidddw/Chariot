package org.jbuilder.chariot.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by d05660ddw on 16/8/26.
 */
public class HttpHelper {

    public String HttpGet(String remoteUrl, String auth) {
        InputStream inputStream = null;
        String result = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            URL url = new URL(remoteUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            if (auth != null) {
                conn.setRequestProperty("Authorization", auth);
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                result = new String(outputStream.toByteArray());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String HttpPost(String remoteUrl, Map<String, String> params, String auth) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String result = "";
        StringBuffer paramBuffer = new StringBuffer();

        try {
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entity : params.entrySet()) {
                    paramBuffer.append(entity.getKey()).append("=")
                            .append(URLEncoder.encode(entity.getValue(), "utf-8")).append("&");

                }
                paramBuffer.deleteCharAt(paramBuffer.length() - 1);
            }
            byte[] mydata = paramBuffer.toString().getBytes();
            URL url = new URL(remoteUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(mydata.length));
            if (auth != null) {
                conn.setRequestProperty("Authorization", auth);
            }
            outputStream = conn.getOutputStream();
            outputStream.write(mydata);
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
                int len;
                byte[] buffer = new byte[1024];
                if (inputStream != null) {
                    while ((len = inputStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    result = new String(byteArrayOutputStream.toByteArray());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}
