package com.dcordero.ibadajoz.fragments.contacts;

import android.content.Intent;
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
import com.dcordero.ibadajoz.activities.contacts.ContactsViewerActivity;
import com.dcordero.ibadajoz.core.models.contacts.Contact;
import com.dcordero.ibadajoz.core.models.contacts.ContactCategory;
import com.dcordero.ibadajoz.core.workers.ContactsWorker;
import com.dcordero.ibadajoz.core.workers.WorkerCallback;

import java.util.ArrayList;

public class ContactsListFragment extends Fragment {

    private ArrayList<ContactCategory> mContactCategories = new ArrayList<>();
    private ContactCategorySectionAdapter mContactCategorySectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts_list, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HeaderListView headerListView = (HeaderListView) getView().findViewById(R.id.headerlistview);
        mContactCategorySectionAdapter = new ContactCategorySectionAdapter();
        headerListView.setAdapter(mContactCategorySectionAdapter);
        headerListView.setBackgroundResource(R.drawable.background);

        fetchContacts();
    }

    private void fetchContacts() {
        ContactsWorker.fetchContacts(new WorkerCallback() {
            @Override
            public void success(Object responseObject) {
                mContactCategories.clear();
                mContactCategories.addAll((ArrayList<ContactCategory>) responseObject);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mContactCategorySectionAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void failure() {

            }
        });
    }

    private class ContactCategorySectionAdapter extends SectionAdapter {

        private static final String CONVERT_VIEW_HEADER = "convert_view_header";
        private static final String CONVERT_VIEW_ROW_CELL = "convert_view_row_cell";

        @Override
        public int numberOfSections() {
            return (mContactCategories == null) ? 0 : mContactCategories.size();
        }

        @Override
        public int numberOfRows(int section) {
            return (mContactCategories.get(section).contacts == null) ? 0 : mContactCategories.get(section).contacts.size();
        }

        @Override
        public View getRowView(int section, int row, View convertView, ViewGroup viewGroup) {

            if (convertView == null || (convertView.getTag() != CONVERT_VIEW_ROW_CELL)) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_contact, null);
                convertView.setTag(CONVERT_VIEW_ROW_CELL);
            }

            Contact contact = (Contact) getRowItem(section, row);
            TextView contactNameTextView = (TextView) convertView.findViewById(R.id.contact_name_label);
            contactNameTextView.setText(contact.name);

            return convertView;
        }

        @Override
        public boolean hasSectionHeaderView(int section) {
            return true;
        }

        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {

            if (convertView == null || (convertView.getTag() != CONVERT_VIEW_HEADER)) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_header_contact_category, null);
                convertView.setTag(CONVERT_VIEW_HEADER);
            }

            ContactCategory contactCategory = mContactCategories.get(section);
            TextView headerTextView = (TextView) convertView.findViewById(R.id.headerlabel);
            headerTextView.setText(contactCategory.name);

            return convertView;
        }

        @Override
        public Object getRowItem(int section, int row) {
            return mContactCategories.get(section).contacts.get(row);
        }

        @Override
        public void onRowItemClick(AdapterView<?> parent, View view, int section, int row, long id) {
            super.onRowItemClick(parent, view, section, row, id);

            Contact contact = (Contact) getRowItem(section, row);
            Intent intent = new Intent(getActivity(), ContactsViewerActivity.class);
            intent.putExtra(ContactsViewerFragment.EXTRA_CONTACT, contact);
            startActivity(intent);
        }
    }
}
