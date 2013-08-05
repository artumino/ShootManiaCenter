package com.artum.shootmaniacenter.menu;

import android.app.Activity;
import android.content.Intent;

import com.artum.shootmaniacenter.LadderActivity;
import com.artum.shootmaniacenter.NewsReader;
import com.artum.shootmaniacenter.ShowAccount;
import com.artum.shootmaniacenter.utilities.oauth2.OAth2Request;
import com.artum.shootmaniacenter.Settings;
import com.artum.shootmaniacenter.global.Variables;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by artum on 29/06/13.
 *
 * Classe relativa al MenuDrawer
 */
public class MenuDrawerClass {

    /** Swaps fragments in the main content view */
    public void selectItem(int position, Activity currentActivity) {

        /*

                Metodo chiamato dopo la selezione di un elemento dal Drawer del menu

         */
        if(Variables.menu_selected != position)
        {
            Intent myIntent = null;
            switch(position)
            {
                case 0:                                                                                             //NEWS
                    myIntent = new Intent(currentActivity.getApplication(), NewsReader.class);
                    break;
                case 1:                                                                                             //LADDERS
                    myIntent = new Intent(currentActivity.getApplication(), LadderActivity.class);
                    break;
                case 2:                                                                                             //ACCOUNT
                    if(Variables.oauth2_token == "")
                    {
                        myIntent = new Intent(currentActivity.getApplication(), OAth2Request.class);
                        myIntent.putExtra("refresh", false);
                    }
                    else if(Variables.oauth2_token_expires < Calendar.getInstance().getTime().getTime())
                    {
                        myIntent = new Intent(currentActivity.getApplication(), OAth2Request.class);
                        myIntent.putExtra("refresh", true);
                    }
                    else
                    {
                        myIntent = new Intent(currentActivity.getApplication(), ShowAccount.class);
                    }
                    break;
                case 3:                                                                                             //SETTINGS
                    myIntent = new Intent(currentActivity.getApplication(), Settings.class);
                    break;
            }
            if(myIntent != null)                                                                                    //Controlla che l'elemento selezionato corrisponda ad un'activity.
            {
                currentActivity.startActivity(myIntent);
                currentActivity.finish();
            }
        }
    }

}
