package com.libcorp.shootmaniacenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by artum on 03/07/13.
 */

public class ShowNews extends Activity{



    WebView mContent;
    TextView mTitle;
    TextView mAuthor;
    TextView mDate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_show);

        Intent intent = getIntent();

        String author = intent.getStringExtra("author");
        String content = intent.getStringExtra("content");
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");

        mTitle = (TextView)findViewById(R.id.news_title);
        mAuthor = (TextView)findViewById(R.id.news_author);
        mDate = (TextView)findViewById(R.id.news_date);
        mContent = (WebView)findViewById(R.id.news_content);

        //mContent.getSettings().setLoadWithOverviewMode(true);
        //mContent.getSettings().setUseWideViewPort(true);

        mAuthor.setText(author);
        mTitle.setText(title);
        mDate.setText(date);

        String text = "<html><head>"
                + "<style type=\"text/css\">body{color: #fff; background-color: #444;text-align:center;}img{max-width:99%;height: auto; margin: auto;}a:link {color: #999999; text-decoration: underline; }" +
                "a:active {color: #999999; text-decoration: underline; }" +
                "a:visited {color: #999999; text-decoration: underline; }" +
                "a:hover {color: #999999; text-decoration: none; }"
                + "</style></head>"
                + "<body>"
                + content
                + "</body></html>";
        mContent.loadDataWithBaseURL(null, text, "text/html", "utf-8", null);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;

        }

        return true;
    }

}