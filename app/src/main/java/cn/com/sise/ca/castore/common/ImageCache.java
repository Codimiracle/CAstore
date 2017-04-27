package cn.com.sise.ca.castore.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.com.sise.ca.castore.server.Server;

/**
 * Created by Codimiracle on 2017/4/8.
 */

public class ImageCache {
    private static Map<URL, InputStream> caches;

    public static InputStream cache(URL imageURL) {
        if (caches.containsKey(imageURL)) {
            return caches.get(imageURL);
        }
        HttpURLConnection connection = null;
        try {
            connection = Server.request(imageURL);
            InputStream input = connection.getInputStream();
            if (input != null) {
                caches.put(imageURL, input);
            }
            return input;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

