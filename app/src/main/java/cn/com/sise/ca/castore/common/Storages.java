package cn.com.sise.ca.castore.common;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codimiracle on 2016/10/23.
 *
 * 获取 Android 设备的 Storage 位置。
 * （基于反射）
 */
public class Storages {
    StorageManager storageManager;
    List<StorageItem> list = new ArrayList<StorageItem>();
    public Storages(Context context) {
        storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        Class<StorageManager> storageManagerClass = (Class<StorageManager>) storageManager.getClass();
        Method getVolumePaths = null, getVolumeState = null;
        try {
            getVolumePaths = storageManagerClass.getMethod("getVolumePaths", new Class[0]);
            getVolumePaths.setAccessible(true);
            getVolumeState = storageManagerClass.getMethod("getVolumeState", new Class[]{String.class});
            getVolumeState.setAccessible(true);
        } catch (NoSuchMethodException e) { e.printStackTrace(); }
        try {
            Object[] temp = (Object[]) getVolumePaths.invoke(storageManager, new Object[0]);
            for(Object o : temp) {
                String mountPoint = o.toString();
                StorageItem si = new StorageItem(new File(mountPoint), (String) getVolumeState.invoke(storageManager,new Object[]{ mountPoint }));
                list.add(si);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public  List<StorageItem> getStoragesList() {
        return list;
    }
    public List<StorageItem> getAvailableStorageList() {
        List<StorageItem> temp = new ArrayList<StorageItem>();
        for(StorageItem item : list)
            if (item.isAvailable()) temp.add(item);
        return temp;
    }
    public  StorageItem[] getStorages() {
        return list.toArray(new StorageItem[list.size()]);
    }
    public  StorageItem[] getAvailableStorages() {
        List<StorageItem> temp = getAvailableStorageList();
        return temp.toArray(new StorageItem[temp.size()]);
    }
    public class StorageItem {
        public static final String MEDIA_USB_STORAGE = "USB STORAGE";
        public static final String MEDIA_DEVICE_STORAGE = "DEVICE_STORAGE";
        public static final String MEDIA_SDCARD_STORAGE = "SDCARD_STORAGE";

        private File mountPath;
        private String state;
        public StorageItem(File mountPath, String state) {
            this.mountPath = mountPath;
            this.state = state;
        }

        public boolean isAvailable() {
            return state.equals(Environment.MEDIA_MOUNTED);
        }
        public String getMediaName() {
            String name = mountPath.getName().toLowerCase();
            if(name.contains("usb"))
                return MEDIA_USB_STORAGE;
            else if (name.equals("0") || name.contains("sdcard0"))
                return MEDIA_DEVICE_STORAGE;
            else
                return MEDIA_SDCARD_STORAGE;
        }
        public String getMountPath() {
            return mountPath.getAbsolutePath();
        }

        public File getMountPathFile() {
            return mountPath;
        }
    }
}
