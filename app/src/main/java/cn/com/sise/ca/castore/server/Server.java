package cn.com.sise.ca.castore.server;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import cn.com.sise.ca.castore.common.SingletonGson;

/**
 * Created by Codimiracle on 2017/4/7.
 * Server 继承于 {@link Object} 用于和服务器进行数据交接
 */


public class Server {
    public static final String HOST = "http://ca.sise.com.cn";
    public static final short PORT = 83;
    public static final String APPDETAILS_REQUEST_URL = HOST + ":" + PORT + "/index.php/admin/json_download?id=%s&category=%s&offset=%s&method=%s";
    public static final String ADVERTISEMENT_REQUEST_URL = HOST + ":" + PORT + "/index.php/admin/json_download?advertisement=1";

    /**
     * 向服务器发出请求。
     * @param address 请求地址
     * @param requestProperty 请求属性,例如 Content-Type
     * @param method 请求方法
     * @return 如果没有问题则返回 HttpURLConnection 否则返回 null。
     * @throws IOException IOException
     */
    public static HttpURLConnection request(@NonNull URL address, Map<String, String> requestProperty, @NonNull String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) address.openConnection();
        connection.setRequestMethod(method);
        if (requestProperty != null) {
            for (String key : requestProperty.keySet()) {
                connection.setRequestProperty(key, requestProperty.get(key));
            }
        }
        connection.setConnectTimeout(3_000);
        connection.connect();
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 304) {
            return connection;
        }
        return null;
    }

    /**
     * 向服务器发出请求。
     * @param address 请求地址
     * @return 如果没有问题则返回 HttpURLConnection 否则返回 null。
     * @throws IOException IOException
     */
    public static HttpURLConnection request(@NonNull String address) throws IOException {
        return request(new URL(address));
    }

    /**
     * 向服务器发出请求。
     * @param address 请求地址
     * @return 如果没有问题则返回 HttpURLConnection 否则返回 null。
     * @throws IOException IOException
     */
    public static HttpURLConnection request(@NonNull URL address) throws IOException {
        return request(address, null, "GET");
    }

    /**
     * 直接获取服务器响应的文本。
     * @param address 请求地址。
     * @return 如果没有问题则返回 String 否则返回 null。
     */
    public static String requestResponseContent(@NonNull URL address) {
        return requestResponseContent(address, Charset.defaultCharset());
    }

    /**
     * 直接获取服务器响应的文本。
     * @param address 请求地址。
     * @return 返回获取到的文本
     * @throws MalformedURLException 地址不正确
     */
    public static String requestResponseContent(@NonNull String address) throws MalformedURLException {
        return requestResponseContent(new URL(address), Charset.defaultCharset());
    }

    /**
     * 直接向服务器请求响应的文本。
     * @param   address  请求地址。
     * @param   charset 请求内容的字符集
     * @return  如果没有问题则返回 String 否则返回 null。
     */
    public static String requestResponseContent(@NonNull String address, @NonNull Charset charset) throws MalformedURLException {
        return requestResponseContent(new URL(address), charset);
    }

    public static String requestResponseContent(@NonNull URL address, @NonNull Charset charset) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            connection = request(address);
            if (connection == null) {
                return null;
            }
            StringBuffer stringBuffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                stringBuffer.append(s);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 请求 广告
     * @return 如果没有问题则返回 Advertisement[] 否则返回 空数组。
     */
    public static Advertisement[] requestAdvertisement() {
        String json = null;
        try {
            json = requestResponseContent(ADVERTISEMENT_REQUEST_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (json != null) {
            Gson gson = SingletonGson.getGson();
            return gson.fromJson(json, Advertisement[].class);
        }
        return new Advertisement[0];
    }
    /**
     * 请求 APP 数据
     * @param id APP 的 id
     * @return 如果没有问题则返回 AppDescription 否则返回 null。
     */
    public static AppDescription requestAppDescriptionById(String id) throws IOException {
        AppDescription[] a = requestAppDescriptions(id,"","","");
        if (a.length > 0) {
            return a[0];
        }else {
            return null;
        }
    }
    /**
     * 请求 APP 数据
     * @param   category 从类型返回 多个APP。
     * @param   pageIndex 大概是页索引
     * @return  如果没有问题则返回 AppDescription[] 否则返回 空数组。
     */
    public static AppDescription[] requestAppDescriptionByCategory(String category,int pageIndex) throws IOException {
        return requestAppDescriptions("", category,"" + pageIndex,"");
    }
    /**
     * 请求 APP 数据
     * @param   id  APP id
     * @param   category 从类型返回 多个APP。
     * @param   pageIndex 大概是页索引
     * @param   method 我也不知道
     * @return  如果没有问题则返回 AppDescription[] 否则返回 空数组。
     */
    public static AppDescription[] requestAppDescriptions(String id, String category, String pageIndex, String method) throws IOException {
        URL appDescriptionURL = new URL(String.format(APPDETAILS_REQUEST_URL, id, category, pageIndex, method));
        String json = requestResponseContent(appDescriptionURL);
        if (json != null) {
            Gson gson = SingletonGson.getGson();
            return gson.fromJson(json, AppDescription[].class);
        }
        return new AppDescription[0];
    }
}
