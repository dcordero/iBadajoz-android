package com.dcordero.ibadajoz.fragments.tubasa;

import android.app.Activity;
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
import com.dcordero.ibadajoz.activities.tubasa.BusLineActivity;
import com.dcordero.ibadajoz.core.models.tubasa.BusLine;
import com.dcordero.ibadajoz.core.workers.BusesWorker;
import com.dcordero.ibadajoz.core.workers.WorkerCallback;

import java.util.ArrayList;

public class LinesFragment extends ListFragment {

    private ArrayList<BusLine> mLines = new ArrayList<>();

    private static final String SAVE_INSTANCE_LINES = "com.dcordero.ibadajoz.save_instance_lines";
    private static final String SAVE_INSTANCE_LISTVIEW_STATE = "com.dcordero.ibadajoz.save_instance_listview_state";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_INSTANCE_LINES, mLines);
        outState.putParcelable(SAVE_INSTANCE_LISTVIEW_STATE, getListView().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getListView().setBackgroundResource(R.drawable.background);

        if (savedInstanceState != null) {
            mLines = (ArrayList<BusLine>) savedInstanceState.getSerializable(SAVE_INSTANCE_LINES);
            ((LinesAdapter) getListAdapter()).notifyDataSetChanged();
            getListView().onRestoreInstanceState(savedInstanceState.getParcelable(SAVE_INSTANCE_LISTVIEW_STATE));
        }

        if (mLines.isEmpty()) {
            fetchLines();
        }
    }

    private void fetchLines()
    {
        BusesWorker.fetchAllLines(new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                mLines.clear();
                mLines.addAll((ArrayList<BusLine>) responseObject);

                Activity mainActivity = getActivity();
                if (mainActivity != null) {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((LinesAdapter) getListAdapter()).notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void failure() {
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        BusLine line = (BusLine) getListAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), BusLineActivity.class);
        intent.putExtra(BusLineFragment.EXTRA_BUS_LINE, line);
        startActivity(intent);
    }

    private void setupAdapter()
    {
        setListAdapter(new LinesAdapter(mLines));
    }

    private class LinesAdapter extends ArrayAdapter<BusLine>
    {

        public LinesAdapter(ArrayList<BusLine> lines) {
            super(getActivity(), 0, lines);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_lines, null);
            }

            BusLine line = getItem(position);

            TextView lineNumberTextView = (TextView) convertView.findViewById(R.id.line_number);
            lineNumberTextView.setText(getResources().getString(R.string.line_label) + " " + line.label);

            TextView lineLabelTextView = (TextView) convertView.findViewById(R.id.line_label);
            lineLabelTextView.setText(line.labelDescriptor());

            View lineColorView = convertView.findViewById(R.id.line_color);
            GradientDrawable lineCircleDrawable = (GradientDrawable) lineColorView.getBackground();
            lineCircleDrawable.setColor(Color.parseColor("#" + line.color));

            return convertView;
        }
    }
}
