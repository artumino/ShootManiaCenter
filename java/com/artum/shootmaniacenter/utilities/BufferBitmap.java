package com.artum.shootmaniacenter.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by artum on 24/05/13.
 */
public class BufferBitmap {
    public static Bitmap loadBitmap(String strUrl) {
        Bitmap bitmap = null;
        /*try {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
    }*/

        try {
            URL url = new URL(strUrl);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(true);
            InputStream response = (InputStream)connection.getContent();
            bitmap = BitmapFactory.decodeStream(response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
