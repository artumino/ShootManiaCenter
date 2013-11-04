package com.libcorp.shootmaniacenter.global;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by artum on 01/06/13.
 *
 * Variabili Globali
 * Salvataggio delle Impostazioni
 *
 */
public class Variables {

    public static String API_Username = "artum|ladderapp";
    public static String API_Password = "app14185";
    public static int menu_selected = 0;

    public static String news_lastUpdate = "";

    public static String oauth2_token = "";
    public static  String oauth2_username = "";
    public static String oauth2_refresh_token = "";
    public static long oauth2_token_expires = -1;
    public static long oauth2_token_expires_in = -1;

    public static void LoadVariables(Activity activity) //Chiamato dalla activity di start, carica tutte le variabili globali
    {
        Variables.menu_selected = 0;

        SharedPreferences preferences = activity.getSharedPreferences("API_Account", Context.MODE_PRIVATE);

        if(preferences.getString("account", "") != "")
            API_Username = preferences.getString("account", "");
        if(preferences.getString("pass", "") != "")
            API_Password = preferences.getString("pass", "");

        oauth2_token = preferences.getString("oauth2_token", "");
        oauth2_username = preferences.getString("oauth2_username", "");
        oauth2_token_expires = preferences.getLong("oauth2_token_expires", -1);
        oauth2_refresh_token = preferences.getString("oauth2_refresh_token", "");
        oauth2_token_expires_in = preferences.getLong("oauth2_token_expires_in", -1);


        preferences = activity.getSharedPreferences("News", Context.MODE_PRIVATE);

        news_lastUpdate = preferences.getString("lastBuildDate", "");
    }

    public static void ForceSaveTokenData(Activity activity)    //Salva le variabili relative al sistema OAuth2
    {
        SharedPreferences preferences = activity.getSharedPreferences("API_Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("oauth2_token", oauth2_token);
        editor.putString("oauth2_username", oauth2_username);
        editor.putLong("oauth2_token_expires", oauth2_token_expires);
        editor.putString("oauth2_refresh_token", oauth2_refresh_token);
        editor.putLong("oauth2_token_expires_in", oauth2_token_expires_in);
        editor.apply();
    }

    public static void ForceSaveNewsDate(Activity activity)
    {
        SharedPreferences preferences = activity.getSharedPreferences("News_Cache", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("lastBuildDate", news_lastUpdate);
        editor.apply();
    }
}
