package cn.com.sise.ca.castore.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.LinearLayout;

import cn.com.sise.ca.castore.MainActivity;
import cn.com.sise.ca.castore.R;
import cn.com.sise.ca.castore.adapters.AdvertisementAdapter;
import cn.com.sise.ca.castore.server.Advertisement;
import cn.com.sise.ca.castore.server.Server;
import cn.com.sise.ca.castore.services.BackgroundService;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnHomeFragmentInteraction} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final int RELOAD_ADVERTISEMENT = 0x1010;
    public static final String ADVERTISEMENT_DATA = "AdvertisementData";

    private static HomeFragment homeFragment;
    private OnHomeFragmentInteraction mListener;
    private AdvertisementAdapter advertisementAdapter;
    private AdapterViewFlipper advertiser;
    private LinearLayout appContainer;

    private static BackgroundService.BackgroundTask ADVERTISEMENT_TASK = new BackgroundService.BackgroundTask() {
        @Override
        public void handle(Intent results) {
            results.putExtra(HomeFragment.ADVERTISEMENT_DATA, Server.requestAdvertisement());
        }
    };

    @Override
    public MainActivity getContext() {
        return (MainActivity) super.getContext();
    }

    private Handler homeFragmentInteractionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RELOAD_ADVERTISEMENT:
                    advertiser.setAdapter(advertisementAdapter);
            }
        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }
    public Handler getHandler() {
        return homeFragmentInteractionHandler;
    }
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        appContainer = (LinearLayout) v.findViewById(R.id.home_app_container);
        advertiser = (AdapterViewFlipper) v.findViewById(R.id.advertiser_view_switcher);
        advertiser.setAutoStart(true);
        advertiser.setFlipInterval(2_500);
        for (int i = 0; i < 16; i++) {
            View view = inflater.inflate(R.layout.app_description_item, container, false);
            appContainer.addView(view);
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onHomeInteraction(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteraction) {
            mListener = (OnHomeFragmentInteraction) context;
            advertisementAdapter = AdvertisementAdapter.newInstance(getContext());
            if (advertisementAdapter.getCount() < 1) {
                getContext().addBackgroundTaskAccomplishedListener(ADVERTISEMENT_TASK.getTaskID(), new BackgroundService.BackgroundTaskAccomplishedReceiver.BackgroundTaskAccomplishedListener() {
                    @Override
                    public void onBackgroundTaskFinished(Context context, Intent results) {
                        Advertisement[] advertisements = (Advertisement[])  results.getSerializableExtra(HomeFragment.ADVERTISEMENT_DATA);
                        HomeFragment homeFragment = HomeFragment.newInstance();
                        advertisementAdapter.setAdvertisements(advertisements);
                        homeFragmentInteractionHandler.sendEmptyMessage(RELOAD_ADVERTISEMENT);
                    }
                });
                getContext().startBackgroundTask(ADVERTISEMENT_TASK);
            }
            homeFragmentInteractionHandler.sendEmptyMessage(RELOAD_ADVERTISEMENT);
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteraction");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnHomeFragmentInteraction {
        void onHomeFragmentInteraction(Uri uri);
    }
}
