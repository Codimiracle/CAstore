package cn.com.sise.ca.castore.server;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Codimiracle on 2017/4/14.
 * {@link AppDescription} 继承于 {@link Object} 实现于 {@link Serializable}
 * 用于描述 App 数据
 */

public class AppDescription implements Serializable {

    // APP 名称
    @SerializedName(value = "true_name", alternate = {"app_name", "appName"})
    private String name;
    // APP 介绍文本
    @SerializedName(value = "content", alternate = {"introductoryText"})
    private String introductoryText;
    // APP 介绍图片
    @SerializedName(value = "directory_img")
    private String iconImageURL;
    // APP 介绍图片对应下载链接
    @SerializedName(value = "introductoryImageURLs", alternate = {"introduce_image_urls"})
    private URL[] introductoryImageURLs;

    /**
     * 我搞不明白为什么不用数组。因为最后还是用数组的。
     * 所以标记为废弃。只用来Gson读取数据用
     * @deprecated
     * （怕 Gson 出错？）
     */
    @Deprecated
    @SerializedName(value = "directory_img_content_1")
    private URL introduceImageURL_1;
    @Deprecated
    @SerializedName(value = "directory_img_content_2")
    private URL introduceImageURL_2;
    @Deprecated
    @SerializedName(value = "directory_img_content_3")
    private URL introduceImageURL_3;

    // 我不知道为什么是 directory_soft，
    // APP 下载链接
    @SerializedName(value = "directory_soft", alternate = {"download_url", "downloadURL"})
    private URL downloadURL;

    public AppDescription(String name, String introductoryText, String iconImageURL, URL[] introductoryImageURLs, URL downloadURL) {
        this.name = name;
        this.introductoryText = introductoryText;
        this.iconImageURL = iconImageURL;
        this.introductoryImageURLs = introductoryImageURLs;
        this.downloadURL = downloadURL;
    }

    public String getName() {
        return name;
    }

    public String getIntroductoryText() {
        return introductoryText;
    }

    public String getIconImageURL() {
        return iconImageURL;
    }

    public URL[] getIntroductoryImageURLs() {
        if (introductoryImageURLs == null) {
            introductoryImageURLs = new URL[] {introduceImageURL_1, introduceImageURL_2, introduceImageURL_3};
        }
        return introductoryImageURLs;
    }

    public URL getDownloadURL() {
        return downloadURL;
    }

    @Override
    public String toString() {
        return "AppDescription{" +
                "name='" + name + '\'' +
                ", introductoryText='" + introductoryText + '\'' +
                ", iconImageURL='" + iconImageURL + '\'' +
                ", introductoryImageURLs=" + Arrays.toString(introductoryImageURLs) +
                ", downloadURL=" + downloadURL +
                '}';
    }
}
