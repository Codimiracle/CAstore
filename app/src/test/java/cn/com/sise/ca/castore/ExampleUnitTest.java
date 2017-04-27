package cn.com.sise.ca.castore;

import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import cn.com.sise.ca.castore.server.Advertisement;
import cn.com.sise.ca.castore.server.AppDescription;
import cn.com.sise.ca.castore.server.Server;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void server_request_reponse_content() {
        Advertisement[] a = Server.requestAdvertisement();
        System.out.println(Arrays.deepToString(a));
        try {
            AppDescription[] ad = Server.requestAppDescriptions("2081 ", "", "", "");
            ad[0].getIntroductoryImageURLs();
            System.out.println(Arrays.deepToString(ad));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}