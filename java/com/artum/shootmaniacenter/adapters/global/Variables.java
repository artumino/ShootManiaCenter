package com.artum.shootmaniacenter.adapters.global;

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
    public static String oauth2_token = "";
    public static  String oauth2_username = "";
    public static long oauth2_token_expires = -1;

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
    }

    public static void ForceSaveTokenData(Activity activity)    //Salva le variabili relative al sistema OAuth2
    {
        SharedPreferences preferences = activity.getSharedPreferences("API_Account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("oauth2_token", oauth2_token);
        editor.putString("oauth2_username", oauth2_username);
        editor.putLong("oauth2_token_expires", oauth2_token_expires);
        editor.apply();
    }

}
