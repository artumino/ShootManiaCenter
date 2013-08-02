package com.artum.shootmaniacenter.utilities.oauth2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by artum on 08/07/13.
 */
public class TokenManager {
    static public String getTokenFromParams(String URI, String params, Boolean refersh)
    {
        String response = "";
        URL url = null;
        try {
            url = new URL(URI);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("charset", "utf-8");
        connection.setRequestProperty("Content-Length", "" + Integer.toString(params.getBytes().length));
        connection.setUseCaches (false);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
        wr.writeBytes(params);
        wr.flush();
        wr.close();
        if(!refersh)
        {
            int temp;
            while((temp = connection.getInputStream().read()) != -1)
                response += (char)temp;
        }
        connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
