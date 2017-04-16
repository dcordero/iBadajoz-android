package com.dcordero.ibadajoz.core.models.contacts;

import java.util.ArrayList;

public class ContactCategory {
    public String identifier;
    public String name;
    public ArrayList<Contact> contacts = new ArrayList<>();
}
