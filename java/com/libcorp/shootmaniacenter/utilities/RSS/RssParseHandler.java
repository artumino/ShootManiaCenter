package com.libcorp.shootmaniacenter.utilities.RSS;

import com.libcorp.shootmaniacenter.global.Variables;
import com.libcorp.shootmaniacenter.structures.RSS.FeedMessage;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jacop_000 on 10/06/13.
 */
public class RssParseHandler extends DefaultHandler {


    private static final int ARTICLES_LIMIT = 10;
    // List of items parsed
    private List<FeedMessage> articleList;
    // We have a local reference to an object which is constructed while parser is working on an item tag
    // Used to reference item while parsing
    private FeedMessage currentArticle;

    public RssParseHandler() {
        articleList = new ArrayList();
        currentArticle = new FeedMessage();
    }
    // We have an access method which returns a list of items that are read from the RSS feed. This method will be called when parsing is done.
    public List<FeedMessage> getItems() {
        return articleList;
    }

    StringBuffer chars;
    int articlesAdded = 0;
    // The StartElement method creates an empty RssItem object when an item start tag is being processed. When a title or link tag are being processed appropriate indicators are set to true.
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        chars = new StringBuffer();
    }

    public void characters(char ch[], int start, int length) {
        chars.append(new String(ch, start, length));
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {

        if(localName.equalsIgnoreCase("lastBuildDate"))
        {
            Variables.news_lastUpdate = chars.toString();
        }

        if (localName.equalsIgnoreCase("title")){
            currentArticle.setTitle(chars.toString());
        } else if (localName.equalsIgnoreCase("description")){
            currentArticle.setDescription(chars.toString());
        } else if (localName.equalsIgnoreCase("pubDate")){
            currentArticle.setPubDate(chars.toString());
        } else if (localName.equalsIgnoreCase("link")){
            currentArticle.setGuid(chars.toString());
        } else if (localName.equalsIgnoreCase("creator")){
            currentArticle.setAuthor(chars.toString());
        } else if (localName.equalsIgnoreCase("encoded")){
            currentArticle.setEncodedContent(chars.toString());
        } else if (localName.equalsIgnoreCase("item")){
            articleList.add(currentArticle);
            currentArticle = new FeedMessage();

            // Lets check if we've hit our limit on number of articles
            articlesAdded++;
            if (articlesAdded >= ARTICLES_LIMIT){
                //throw new SAXException();
            }
        }
    }
}