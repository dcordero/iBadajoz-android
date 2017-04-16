package com.dcordero.ibadajoz.core.models.tubasa;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

public class BusStopInfo implements Serializable {

    public String nextTime;

    public BusStopInfo(JSONObject stopInfoObject)
    {
        try {
            nextTime = stopInfoObject.getString("next_time");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
