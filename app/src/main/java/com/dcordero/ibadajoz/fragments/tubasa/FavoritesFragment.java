package com.dcordero.ibadajoz.fragments.tubasa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.activities.tubasa.BusStopActivity;
import com.dcordero.ibadajoz.core.models.tubasa.BusStop;
import com.dcordero.ibadajoz.core.workers.FavoritesWorker;
import com.dcordero.ibadajoz.core.workers.WorkerCallback;

import java.util.ArrayList;

public class FavoritesFragment extends ListFragment {

    private ArrayList<BusStop> mFavorites = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundResource(R.drawable.background);
        setEmptyText(getResources().getString(R.string.no_favorites));
        setupAdapter();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        BusStop favoriteStop = (BusStop) getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), BusStopActivity.class);
        intent.putExtra(BusStopFragment.EXTRA_BUS_STOP, favoriteStop);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchFavorites();
    }

    private void setupAdapter()
    {
        setListAdapter(new FavoritesAdapter(mFavorites));
    }

    private void fetchFavorites()
    {
        FavoritesWorker.fetchFavorites(getActivity(), new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                mFavorites.clear();
                mFavorites.addAll((ArrayList<BusStop>) responseObject);
                ((FavoritesAdapter) getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void failure() {

            }
        });
    }

    private class FavoritesAdapter extends ArrayAdapter<BusStop>
    {

        public FavoritesAdapter(ArrayList<BusStop> favorites) {
            super(getActivity(), 0, favorites);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_lines, null);
            }

            BusStop favoriteStop = getItem(position);

            TextView lineNumberTextView = (TextView) convertView.findViewById(R.id.line_number);
            lineNumberTextView.setText(getResources().getString(R.string.line_label) + " " + favoriteStop.line.label);

            TextView lineLabelTextView = (TextView) convertView.findViewById(R.id.line_label);
            lineLabelTextView.setText(favoriteStop.label);

            View lineColorView = convertView.findViewById(R.id.line_color);
            GradientDrawable lineCircleDrawable = (GradientDrawable) lineColorView.getBackground();
            lineCircleDrawable.setColor(Color.parseColor("#" + favoriteStop.line.color));

            return convertView;
        }
    }
}
