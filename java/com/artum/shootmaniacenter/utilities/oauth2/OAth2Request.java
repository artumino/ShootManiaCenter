package com.artum.shootmaniacenter.utilities.oauth2;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.artum.shootmaniacenter.R;
import com.artum.shootmaniacenter.ShowAccount;
import com.artum.shootmaniacenter.adapters.menuItemAdapter;
import com.artum.shootmaniacenter.global.Variables;
import com.artum.shootmaniacenter.menu.MenuDrawerClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 07/07/13.
 */
public class OAth2Request extends Activity {

    TextView alert;
    ImageView connectButton;
    WebView browser;
    String code = "";
    Boolean refresh;

    String api_username = "artum|appAccount";
    String api_secret = "app14185";
    String redirect_uri = "http://localhost:8080/ShootmaniaCenter";

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Variables.menu_selected = 2;

        setContentView(R.layout.oauth2_request);


        refresh = getIntent().getBooleanExtra("refresh", false);



            alert = (TextView)findViewById(R.id.alert_text);
            connectButton = (ImageView)findViewById(R.id.connect_button);
            browser = (WebView)findViewById(R.id.auth_browser);
            browser.setWebViewClient(new DontRedirect());
            browser.getSettings().setBuiltInZoomControls(true);
            browser.getSettings().setJavaScriptEnabled(true);


        if(!refresh)
        {
            connectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RunOAth2();
                }
            });
        }
        else
        {
            connectButton.setVisibility(View.INVISIBLE);
            alert.setVisibility(View.INVISIBLE);
            new RefreshToken().execute("");
        }
        setupDrawer();
    }

    private void RunOAth2()
    {
        alert.setVisibility(View.INVISIBLE);
        connectButton.setVisibility(View.INVISIBLE);
        browser.setVisibility(View.VISIBLE);

        browser.loadUrl("https://ws.maniaplanet.com/oauth2/authorize/?client_id=" + api_username + "&redirect_uri=" + redirect_uri + "&scope=buddies teams dedicated titles offline&response_type=code");
    }

    private class DontRedirect extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            HashMap results = new HashMap();
                Pattern pattern = Pattern.compile("[?&]([^&]+)=([^&]+)");
                Matcher matcher = pattern.matcher(url);
                while(matcher.find())
                {
                    results.put(matcher.group(1), matcher.group(2));
                }
                if(code == "" && results.get("code") != null)
                {
                    view.setVisibility(View.INVISIBLE);
                    code = (String)results.get("code");
                    alert.setVisibility(View.VISIBLE);
                    alert.setText("Getting token from Nadeo Servers...");
                    new GetToken().execute("");
                }
                else
                {
                    view.loadUrl(url);
                }
            return false;
        }
    }

    private class GetToken extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            return TokenManager.getTokenFromParams("https://ws.maniaplanet.com/oauth2/token/", "client_id="+ api_username +"&client_secret=" + api_secret +"&redirect_uri="+ redirect_uri + "&grant_type=authorization_code&code=" + code, false);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject object = new JSONObject(s);
                if(object.getString("access_token") != null)
                {
                    Variables.oauth2_token = object.getString("access_token");
                    Variables.oauth2_refresh_token = object.getString("refresh_token");
                    Variables.oauth2_username = object.getString("login");
                    Variables.oauth2_token_expires = Calendar.getInstance().getTime().getTime() + (object.getLong("expires_in") * 1000);
                    Variables.oauth2_token_expires_in = object.getLong("expires_in");
                    Variables.ForceSaveTokenData(OAth2Request.this);
                    finishTask();
                }
                else
                {
                    alert.setText(object.getString("error"));
                }

            } catch (JSONException e) {

                alert.setText("Error parsing Response:\n" + s);
            }
        }
    }

    private class RefreshToken extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... strings) {
            return TokenManager.getTokenFromParams("https://ws.maniaplanet.com/oauth2/token/", "client_id=" + api_username + "&client_secret=" + api_secret + "grant_type=refresh_token&refresh_token=" + Variables.oauth2_refresh_token, true);
        }

        @Override
        protected void onPostExecute(String s) {
        Variables.oauth2_token_expires = Calendar.getInstance().getTime().getTime() + (Variables.oauth2_token_expires_in * 1000);
        Variables.ForceSaveTokenData(OAth2Request.this);
        finishTask();
        }
    }

    private void finishTask()
    {
        Intent myIntent = new Intent(getApplication(), ShowAccount.class);
        if(myIntent != null)                                                                                    //Controlla che l'elemento selezionato corrisponda ad un'activity.
        {
            startActivity(myIntent);
            this.finish();
        }

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
            menuDrawerClass.selectItem(position, OAth2Request.this);
        }
    }

    // </editor-fold>

}