package com.dcordero.ibadajoz.core.models.contacts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Contact implements Serializable {
    public String name;
    public String address;
    public String phone;
    public String web;
    public String longitude;
    public String latitude;

    public Contact(JSONObject contactJSONObject)
    {
        try {
            name = contactJSONObject.has("nombre") ? contactJSONObject.getString("nombre") : null;
            address = contactJSONObject.has("direccion") ? contactJSONObject.getString("direccion") : null;
            phone = contactJSONObject.has("telefono1") ? contactJSONObject.getString("telefono1") : null;
            web = contactJSONObject.has("web") ? contactJSONObject.getString("web") : null;
            longitude = contactJSONObject.has("latitud") ? contactJSONObject.getString("latitud") : null;
            latitude = contactJSONObject.has("longitud") ? contactJSONObject.getString("longitud") : null;
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
