package cn.com.sise.ca.castore.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.sise.ca.castore.server.AppDescription;

/**
 * Created by Codimiracle on 2017/4/19.
 */

public class AppDescriptionView extends FrameLayout {

    private AppDescription appDescription;
    private ImageView app_icon;
    private TextView app_name;
    private TextView app_size;
    private TextView app_description;

    public AppDescriptionView(@NonNull Context context) {
        super(context);
    }

    public AppDescriptionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AppDescriptionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppDescriptionView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init_content_view() {

    }

    public void setAppDescriptionView(AppDescription appDescription) {
        this.appDescription = appDescription;
    }



}
