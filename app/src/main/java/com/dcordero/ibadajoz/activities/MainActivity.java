package com.dcordero.ibadajoz.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.dcordero.ibadajoz.R;
import com.dcordero.ibadajoz.fragments.contacts.ContactsListFragment;
import com.dcordero.ibadajoz.fragments.news.NewsTabsFragment;
import com.dcordero.ibadajoz.fragments.tubasa.BusesTabsFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends SingleFragmentActivity {

    private static final String SAVE_INSTANCE_LAST_SELECTION = "com.dcordero.ibadajoz.save_instance_last_selection";

    private SlidingMenu slidingMenu ;
    private int mLastSelection;

    @Override
    protected Fragment createFragment() {
        loadLastSelection();
        switch (mLastSelection) {
            case 2:
                setTitle(R.string.action_bar_contacts);
                return new ContactsListFragment();
            case 3:
                setTitle(R.string.action_bar_buses);
                return new BusesTabsFragment();
            default:
                setTitle(R.string.action_bar_news);
                return new NewsTabsFragment();
        }
    }

    @Override
    protected void postCreate() {
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
        slidingMenu.setShadowDrawable(R.drawable.slidingmenu_shadow);
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slidingMenu.setFadeDegree(0.35f);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setMenu(R.layout.activity_sliding_menu);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void selectedMenuOption(int position) {
        slidingMenu.toggle();

        saveLastSelection(position);
        getActionBar().removeAllTabs();
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        Fragment selectedFragment;
        switch (position) {
            case 1:
                selectedFragment = new NewsTabsFragment();
                setTitle(R.string.action_bar_news);
                break;
            case 2:
                selectedFragment = new ContactsListFragment();
                setTitle(R.string.action_bar_contacts);
                break;
            case 3:
                selectedFragment = new BusesTabsFragment();
                setTitle(R.string.action_bar_buses);
                break;
            default:
                return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment)
                .commit();

    }

    @Override
    public void onBackPressed() {
        if ( slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            this.slidingMenu.toggle();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.slidingMenu.toggle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveLastSelection(int lastSelection)
    {
        SharedPreferences prefs = this.getSharedPreferences("com.dcordero.ibadajoz", Context.MODE_PRIVATE);
        prefs.edit().putInt(SAVE_INSTANCE_LAST_SELECTION, lastSelection).apply();
    }

    private void loadLastSelection(){
        SharedPreferences prefs = this.getSharedPreferences("com.dcordero.ibadajoz", Context.MODE_PRIVATE);
        mLastSelection = prefs.getInt(SAVE_INSTANCE_LAST_SELECTION, 1);
    }

}
