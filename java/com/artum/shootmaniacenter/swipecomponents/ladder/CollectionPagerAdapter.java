package com.artum.shootmaniacenter.swipecomponents.ladder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by artum on 25/05/13.
 *
 *
 * Gestione delle swipe Tabs nella Ladder.
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
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String pageName = "null";
        switch(position)
        {
            case 0:
                pageName = "Elite";
                break;
            case 1:
                pageName = "Storm";
                break;
            case 2:
                pageName = "Joust";
                break;
        }
        return pageName;
    }
}
