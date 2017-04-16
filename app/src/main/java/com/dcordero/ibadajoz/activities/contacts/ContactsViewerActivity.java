package com.dcordero.ibadajoz.activities.contacts;

import android.support.v4.app.Fragment;

import com.dcordero.ibadajoz.activities.SingleFragmentActivity;
import com.dcordero.ibadajoz.core.models.contacts.Contact;
import com.dcordero.ibadajoz.fragments.contacts.ContactsViewerFragment;

public class ContactsViewerActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        Contact contact = (Contact) getIntent().getSerializableExtra(ContactsViewerFragment.EXTRA_CONTACT);
        return new ContactsViewerFragment().newInstance(contact);
    }
}
