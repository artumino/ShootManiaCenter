package com.artum.shootmaniacenter.utilities;

import com.artum.shootmaniacenter.global.Variables;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 18/05/13.
 */
public class NadeoDataSeeker {

    public String getLadder(String title, int offset, int length)
    {
        if(title != "joust")
            return getStringFromNadeo("http://ws.maniaplanet.com/" + title + "/rankings/multiplayer/zone/?length=" + length + "&offset=" + offset, true);
        else
            return getStringFromNadeo("http://ws.maniaplanet.com/titles/rankings/multiplayer/zone/?title=SMStormJoust@nadeolabs&length=" + length + "&offset=" + offset, true);
    }

    public String getPlayerInfo(String playerName)
    {
        return getStringFromNadeo("http://ws.maniaplanet.com/players/" + playerName + "/", true);
    }

    public String getPlayerRank(String title, String playerName)
    {
        if(title != "joust")
            return getStringFromNadeo("http://ws.maniaplanet.com/"+ title + "/rankings/multiplayer/player/" + playerName + "/", true);
        else
            return getStringFromNadeo("http://ws.maniaplanet.com/titles/rankings/multiplayer/player/" + playerName + "/?title=SMStormJoust@nadeolabs", true);
    }

    public String getPlayer(String token)
    {
        return getStringFromOauth2("https://ws.maniaplanet.com/player/", false, token);
    }

    public String getTitles(String token)
    {
        return getStringFromOauth2("https://ws.maniaplanet.com/player/titles/installed/", false, token);
    }

    public String getStringFromOauth2(String URL, boolean auth, String token)
    {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            if(auth)
            {
                Credentials creds = new UsernamePasswordCredentials(Variables.API_Username, Variables.API_Password);
                ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
            }
            response.addHeader("Authorization" , "Bearer " + token);
            response = httpclient.execute(new HttpGet(URL));
            responseString = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(responseString == null || responseString.charAt(0) != '{')
            responseString = "Nope";

        return responseString;
    }

    public String getStringFromNadeo(String URL, boolean auth)
    {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            if(auth)
            {
                Credentials creds = new UsernamePasswordCredentials(Variables.API_Username, Variables.API_Password);
                ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);
            }

            response = httpclient.execute(new HttpGet(URL));
            responseString = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(responseString == null || responseString.charAt(0) != '{')
            responseString = "Nope";

        return responseString;
    }

    public String zoneJpgURL (int zoneId)
    {
        String segment = getStringFromNadeo("http://ws.maniaplanet.com/zones/id/" + zoneId + "/", true);
        String imgJPGURL = "null";
        Pattern pattern = Pattern.compile("\"iconJPGURL\":\"+(.+)\"");
        Matcher matcher = pattern.matcher(segment);
        while (matcher.find())
            imgJPGURL = matcher.group(1).replace("\\", "");

        if(imgJPGURL == "null")
        {
            pattern = Pattern.compile("\"iconURL\":\"+(.+)\",");
            matcher = pattern.matcher(segment);
            while (matcher.find())
                imgJPGURL = matcher.group(1).replace("\\", "");
        }

        return imgJPGURL;
    }



}
