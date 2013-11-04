package com.libcorp.shootmaniacenter.structures.RSS;

import android.graphics.Bitmap;

/**
 * Created by artum on 10/06/13.
 */
public class FeedMessage {
    String title;
    String description;
    String link;
    String author;
    String guid;
    String pubdate;
    String encodedcontent;
    Bitmap image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setEncodedContent(String contet)
    {
        this.encodedcontent = contet;
    }

    public String getEncodedcontent()
    {
        return encodedcontent;
    }

    public void setPubDate(String date)
    {
        this.pubdate = date;
    }

    public String getPubdate()
    {
        return pubdate;
    }

    public void setImage(Bitmap bitmap)
    {
        this.image = bitmap;
    }

    public Bitmap getImage()
    {
        return image;
    }

    @Override
    public String toString() {
        return "FeedMessage [title=" + title + ", description=" + description
                + ", link=" + link + ", author=" + author + ", guid=" + guid
                + ", encodedcontent=" + encodedcontent + ", pubdate=" + pubdate
                + "]";
    }
}
