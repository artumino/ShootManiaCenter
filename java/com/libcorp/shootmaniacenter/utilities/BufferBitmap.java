package com.libcorp.shootmaniacenter.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by artum on 24/05/13.
 */
public class BufferBitmap {

    static File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.artum.shootmaniacenter/files/cache");

    public static Bitmap loadBitmap(String strUrl) {
        Bitmap bitmap = null;
        String fileName = strUrl.substring( strUrl.lastIndexOf('/')+1, strUrl.length() );
        File file = new File(dir, "cache_" + fileName + ".smc");
        try {
            URL url = new URL(strUrl);
            dir.mkdirs();
            if(!file.exists())
            {
                URLConnection connection = url.openConnection();
                connection.setUseCaches(true);
                Object response = connection.getContent();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int b;
                while((b = ((InputStream)response).read()) != -1)
                {
                    stream.write(b);
                }
                byte[] buffer = stream.toByteArray();

                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            }
            else
            {
                FileInputStream fileInputStream = new FileInputStream(file);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                int b;
                while((b = fileInputStream.read()) != -1)
                {
                    stream.write(b);
                }
                byte[] buffer = stream.toByteArray();

                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void ereaseBitmapCache()
    {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }
}
