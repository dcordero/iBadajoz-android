package com.dcordero.ibadajoz.core.models.tubasa;

import java.io.Serializable;
import java.util.ArrayList;

public class BusLine implements Serializable {
    public String label;
    public String color;
    public String code;

    public ArrayList<BusStop> forwardStops = new ArrayList<>();
    public ArrayList<BusStop> backStops = new ArrayList<>();

    public String labelDescriptor()
    {
        String descriptor = "";
        if (forwardStops != null && !forwardStops.isEmpty()) {
            descriptor += forwardStops.get(0).label + " → ";
            descriptor += forwardStops.get(forwardStops.size() - 1).label;
        }

        return descriptor;
    }
}
