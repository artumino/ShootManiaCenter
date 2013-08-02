package com.artum.shootmaniacenter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.artum.shootmaniacenter.adapters.feedsAdapter;
import com.artum.shootmaniacenter.adapters.global.Variables;
import com.artum.shootmaniacenter.menu.MenuDrawerClass;
import com.artum.shootmaniacenter.adapters.menuItemAdapter;
import com.artum.shootmaniacenter.structures.RSS.FeedMessage;
import com.artum.shootmaniacenter.utilities.BufferBitmap;
import com.artum.shootmaniacenter.utilities.NadeoDataSeeker;
import com.artum.shootmaniacenter.utilities.RSS.RssParseHandler;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by artum on 10/06/13.
 */

public class NewsReader extends Activity {

    ListView listView;
    feedsAdapter mAdapter;
    ArrayList<FeedMessage> feedMessages;

    _getNewsFeeds task = new _getNewsFeeds();
    _getNewsFeedsImages taskImg = new _getNewsFeedsImages();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader);

        Variables.LoadVariables(this);

        feedMessages = new ArrayList<FeedMessage>();
        listView = (ListView)findViewById(R.id.feedList);
        mAdapter = new feedsAdapter(this, feedMessages);

        setupDrawer();


            //SHOOTMANIA OFFICIAL NEWS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://blog.maniaplanet.com/blog/tag/shootmania-2/feed/");
        else
            task.execute("http://blog.maniaplanet.com/blog/tag/shootmania-2/feed/");


        /*
            //SHOOTMANIA ITALIA NEWS
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "http://www.shootmania.it/feed/");
        else
            task.execute("http://www.shootmania.it/feed/");
        */

        //new _getNewsFeeds().execute("http://mania-actu.com/rss/news-en-sm.xml");



        AdapterView.OnItemClickListener newsListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(feedMessages != null && feedMessages.size() > 1)
                {
                    //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedMessages.get(i).getGuid()));
                    //startActivity(browserIntent);
                    Intent intent = new Intent(NewsReader.this, ShowNews.class);
                    intent.putExtra("title", feedMessages.get(i).getTitle());
                    intent.putExtra("content", feedMessages.get(i).getEncodedcontent());
                    intent.putExtra("date", feedMessages.get(i).getPubdate());
                    intent.putExtra("author", feedMessages.get(i).getAuthor());
                    startActivity(intent);
                }
            }
        };

        listView.setOnItemClickListener(newsListener);
    }

    private class _getNewsFeeds extends AsyncTask<String, Void, List<FeedMessage>> {

        @Override
        protected List<FeedMessage> doInBackground(String... urls) {
            String feed = urls[0];
            try {
                URL url = new URL(feed);
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                NadeoDataSeeker seeker = new NadeoDataSeeker();

                RssParseHandler rh = new RssParseHandler();

                xr.setContentHandler(rh);
                xr.parse(new InputSource(url.openStream()));

                setProgress(1);
                return rh.getItems();
            }
            catch (IOException e) {
                Log.e("RSS Handler IO", e.getMessage() + " >> " + e.toString());
            } catch (SAXException e) {
                Log.e("RSS Handler SAX", e.toString());
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                Log.e("RSS Handler Parser Config", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<FeedMessage> messages) {
            if(messages != null && messages.size() > 0)
            {
                for(FeedMessage message : messages)
                    feedMessages.add(message);
                listView.setAdapter(mAdapter);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                    taskImg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
                else
                    taskImg.execute("");
            }
            else
            {
                FeedMessage error = new FeedMessage();
                error.setAuthor("artum");
                error.setTitle("Cannot Retrive News");
                error.setDescription("Errore retriving information from the news feed, check your connection!");
                feedMessages.add(error);
                listView.setAdapter(mAdapter);
            }
    }
    }

    private class _getNewsFeedsImages extends AsyncTask<String, Void, List<FeedMessage>> {

        @Override
        protected List<FeedMessage> doInBackground(String... urls) {
            for(int i = 0; i < feedMessages.size(); i++)
            {
                if(!isCancelled() && getFirstHTMLImage(feedMessages.get(i).getEncodedcontent()) != null)
                {
                    Bitmap bitmap = BufferBitmap.loadBitmap(getFirstHTMLImage(feedMessages.get(i).getEncodedcontent()));
                    if(bitmap == null && getFirstHTMLImage(feedMessages.get(i).getDescription()) != null)
                        bitmap = BufferBitmap.loadBitmap(getFirstHTMLImage(feedMessages.get(i).getDescription()));
                    feedMessages.get(i).setImage(bitmap);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<FeedMessage> messages) {
            listView.invalidateViews();
        }
    }


    private String getFirstHTMLImage(String content)
    {
        if(content != null)
        {
            Pattern pattern = Pattern.compile("<img.+src=[\\'\"]([^\\'\"]+)[\\'\"].*>");
            Matcher matcher = pattern.matcher(content);
            if(matcher.find())
                return matcher.group(1);
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="MENU DRAWER CODE">

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;

    private void setupDrawer()      //Insert this code in OnCreate function!
    {

        String[] mTitles = getResources().getStringArray(R.array.menu_items);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerList = (ListView)findViewById(R.id.navigation_list);

        ViewGroup.LayoutParams params= mDrawerList.getLayoutParams();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        double inches = Math.sqrt((metrics.widthPixels*metrics.widthPixels) + (metrics.heightPixels*metrics.heightPixels)) / metrics.densityDpi;

        if(inches > 5)
            params.width= (metrics.widthPixels / 100) * 50;
        else
            params.width = metrics.widthPixels;
        mDrawerList.setLayoutParams(params);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new menuItemAdapter(this, mTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            MenuDrawerClass menuDrawerClass = new MenuDrawerClass();
            mDrawerLayout.closeDrawers();
            menuDrawerClass.selectItem(position, NewsReader.this);
            if(task != null && task.getStatus() == AsyncTask.Status.RUNNING)
                task.cancel(true);
            if(taskImg != null && taskImg.getStatus() == AsyncTask.Status.RUNNING)
                taskImg.cancel(true);
        }
    }
    // </editor-fold>

}