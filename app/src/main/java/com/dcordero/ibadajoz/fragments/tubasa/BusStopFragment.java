package com.dcordero.ibadajoz.fragments.tubasa;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.core.models.tubasa.BusStop;
import com.dcordero.ibadajoz.core.models.tubasa.BusStopInfo;
import com.dcordero.ibadajoz.core.workers.BusesWorker;
import com.dcordero.ibadajoz.core.workers.FavoritesWorker;
import com.dcordero.ibadajoz.core.workers.WorkerCallback;

import java.util.ArrayList;

public class BusStopFragment extends Fragment {

    public static final String EXTRA_BUS_STOP = "com.dcordero.ibadajoz.extra_bus_stop";

    private static final String SAVE_INSTANCE_STATE_BUS_STOP = "com.dcordero.ibadajoz.save_instance_state_bus_stop";
    private static final String SAVE_INSTANCE_STATE_STOP_INFO = "com.dcordero.ibadajoz.save_instance_state_stop_info";
    private static final String SAVE_INSTANCE_STATE_STOP_SAVED = "com.dcordero.ibadajoz.save_instance_state_stop_saved";

    private BusStop mBusStop;
    private BusStopInfo mBusStopInfo;
    private boolean mBusStopSaved;

    public static BusStopFragment newInstance(BusStop busStop)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BUS_STOP, busStop);

        BusStopFragment fragment = new BusStopFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_busstop, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.menu_bus_stop_save_button);
        menuItem.setIcon(mBusStopSaved ?  R.drawable.save_stop_button_on : R.drawable.save_stop_button_off);
        menuItem.setTitle(mBusStopSaved ? R.string.menu_unsave_button_title : R.string.menu_save_button_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_bus_stop_save_button:
                toggleSavedBusStopFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_INSTANCE_STATE_BUS_STOP, mBusStop);
        outState.putSerializable(SAVE_INSTANCE_STATE_STOP_INFO, mBusStopInfo);
        outState.putBoolean(SAVE_INSTANCE_STATE_STOP_SAVED, mBusStopSaved);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_busstop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mBusStop = (BusStop) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE_BUS_STOP);
            mBusStopInfo = (BusStopInfo) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE_STOP_INFO);
            mBusStopSaved = savedInstanceState.getBoolean(SAVE_INSTANCE_STATE_STOP_SAVED);
        }
        else if (getArguments() != null) {
            mBusStop = (BusStop) getArguments().getSerializable(EXTRA_BUS_STOP);
        }

        setupFavoriteButton();
        setupInfoView();
        if (mBusStopInfo == null) {
            fetchStopInfo();
        }
        else {
            updateStopInfoView();
        }
    }

    private void updateBusSavedStatus(Object favorites) {
        mBusStopSaved = ((ArrayList<BusStop>) favorites).contains(mBusStop);
        getActivity().invalidateOptionsMenu();
    }
    private void toggleSavedBusStopFavorite()
    {
        FavoritesWorker.toggleFavorite(getActivity(), mBusStop, new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                updateBusSavedStatus(responseObject);
            }

            @Override
            public void failure() { }
        });
    }

    private void setupFavoriteButton()
    {
        FavoritesWorker.fetchFavorites(getActivity(), new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                updateBusSavedStatus(responseObject);
            }

            @Override
            public void failure() { }
        });
    }

    private void setupInfoView()
    {
        getView().setBackgroundResource(R.drawable.background);
        ((TextView)getView().findViewById(R.id.stop_label)).setText(mBusStop.label);

        TextView lineLabelTextView = (TextView) getView().findViewById(R.id.line_label);
        lineLabelTextView.setText(getResources().getString(R.string.line_label) + " " + mBusStop.line.label);

        View stopColorView = getView().findViewById(R.id.stop_color);
        GradientDrawable stopCircleDrawable = (GradientDrawable) stopColorView.getBackground();
        stopCircleDrawable.setColor(Color.parseColor("#" + mBusStop.line.color));
    }

    private void updateStopInfoView()
    {
        View view = getView();
        if (mBusStopInfo != null && view != null) {
            ((TextView) view.findViewById(R.id.next_stop_value)).setText(mBusStopInfo.nextTime);
        }
    }

    private void fetchStopInfo()
    {
        ((TextView) getView().findViewById(R.id.next_stop_value)).setText(R.string.loading_stop_info);

        BusesWorker.fetchStopInfo(mBusStop, new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                mBusStopInfo = (BusStopInfo) responseObject;
                updateStopInfoView();
            }

            @Override
            public void failure() {
                View view = getView();
                if (view != null) {
                    ((TextView) view.findViewById(R.id.next_stop_value)).setText(R.string.loading_stop_info_error);
                }
            }
        });
    }
}
