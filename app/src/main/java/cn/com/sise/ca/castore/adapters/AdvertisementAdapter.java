package cn.com.sise.ca.castore.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;

import cn.com.sise.ca.castore.MainActivity;
import cn.com.sise.ca.castore.R;
import cn.com.sise.ca.castore.server.Advertisement;
import cn.com.sise.ca.castore.server.Server;
import cn.com.sise.ca.castore.services.BackgroundService;

/**
 * Created by Codimiracle on 2017/4/22.
 */

public class AdvertisementAdapter extends BaseAdapter {
    private static final String ADVERTISEMENT_IMAGE_CACHE = "AdvertisementImageCache";
    private static AdvertisementAdapter instance;

    private Advertisement[] advertisements = new Advertisement[0];
    private Drawable[] advertisementDrawables;
    private Context context;
    private AdvertisementAdapter(Context context) {
        this.context = context;
    }
    private BackgroundService.BackgroundTask ADVERTISEMENT_IMAGE_CACHE_TASK = new BackgroundService.BackgroundTask() {
        @Override
        public void handle(Intent results) {
            HttpURLConnection connection = null;
            advertisementDrawables = new Drawable[advertisements.length];
            for (int i = 0; i < advertisements.length; i++) {
                Advertisement advertisement = advertisements[i];
                try {
                    connection = Server.request(advertisement.getDescriptingImageURL());
                    Drawable drawable = Drawable.createFromStream(connection.getInputStream(), advertisement.getName());
                    advertisementDrawables[i] = drawable;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }
    };
    public static AdvertisementAdapter newInstance(Context context) {
        if (instance != null) {
            instance.context = context;
            return instance;
        }
        instance = new AdvertisementAdapter(context);
        return instance;
    }

    public MainActivity getContext() {
        return (MainActivity) this.context;
    }
    public Advertisement[] getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(Advertisement[] advertisements) {
        this.advertisements = advertisements;
        getContext().startBackgroundTask(ADVERTISEMENT_IMAGE_CACHE_TASK);
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return advertisements.length;
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Advertisement getItem(int position) {
        return advertisements[position];
    }

    private void cacheAdvertisementImage() {

    }
    /**
     * 获取 广告 的图片
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The Drawable at the specified position.
     */
    public Drawable getAdvertisementDrawable(int position) {
        if (advertisementDrawables == null) {
            return null;
        }
        return advertisementDrawables[position];
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position * hashCode();
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Advertisement advertisement = getItem(position);
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(context);
        }
        Drawable drawable = getAdvertisementDrawable(position);
        if (drawable == null) {
            drawable = getContext().getResources().getDrawable(R.mipmap.progressing);
        }
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setImageDrawable(drawable);
        return view;
    }
}
