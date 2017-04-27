package cn.com.sise.ca.castore.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.os.Binder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v4.util.ArrayMap;
import java.util.Map;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * {@link BackgroundService} Service 用于处理程序中较为容易花费时间和微型的任务。
 */
public class BackgroundService extends IntentService {
    public static final String BACKGROUND_TASK_DATA_KEY = "BackgroundTaskDataKey";
    public static final String BACKGROUND_TASK_RECEIVER_ACTION = "cn.com.sise.ca.action.BackgroundTaskReceiverAction";
    public static final String BACKGROUND_TASK_FINISHED_TASK_ID = "BACKGROUND_TASK_FINISHED_TASK_ID";

    /**
     * 获得任务结束后传递的意图
     * @param taskID
     * @return {@link Intent}
     */
    public static Intent obtainBackgroundTaskAccomplishedIntent(long taskID) {
        Intent intent = new Intent(BackgroundService.BACKGROUND_TASK_RECEIVER_ACTION);
        intent.putExtra(BACKGROUND_TASK_FINISHED_TASK_ID, taskID);
        return intent;
    }
    /**
     * 获得已经封装好的 任务意图
     * @param context 启动后台任务的上下文
     * @param task 后台任务，BackgroundService$BackgroundTask 实例对象
     * @return {@link Intent}
     */
    public static Intent obtainBackgroundTaskLauncherIntent(Context context, BackgroundService.BackgroundTask task) {
        Intent intent = new Intent(context, BackgroundService.class);
        Bundle bundle = new Bundle();
        bundle.putBinder(BackgroundService.BACKGROUND_TASK_DATA_KEY, task);
        intent.putExtras(bundle);
        intent.putExtra(BACKGROUND_TASK_FINISHED_TASK_ID, task.getTaskID());
        return intent;
    }

    /**
     * 必须要定义的 零参数 构造器
     */
    public BackgroundService() {
        super("BackgroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            BackgroundTask task = (BackgroundTask) intent.getExtras().getBinder(BACKGROUND_TASK_DATA_KEY);
            Intent results = obtainBackgroundTaskAccomplishedIntent(task.getTaskID());
            task.handle(results);
            getBaseContext().sendBroadcast(results);
        }
    }

    /**
     * 我们需要业务代码，但不需要单独线程
     * 所以并没有继承类似于 <p>{@link Thread}/p> 的类，我们使用了 {@link IntentService}
     * 所以 简单地传入业务代码即可 。
     */
    public abstract static class BackgroundTask extends Binder {
        private long id = -1;

        /**
         * 获得任务ID
         * @return 任务的ID
         */
        public long getTaskID() {
            if (id == -1) {
                id = hashCode(); //直接保存 hashCode 作为 ID
                                 // 拷贝 BackgroundTask 对象不会影响到任务的ID
            }
            return id;
        }

        /**
         * 处理业务代码
         * @param results 处理业务代码后需要返回的数据 Intent
         */
        public abstract void handle(Intent results);
    }

    /**
     * 一个由 {@link BroadcastReceiver} 实现的 Receiver
     * 用于处理 BackgroundTask 完成后与 上下文（Context）的交互
     */
    public static class BackgroundTaskAccomplishedReceiver extends BroadcastReceiver {
        private Map<Long, BackgroundTaskAccomplishedListener> attachingListener = new ArrayMap<Long, BackgroundTaskAccomplishedListener>();
        /**
         * 添加一个任务侦听器
         * @param taskID 任务 ID
         * @param listener 完成后的侦听器
         */
        public void addBackgroundTaskAccomplishedListener(long taskID, @NonNull BackgroundTaskAccomplishedListener listener) {
            attachingListener.put(taskID, listener);
        }
        /**
         * 获取 任务ID 对应的 侦听器
         * @param taskID 任务ID
         * @return {@link BackgroundTaskAccomplishedListener}
         */
        public BackgroundTaskAccomplishedListener getBackgroundTaskAccomplishedListener(long taskID) {
            return attachingListener.get(taskID);
        }
        /**
         * 请不要覆盖 onReceive 方法，如果需要完成后的处理，应使用 {@link BackgroundTaskAccomplishedListener#onBackgroundTaskFinished}
         * @param context Receiver 注册 Context
         * @param results BackgroundTask 执行后的结果
         */
        @Override
        public void onReceive(Context context, Intent results) {
            long taskID = results.getLongExtra(BACKGROUND_TASK_FINISHED_TASK_ID, -1);
            if (attachingListener.containsKey(taskID)) {
                attachingListener.get(taskID).onBackgroundTaskFinished(context, results);
                attachingListener.remove(taskID);
            }
        }

        public interface BackgroundTaskAccomplishedListener {
            void onBackgroundTaskFinished(Context context, Intent results);
        }

    }
}
