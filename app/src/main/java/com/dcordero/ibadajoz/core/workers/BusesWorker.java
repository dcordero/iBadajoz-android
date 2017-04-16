package com.dcordero.ibadajoz.core.workers;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dcordero.ibadajoz.BadajozBusApplication;
import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.core.models.tubasa.BusLine;
import com.dcordero.ibadajoz.core.models.tubasa.BusStop;
import com.dcordero.ibadajoz.core.models.tubasa.BusStopInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class CustomJsonObjectRequest  extends JsonObjectRequest
{
    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest,Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map getHeaders() throws AuthFailureError {
        Map headers = new HashMap();
        headers.put("x-api-key", BadajozBusApplication.getAppContext().getString(R.string.aws_apikey));
        return headers;
    }

}

public class BusesWorker {

    public static void fetchStopInfo(BusStop busStop, final WorkerCallback callback)
    {
        String url = BadajozBusApplication.getAppContext().getString(R.string.busstop_url);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("idl", busStop.line.code );
            parameters.put("idp", busStop.idp );
            parameters.put("ido", busStop.ido );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new CustomJsonObjectRequest
                (Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.success(new BusStopInfo(response));
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(BadajozBusApplication.getAppContext());
        queue.add(jsObjRequest);
    }

    public static void fetchAllLines(final WorkerCallback callback)
    {
        String url = BadajozBusApplication.getAppContext().getString(R.string.buslines_url);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.success(parseAllLinesObject(response));
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

    private static ArrayList<BusLine> parseAllLinesObject(JSONObject linesJSONObject) throws JSONException {

        ArrayList<BusLine> parsedLines = new ArrayList<BusLine>();

        JSONArray arrayOfLines = linesJSONObject.getJSONArray("lineas");
        for(int i = 0 ; i < arrayOfLines.length() ; i++) {
            JSONObject lineObject = arrayOfLines.getJSONObject(i);

            BusLine line = new BusLine();
            line.label = lineObject.getString("label");
            line.color = lineObject.getString("color");
            line.code = lineObject.getString("code");

            JSONObject stopsObject = lineObject.getJSONObject("paradas");
            try {
                JSONArray stopsForwardObject = stopsObject.getJSONArray("ida");
                for (int j=0; j<stopsForwardObject.length(); j++) {
                    BusStop stop = new BusStop(stopsForwardObject.getJSONObject(j));
                    stop.line = line;
                    line.forwardStops.add(stop);
                }

                JSONArray stopsBackObject = stopsObject.getJSONArray("vuelta");
                for (int j=0; j<stopsBackObject.length(); j++) {
                    BusStop stop = new BusStop(stopsBackObject.getJSONObject(j));
                    stop.line = line;
                    line.backStops.add(stop);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            parsedLines.add(line);
        }
        return parsedLines;
    }
}
