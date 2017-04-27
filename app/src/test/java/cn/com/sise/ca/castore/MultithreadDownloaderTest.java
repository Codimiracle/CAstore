package cn.com.sise.ca.castore;

import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import cn.com.sise.ca.castore.net.AbstractDownloader;
import cn.com.sise.ca.castore.net.MultithreadDownloader;

import static org.junit.Assert.assertEquals;


/**
 * Created by Codimiracle on 2017/4/15.
 */

public class MultithreadDownloaderTest {
    @Test
    public void download_test() {
        URL url = null;
        try {
            url = new URL("http://ca.sise.com.cn:83/uploads/softs/cn.ibuka.manga.ui_33554454.apk");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File target = new File("D:/download_test.apk");
        MultithreadDownloader downloader = new MultithreadDownloader(url, target, 2);
        downloader.start();
    }

}
