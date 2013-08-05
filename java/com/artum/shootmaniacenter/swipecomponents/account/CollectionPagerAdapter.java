package com.artum.shootmaniacenter.swipecomponents.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by artum on 25/05/13.
 *
 *
 * Gestione delle swipe Tabs della Account Tab.
 */

// Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
    public CollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(ObjectFragment.ARG_OBJECT, i);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageName = "null";
        switch(position)
        {
            case 0:
                pageName = "My Account";
                break;
            case 1:
                pageName = "Buddies";
                break;
            case 2:
                pageName = "Teams";
                break;
            case 3:
                pageName = "Favourites";
                break;
            case 4:
                pageName = "Titles";
                break;
        }
        return pageName;
    }
}
