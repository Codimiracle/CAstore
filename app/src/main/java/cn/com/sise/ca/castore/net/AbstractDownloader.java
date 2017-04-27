package cn.com.sise.ca.castore.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Codimiracle on 2017/4/11.
 */

public abstract class AbstractDownloader {

    public static final int STATUS_ERROR = -1;
    public static final int STATUS_READY = 0;
    public static final int STATUS_RUNNING = 1;
    public static final int STATUS_FINISHED = 2;
    public static final int STATUS_PAUSED = 3;
    public static final int STATUS_STOPPED = 4;


    private int status = STATUS_READY;

    protected void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public boolean isStatus(int status) {
        return this.status == status;
    }
    public boolean isConsistingIn(int... states) {
        for (int state : states) {
            if (state == this.status)
                return true;
        }
        return false;
    }
    public boolean isAvailable() {
        return this.status == STATUS_READY || this.status == STATUS_PAUSED;
    }
    public boolean isDisabled() {
        return this.status == STATUS_ERROR || this.status == STATUS_STOPPED;
    }
    public boolean isFinished() {
        return this.status == STATUS_FINISHED;
    }
    public void start() {
        setStatus(STATUS_RUNNING);
    }
    public void pause() {
        setStatus(STATUS_PAUSED);
    }
    public void stop() {
        setStatus(STATUS_STOPPED);
    }

    public void skip(InputStream input, long offset) throws IOException {
        do {
            offset -= input.skip(offset);
        } while (offset > 0);
    }

    public interface OnDownloadExceptionListener {
        public void onDownloadException();
    }
    public interface OnDownloadProgressListener {
        public void onDownloadProgress();
    }
    public interface OnDownloadRetrievableListener {
        public void onDownloadRetrievable();
    }
}
