package com.dcordero.ibadajoz.activities.tubasa;

import android.support.v4.app.Fragment;

import com.dcordero.ibadajoz.activities.SingleFragmentActivity;
import com.dcordero.ibadajoz.core.models.tubasa.BusStop;
import com.dcordero.ibadajoz.fragments.tubasa.BusStopFragment;

public class BusStopActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        BusStop busStop = (BusStop) getIntent().getSerializableExtra(BusStopFragment.EXTRA_BUS_STOP);
        return BusStopFragment.newInstance(busStop);
    }
}
