package cn.com.sise.ca.castore;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import cn.com.sise.ca.castore.server.Server;

/**
 * Created by Codimiracle on 2017/4/16.
 */

public class ServerTest {
    @Test
    public void server_timeout_test() {
        try {
            String s = Server.requestResponseContent("http://ca.sise.com.cn/");
            System.out.println(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void server_appd_test() {
        try {
            Server.requestAppDescriptions("2081","","","GET");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
