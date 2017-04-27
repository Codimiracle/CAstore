package cn.com.sise.ca.castore.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Codimiracle on 2017/4/10.
 */

public class MultithreadDownloader extends AbstractDownloader {
    private long length;
    private long downloadedLength = 0;
    private int threadNumber;
    private URL url;
    private File target;
    private DownloadThread[] threads;
    private HttpURLConnection connection;
    private OnDownloadProgressListener onDownloadProgressListener;

    public MultithreadDownloader(URL url, File target, int threadNumber) {
        this.url = url;
        this.target = target;
        this.threadNumber = threadNumber;
    }

    public OnDownloadProgressListener getOnDownloadProgressListener() {
        return onDownloadProgressListener;
    }

    public void setOnDownloadProgressListener(OnDownloadProgressListener onDownloadProgressListener) {
        this.onDownloadProgressListener = onDownloadProgressListener;
    }

    private void init() throws IOException {
        if (threads != null) {
            for (DownloadThread dt : threads) {
                dt.start();
            }
            return;
        }
        connection = (HttpURLConnection) url.openConnection();
        length = connection.getContentLength();
        RandomAccessFile raf = new RandomAccessFile(target, "rw");
        raf.setLength(length);
        raf.close();
        long partition_size = connection.getContentLength() / threadNumber;
        long addtional_size = length % threadNumber;
        threads = new DownloadThread[threadNumber];
        for (int i = 0; i < threadNumber; i++) {
            threads[i] = new DownloadThread(url, target, partition_size * i, partition_size + ((threadNumber == i + 1) ? addtional_size : 0));
        }
    }
    @Override
    public void start() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.start();
    }
    public long downloadedLength() {
        return downloadedLength;
    }
    public long length() {
        return length;
    }
    public float getProgressPercentage() {
        return (float) (downloadedLength / (double) length);
    }
    private class DownloadThread extends AbstractDownloader{
        private final Object[] lock = new Object[0]; //多线程加锁
        private final Runnable downloadRunnable = new Runnable() {
            @Override
            public void run() {
                InputStream input = null;
                RandomAccessFile raf = null;
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    input = connection.getInputStream();
                    raf = new RandomAccessFile(target, "rw");
                    skip(input, offset + writtenLength);
                    raf.seek(offset + writtenLength);
                    byte[] buffer = new byte[4096];
                    int read = 0;
                    while (writtenLength < length) {
                        read = input.read(buffer);
                        writtenLength += read;
                        if (writtenLength > length) {
                            downloadedLength += (read - (writtenLength - length));
                            writtenLength = length;
                        } else {
                            downloadedLength += read;
                        }
                        raf.write(buffer,0, read);
                        if (isStatus(STATUS_PAUSED) || isStatus(STATUS_STOPPED))
                            break;
                    }
                } catch (IOException e) {
                    setStatus(STATUS_ERROR);
                    e.printStackTrace();
                } finally {
                    try {
                        if (raf != null)
                            raf.close();
                        if (input != null)
                            input.close();
                        if (connection != null) {
                            connection.disconnect();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        private URL url;
        private File target;
        private long offset;
        private long length;
        private long writtenLength = 0;
        private Thread thread;

        public DownloadThread(URL url, File target, long offset, long length) {
            this.url = url;
            this.target = target;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public void start() {
            synchronized (lock) {
                super.start();
                thread = new Thread(downloadRunnable);
                thread.run();
            }

        }



    }
}
