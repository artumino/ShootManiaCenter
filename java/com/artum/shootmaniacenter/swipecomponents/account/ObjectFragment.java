package com.artum.shootmaniacenter.swipecomponents.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artum.shootmaniacenter.R;

/**
 * Created by artum on 25/05/13.
 *
 * Frammento della pagina Account
 *
 */

// Instances of this class are fragments representing a single
// object in our collection.
public class ObjectFragment extends Fragment {

    public static final String ARG_OBJECT = "object";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.activity_main, container, false);
        Bundle args = getArguments();
        return rootView;
    }
}

