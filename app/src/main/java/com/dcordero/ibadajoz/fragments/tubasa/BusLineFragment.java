package com.dcordero.ibadajoz.fragments.tubasa;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.applidium.headerlistview.HeaderListView;
import com.applidium.headerlistview.SectionAdapter;
import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.activities.tubasa.BusStopActivity;
import com.dcordero.ibadajoz.core.models.tubasa.BusLine;
import com.dcordero.ibadajoz.core.models.tubasa.BusStop;

public class BusLineFragment extends Fragment {

    public static final String EXTRA_BUS_LINE = "com.dcordero.ibadajoz.extra_bus_line";

    private BusLine mLine;

    private static final String SAVE_INSTANCE_STATE_LINE = "com.dcordero.ibadajoz.save_instance_state_line";

    public static BusLineFragment newInstance(BusLine line)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_BUS_LINE, line);
        BusLineFragment lineFragment = new BusLineFragment();
        lineFragment.setArguments(args);
        return lineFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_INSTANCE_STATE_LINE, mLine);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_busline, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        HeaderListView headerListView = (HeaderListView) getView().findViewById(R.id.headerlistview);

        if(savedInstanceState != null) {
            mLine = (BusLine) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE_LINE);
        }
        else {
            mLine = (BusLine) getArguments().getSerializable(EXTRA_BUS_LINE);
        }

        headerListView.setAdapter(new BusLineSectionAdapter());
        headerListView.setBackgroundResource(R.drawable.background);
    }

    private class BusLineSectionAdapter extends SectionAdapter {

        private static final String CONVERT_VIEW_HEADER = "convert_view_header";
        private static final String CONVERT_VIEW_ROW_CELL = "convert_view_row_cell";

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

            BusStop stop = (BusStop) getRowItem(section, row);
            Intent intent = new Intent(getActivity(), BusStopActivity.class);
            intent.putExtra(BusStopFragment.EXTRA_BUS_STOP, stop);
            startActivity(intent);
        }

        @Override
        public int numberOfSections() {
            return 2;
        }

        @Override
        public int numberOfRows(int section) {
            switch (section) {
                case 0:
                    return mLine.forwardStops.size();
                case 1:
                    return mLine.backStops.size();
            }
            return 0;
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup parent) {

            if (convertView == null  || (convertView.getTag() != CONVERT_VIEW_ROW_CELL)) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_busline, null);
                convertView.setTag(CONVERT_VIEW_ROW_CELL);
            }

            BusStop stop = (BusStop) getRowItem(section, row);
            TextView stopLabelTextView = (TextView) convertView.findViewById(R.id.stop_label);
            stopLabelTextView.setText(stop.label);

            View stopColorView = convertView.findViewById(R.id.stop_color);
            GradientDrawable stopCircleDrawable = (GradientDrawable) stopColorView.getBackground();
            stopCircleDrawable.setColor(Color.parseColor("#" + mLine.color));

            View lineColorView = convertView.findViewById(R.id.line_color);
            GradientDrawable lineCircleDrawable = (GradientDrawable) lineColorView.getBackground();
            lineCircleDrawable.setColor(Color.parseColor("#" + mLine.color));

            return convertView;
        }

        @Override
        public Object getRowItem(int section, int row) {
            switch (section) {
                case 0:
                    return mLine.forwardStops.get(row);
                case 1:
                    return mLine.backStops.get(row);
            }
            return null;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

            if (convertView == null || (convertView.getTag() != CONVERT_VIEW_HEADER)) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_header_busline, null);
                convertView.setTag(CONVERT_VIEW_HEADER);
            }

            TextView headerTextView = (TextView) convertView.findViewById(R.id.headerlabel);
            switch (section) {
                case 0:
                    headerTextView.setText(R.string.line_forward);
                    break;
                case 1:
                    headerTextView.setText(R.string.line_return);
                    break;
            }

            return convertView;
        }
    }
}
