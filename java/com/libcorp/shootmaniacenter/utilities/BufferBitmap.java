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
 *
 * Methods to read & cache Bitmap images
 */
public class BufferBitmap {

    static File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.libcorp.shootmaniacenter/files/cache");

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
                InputStream response = (InputStream)connection.getContent();

                byte[] buffer = bufferedRead(response);

                bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);

                file.createNewFile();

                FileOutputStream outputStream = new FileOutputStream(file);
                outputStream.write(buffer);
                outputStream.close();
            }
            else
                bitmap = BitmapFactory.decodeFile(file.getPath());

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

    public static byte[] bufferedRead(InputStream is)
            throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        byte[] buffer = new byte[16384];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1)
        {
            stream.write(buffer, 0, bytesRead);
        }

        return stream.toByteArray();
    }

}
