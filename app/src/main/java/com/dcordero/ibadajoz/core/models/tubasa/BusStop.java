package com.dcordero.ibadajoz.core.models.tubasa;

import com.dcordero.ibadajoz.BadajozBusApplication;
import com.dcordero.ibadajoz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class BusStop implements Serializable {

    public String idp;
    public String ido;
    public String label;
    public BusLine line;

    public BusStop(JSONObject busStopJSONObject) {
        try {
            idp = busStopJSONObject.has("idp") ? busStopJSONObject.getString("idp") : null;
            ido =  busStopJSONObject.has("ido") ? busStopJSONObject.getString("ido") : null;
            label =  busStopJSONObject.has("label") ? busStopJSONObject.getString("label") : null;

            if (label != null && label.isEmpty()) {
                label = BadajozBusApplication.getAppContext().getResources().getString(R.string.parada_without_name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object != null && object instanceof BusStop)
        {
            BusStop busStop = (BusStop) object;
            return (idp.equals(busStop.idp)) && (ido.equals(busStop.ido));
        }
        return false;
    }
}
