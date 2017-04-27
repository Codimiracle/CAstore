package cn.com.sise.ca.castore.common;

import com.google.gson.Gson;

/**
 * Created by Codimiracle on 2017/4/15.
 */

public class SingletonGson {
    private static Gson singletonGson;
    public static Gson getGson() {
        if (singletonGson == null) {
            singletonGson = new Gson();
        }
        return singletonGson;
    }
}
