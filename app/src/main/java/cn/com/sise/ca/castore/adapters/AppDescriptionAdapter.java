package cn.com.sise.ca.castore.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import cn.com.sise.ca.castore.server.AppDescription;
import cn.com.sise.ca.castore.server.Server;

/**
 * Created by Codimiracle on 2017/4/17.
 */

public class AppDescriptionAdapter extends BaseAdapter {
    AppDescription[] appDescriptions;
    public AppDescriptionAdapter() {
    }
    @Override
    public int getCount() {
        return appDescriptions.length;
    }

    @Override
    public AppDescription getItem(int position) {
        return appDescriptions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppDescription appDescription = getItem(position);

        return null;
    }
}
