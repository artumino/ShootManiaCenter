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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 18/05/13.
 */
public class NadeoDataSeeker {


    public String getEliteLadder(int offset, int length)
    {
        return getStringFromNadeo("http://ws.maniaplanet.com/elite/rankings/multiplayer/zone/?length=" + length + "&offset=" + offset);
    }

    public String getJoustLadder(int offset, int length)
    {
        return getStringFromNadeo("http://ws.maniaplanet.com/titles/rankings/multiplayer/zone/?title=SMStormJoust@nadeolabs&length=" + length + "&offset=" + offset);
    }

    public String getStormLadder(int offset, int length)
    {
        return getStringFromNadeo("http://ws.maniaplanet.com/storm/rankings/multiplayer/zone/?length=" + length + "&offset=" + offset);
    }

    public String getPlayerInfo(String playerName)
    {
        return getStringFromNadeo("http://ws.maniaplanet.com/players/" + playerName + "/");
    }

    public String getPlayerRank(String title, String playerName)
    {
        if(title != "joust")
            return getStringFromNadeo("http://ws.maniaplanet.com/"+ title + "/rankings/multiplayer/player/" + playerName + "/");
        else
            return getStringFromNadeo("http://ws.maniaplanet.com/titles/rankings/multiplayer/player/" + playerName + "/?title=SMStormJoust@nadeolabs");
    }

    public String getStringFromNadeo(String URL)
    {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            Credentials creds = new UsernamePasswordCredentials(Variables.API_Username, Variables.API_Password);
            ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

            response = httpclient.execute(new HttpGet(URL));
            responseString = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(responseString == null || responseString.charAt(0) != '{')
            responseString = "Nope";

        return responseString;
    }

    public String postStringToNadeo(String URL)
    {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            Credentials creds = new UsernamePasswordCredentials("artum|ladderapp", "app14185");
            ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(AuthScope.ANY, creds);

            response = httpclient.execute(new HttpPost(URL));
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
        String segment = getStringFromNadeo("http://ws.maniaplanet.com/zones/id/" + zoneId + "/");
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

    public String getNewsFeedXML(String URL)
    {
        String responseString = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = httpclient.execute(new HttpGet(URL));
            responseString = EntityUtils.toString(response.getEntity());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

}
