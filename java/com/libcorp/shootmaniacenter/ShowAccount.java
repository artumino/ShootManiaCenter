package com.libcorp.shootmaniacenter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.libcorp.shootmaniacenter.R;
import com.libcorp.shootmaniacenter.adapters.menuItemAdapter;
import com.libcorp.shootmaniacenter.global.Variables;
import com.libcorp.shootmaniacenter.menu.MenuDrawerClass;
import com.libcorp.shootmaniacenter.swipecomponents.account.CollectionPagerAdapter;
import com.libcorp.shootmaniacenter.swipecomponents.DepthPageTransformer;

/**
 * Created by artum on 02/08/13.
 */
public class ShowAccount extends FragmentActivity {

    CollectionPagerAdapter mCollectionPagerAdapter;
    ViewPager mViewPager;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Variables.menu_selected = 2;

        setContentView(R.layout.show_account);

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mCollectionPagerAdapter = new CollectionPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager)findViewById(R.id.pager);

        mViewPager.setPageTransformer(true, new DepthPageTransformer());

        mViewPager.setAdapter(mCollectionPagerAdapter);

        setupDrawer();
    }

    // <editor-fold defaultstate="collapsed" desc="MENU DRAWER CODE">

    private void setupDrawer()      //Insert this code in OnCreate function!
    {

        String[] mTitles = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navigation_list);

        ViewGroup.LayoutParams params= mDrawerList.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double inches = Math.sqrt((metrics.widthPixels*metrics.widthPixels) + (metrics.heightPixels*metrics.heightPixels)) / metrics.densityDpi;

        if(inches > 5)
            params.width= (metrics.widthPixels / 100) * 50;
        else
            params.width = metrics.widthPixels;
        mDrawerList.setLayoutParams(params);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new menuItemAdapter(this, mTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MenuDrawerClass menuDrawerClass = new MenuDrawerClass();
            mDrawerLayout.closeDrawers();
            menuDrawerClass.selectItem(position, ShowAccount.this);
        }
    }

    // </editor-fold>
}