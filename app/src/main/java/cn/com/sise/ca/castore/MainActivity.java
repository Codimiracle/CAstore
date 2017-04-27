package cn.com.sise.ca.castore;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import cn.com.sise.ca.castore.fragments.CategoriesFragment;
import cn.com.sise.ca.castore.fragments.HomeFragment;
import cn.com.sise.ca.castore.services.BackgroundService;
import cn.com.sise.ca.castore.services.BackgroundService.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnHomeFragmentInteraction,
                    CategoriesFragment.OnCategoriesInteractionListener{
    private static final int LOGO_SHOWING_TIME = 1000;
    private static final int DOUBLE_CLICK_EXIT_INTERVAL = LOGO_SHOWING_TIME;

    private static final int MESSAGE_DOUBLE_BACK_EXIT = 0x100;
    private static final int MESSAGE_LOGO_DISPLAY_FINISHED = 0x200;

    // TAGs
    private static final int TAGS_RECONMMEND_FRAGMENT= 0;
    private static final int TAGS_RANDOM_FRAGMENT= 1;
    private static final int TAGS_CATEGORIES_FRAGMENT= 2;
    private static final int TAGS_SETTINGS_FRAGMENT= 3;

    private Fragment currentFragment;
    private int lastSelectedPosition = 0;

    private Handler interactionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DOUBLE_BACK_EXIT:
                    backPressedCount = 0;
                    break;
                case MESSAGE_LOGO_DISPLAY_FINISHED:
                    initialize();
                    break;
            }
        }
    };
    private byte backPressedCount = 2; // 方便用户在 LOGO 画面退出
    private FragmentManager fragmentManager;
    private BackgroundTaskAccomplishedReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        interactionHandler.sendEmptyMessageDelayed(MESSAGE_LOGO_DISPLAY_FINISHED, LOGO_SHOWING_TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void initialize() {
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init_bottom_navigation(); //初始化底部栏

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        backPressedCount = 0;
        currentFragment = new HomeFragment();
        replaceFragment(currentFragment);
        receiver = new BackgroundTaskAccomplishedReceiver();
        registerReceiver(receiver, new IntentFilter(BackgroundService.BACKGROUND_TASK_RECEIVER_ACTION));
    }
    public BackgroundTaskAccomplishedReceiver getBackgroundTaskAccomplishedReceiver() {
        return this.receiver;
    }

    private void init_bottom_navigation() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar
                .setActiveColor(R.color.colorTagsButtonActive)
                .setInActiveColor(R.color.colorTagsButtonInActive)
                .setBarBackgroundColor(R.color.colorPrimaryDark);
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                replaceFragment(getMainActivityFragment(position));
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.recommend, R.string.app_tags_recommend))
                .addItem(new BottomNavigationItem(R.mipmap.random_app,R.string.app_tags_lookat))
                .addItem(new BottomNavigationItem(R.mipmap.categories, R.string.app_tags_categories))
                .addItem(new BottomNavigationItem(R.mipmap.settings, R.string.app_tags_settings))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();
    }

    /**
     * 与
     *  {@link cn.com.sise.ca.castore.services.BackgroundService.BackgroundTaskAccomplishedReceiver.BackgroundTaskAccomplishedListener}
     * 的功能一致
     * @param taskID 后台任务ID
     * @param listener 侦听器
     */
    public void addBackgroundTaskAccomplishedListener(long taskID, BackgroundTaskAccomplishedReceiver.BackgroundTaskAccomplishedListener listener) {
        receiver.addBackgroundTaskAccomplishedListener(taskID, listener);
    }

    /**
     * 开始 后台任务 。
     * @param task 后台处理的任务
     */
    public void startBackgroundTask(BackgroundTask task) {
        Intent intent = BackgroundService.obtainBackgroundTaskLauncherIntent(MainActivity.this, task);
        startService(intent);
    }

    public Fragment getMainActivityFragment(int position) {
        switch (position) {
            case TAGS_RECONMMEND_FRAGMENT:
                return HomeFragment.newInstance();
            case TAGS_RANDOM_FRAGMENT:
                return null;
            case TAGS_CATEGORIES_FRAGMENT:
                return CategoriesFragment.newInstance();
            case TAGS_SETTINGS_FRAGMENT:
                return null;
            default:
                return HomeFragment.newInstance();
        }
    }
    public void replaceFragment(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.app_fragment_container, fragment)
                .commit();
    }

    /*
     * Home Page
     */
    @Override
    public void onHomeFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCategoriesFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressedCount++;
            if (backPressedCount >= 2) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, R.string.app_double_back, Toast.LENGTH_SHORT).show();
                interactionHandler.sendEmptyMessageDelayed(MESSAGE_DOUBLE_BACK_EXIT, DOUBLE_CLICK_EXIT_INTERVAL);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
