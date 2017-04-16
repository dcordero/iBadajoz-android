package com.dcordero.ibadajoz.activities.tubasa;

import android.support.v4.app.Fragment;

import com.dcordero.ibadajoz.activities.SingleFragmentActivity;
import com.dcordero.ibadajoz.core.models.tubasa.BusLine;
import com.dcordero.ibadajoz.fragments.tubasa.BusLineFragment;

public class BusLineActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        BusLine line = (BusLine) getIntent().getSerializableExtra(BusLineFragment.EXTRA_BUS_LINE);
        return BusLineFragment.newInstance(line);
    }
}
