package com.artum.shootmaniacenter.utilities.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.artum.shootmaniacenter.global.Variables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jacopo on 07/08/13.
 */
public class NewsCacheManager {


    static public String loadFromCache(Activity activity, String param)
    {
        if(activity != null)
        {
            SharedPreferences preferences = activity.getSharedPreferences("News_Cache", Context.MODE_PRIVATE);
            return preferences.getString(param, null);
        }
        return null;
    }

    static public void saveToCache(Activity activity, String param, String data)
    {
        if(activity != null)
        {
            SharedPreferences preferences = activity.getSharedPreferences("News_Cache", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(param, data);
            Variables.ForceSaveNewsDate(activity);
            editor.apply();
        }
    }

    static public boolean hasToUpdate(Activity activity){
        if(activity != null)
        {
            SharedPreferences preferences = activity.getSharedPreferences("News_Cache", Context.MODE_PRIVATE);
            if(preferences.getString("lastBuildDate", null) != null && preferences.getString("news", null) != null)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                try {
                    Date lastUpdate = sdf.parse(preferences.getString("lastBuildDate", null));
                    Date lastBuildDate = sdf.parse(Variables.news_lastUpdate);
                    if(lastBuildDate.after(lastUpdate))
                        return true;
                    else
                        return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                    return true;
                }
            }
        }
        return true;
    }

}
