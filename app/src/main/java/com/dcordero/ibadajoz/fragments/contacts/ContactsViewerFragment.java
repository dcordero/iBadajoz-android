package com.dcordero.ibadajoz.fragments.contacts;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.core.models.contacts.Contact;

public class ContactsViewerFragment extends Fragment {

    private static final String SAVE_INSTANCE_STATE_CONTACT = "com.dcordero.ibadajoz.save_instance_contact";

    public static final String EXTRA_CONTACT = "com.dcordero.ibadajoz.extra_contact";

    private Contact mContact;

    public static ContactsViewerFragment newInstance(Contact contact)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_CONTACT, contact);
        ContactsViewerFragment fragment = new ContactsViewerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_INSTANCE_STATE_CONTACT, mContact);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts_viewer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         if (savedInstanceState != null) {
            mContact = (Contact) savedInstanceState.getSerializable(SAVE_INSTANCE_STATE_CONTACT);
        }
        else if (getArguments() != null) {
             mContact = (Contact) getArguments().getSerializable(EXTRA_CONTACT);
        }

        setupInfoView();
    }

    private void setupInfoView()
    {
        getView().setBackgroundResource(R.drawable.background);

        if (mContact.name != null) {
            ((TextView) getView().findViewById(R.id.contact_name_label)).setText(mContact.name);
        }

        if (mContact.address != null) {
            ((TextView) getView().findViewById(R.id.contact_address_label)).setText(mContact.address);
        }
        else {
            ((LinearLayout) getView().findViewById(R.id.contact_address_section)).setVisibility(View.GONE);
        }

        if (mContact.phone != null) {
            ((TextView) getView().findViewById(R.id.contact_phone_label)).setText(mContact.phone);
        }
        else {
            ((LinearLayout) getView().findViewById(R.id.contact_phone_section)).setVisibility(View.GONE);
        }

        if (mContact.web != null) {
            ((TextView) getView().findViewById(R.id.contact_web_label)).setText(mContact.web);
        }
        else {
            ((LinearLayout) getView().findViewById(R.id.contact_web_section)).setVisibility(View.GONE);
        }

    }
}
