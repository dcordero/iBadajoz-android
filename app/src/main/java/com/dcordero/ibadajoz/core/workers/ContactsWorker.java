package com.dcordero.ibadajoz.core.workers;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dcordero.ibadajoz.BadajozBusApplication;
import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.core.models.contacts.Contact;
import com.dcordero.ibadajoz.core.models.contacts.ContactCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ContactsWorker {

    public static void fetchContacts(final WorkerCallback callback) {

        String url = BadajozBusApplication.getAppContext().getString(R.string.contacts_url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.success(parseAllContactsObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(BadajozBusApplication.getAppContext());
        queue.add(jsObjRequest);
    }

    private static ArrayList<ContactCategory> parseAllContactsObject(JSONObject linesJSONObject) throws JSONException {

        ArrayList<ContactCategory> parsedContactCategories = new ArrayList<ContactCategory>();

        JSONArray arrayOfContactCategories = linesJSONObject.getJSONArray("categorias");
        for(int i = 0 ; i < arrayOfContactCategories.length() ; i++) {
            JSONObject contactCategoryObject = arrayOfContactCategories.getJSONObject(i);

            ContactCategory contactCategory = new ContactCategory();
            contactCategory.identifier = contactCategoryObject.getString("idcategoria");
            contactCategory.name = contactCategoryObject.getString("nombrecategoria");

            try {
                JSONArray contactsObject = contactCategoryObject.getJSONArray("contacto");
                for (int j=0; j<contactsObject.length(); j++) {
                    Contact contact = new Contact(contactsObject.getJSONObject(j));
                    contactCategory.contacts.add(contact);
                }
            }
            catch (JSONException e1) {
                e1.printStackTrace();
            }

            parsedContactCategories.add(contactCategory);
        }

        return parsedContactCategories;
    }
}
