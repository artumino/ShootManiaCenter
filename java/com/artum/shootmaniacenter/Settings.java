package com.artum.shootmaniacenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.artum.shootmaniacenter.global.Variables;
import com.artum.shootmaniacenter.menu.MenuDrawerClass;
import com.artum.shootmaniacenter.adapters.menuItemAdapter;

/**
 * Created by artum on 02/06/13.
 */
public class Settings extends Activity {

    TextView userText;
    TextView passText;
    Button saveButton;
    Button resetButton;
    Button goToBrowser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Variables.menu_selected = 4;

        setContentView(R.layout.settings_view);

        userText = (TextView)findViewById(R.id.APIuserText);
        passText = (TextView)findViewById(R.id.APIpassText);
        saveButton = (Button)findViewById(R.id.saveSettings);
        resetButton = (Button)findViewById(R.id.resetSettings);
        goToBrowser = (Button)findViewById(R.id.goToWebAPI);

        SharedPreferences settings = getSharedPreferences("API_Account", MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();

        if(Variables.API_Username != "artum|ladderapp")
            userText.setText(Variables.API_Username);
        if(Variables.API_Password != "app14185")
            passText.setText(Variables.API_Password);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userText.getText() != "" && userText.getText() != "artum|app14185")
                {
                    editor.putString("account", userText.getText().toString());
                    editor.putString("pass", passText.getText().toString());
                    editor.apply();

                    AlertDialog alertDialog;
                    alertDialog = new AlertDialog.Builder(Settings.this).create();
                    alertDialog.setTitle("Settings");
                    alertDialog.setMessage("Settings Saved!");
                    alertDialog.show();

                    Variables.API_Username = userText.getText().toString();
                    Variables.API_Password = passText.getText().toString();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("account", "");
                editor.putString("pass", "");
                editor.apply();

                Variables.API_Username = "artum|ladderapp";
                Variables.API_Password = "app14185";

                userText.setText("");
                passText.setText("");
            }
        });

        goToBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://player.maniaplanet.com/webservices"));
                startActivity(browserIntent);
            }
        });

        setupDrawer();

    }

    // <editor-fold defaultstate="collapsed" desc="MENU DRAWER CODE">
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;

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
            menuDrawerClass.selectItem(position, Settings.this);
        }
    }

    // </editor-fold>

}