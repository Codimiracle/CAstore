package cn.com.sise.ca.castore.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Queue;

import cn.com.sise.ca.castore.net.MultithreadDownloader;
import cn.com.sise.ca.castore.server.AppDescription;

public class DownloadService extends Service {
    private Queue<AppDescription> appDescriptionQueue;
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void handleAppDescription() {

    }
}
