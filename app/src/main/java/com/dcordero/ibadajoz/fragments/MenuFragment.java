package com.dcordero.ibadajoz.fragments;

import android.app.ListFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.activities.MainActivity;

import java.util.ArrayList;

public class MenuFragment extends ListFragment {

    ArrayList<String> menuOptions;

    private View mHeaderView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        menuOptions = new ArrayList<>();
        menuOptions.add(getResources().getString(R.string.sliding_menu_news));
        menuOptions.add(getResources().getString(R.string.sliding_menu_contacts));
        menuOptions.add(getResources().getString(R.string.sliding_menu_tubasa));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHeaderView = inflater.inflate(R.layout.list_header_menu, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.parseColor("#0083a3"));
        if (mHeaderView != null)  this.getListView().addHeaderView(mHeaderView);
        setupAdapter();
    }

    private void setupAdapter()
    {
        setListAdapter(new MenuAdapter(menuOptions));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        ((MainActivity)getActivity()).selectedMenuOption(position);
    }

    private class MenuAdapter extends ArrayAdapter<String>
    {
        public MenuAdapter(ArrayList<String> menuOptions) {
            super(getActivity(), 0, menuOptions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_menu, null);
            }

            String menuOption = menuOptions.get(position);

            TextView menuEntryTextView = (TextView) convertView.findViewById(R.id.menu_label);
            menuEntryTextView.setText(menuOption);

            return convertView;
        }
    }
}
