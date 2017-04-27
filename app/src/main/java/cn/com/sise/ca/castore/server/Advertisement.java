package cn.com.sise.ca.castore.server;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URL;

/**
 * Created by Codimiracle on 2017/4/8.
 * {@link Advertisement} 继承 {@link Object} 实现 {@link Serializable}
 * 用于描述 广告 信息
 */

public class Advertisement implements Serializable {
    //发行者页面链接
    @SerializedName(value = "URL", alternate = {"venderURL", "vender_url"})
    private URL venderURL;
    //我依然无语，广告的名称
    @SerializedName(value = "true_name", alternate = {"advertisementName", "advertisement_name"})
    private String name;
    // 广告描述
    @SerializedName(value = "description", alternate = {"advertisementDescription", "advertisement_description"})
    private String description;
    // 广告发行者
    private String vender;
    @SerializedName(value = "directory_img", alternate = {"descriptingImageURL", "descripting_image_url"})
    // 广告描述图片
    private URL descriptingImageURL;

    public Advertisement(URL venderURL, String name, URL descriptingImageURL) {
        this.venderURL = venderURL;
        this.name = name;
        this.descriptingImageURL = descriptingImageURL;
    }

    public URL getVenderURL() {
        return venderURL;
    }

    public String getName() {
        return name;
    }

    public URL getDescriptingImageURL() {
        return descriptingImageURL;
    }

    @Override
    public String toString() {
        return "Advertisement{" +
                "venderURL='" + venderURL + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", vender='" + vender + '\'' +
                ", descriptingImageURL='" + descriptingImageURL + '\'' +
                '}';
    }
}

