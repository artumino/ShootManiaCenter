package com.artum.shootmaniacenter.utilities;

import java.lang.String;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 20/05/13.
 */
public class HtmlFormatter {

    public String fromStringToHtml (String base)
    {
        String temp = base;

        Pattern pattern = Pattern.compile("\\$(([a-fA-F0-9]){3}){1,2}");
        Matcher matcher = pattern.matcher(temp);
        UnicodeInterpreter unicodeInterpreter = new UnicodeInterpreter();

        while(matcher.find())
        {
            if (matcher.group().length() < 6)
            {
                temp = temp.replace(matcher.group(), "</font><font color=\"#" + matcher.group().charAt(1) + matcher.group().charAt(1) + matcher.group().charAt(2) + matcher.group().charAt(2) + matcher.group().charAt(3) + matcher.group().charAt(3) + "\">");
            }
            else
                temp = temp.replace(matcher.group(), "</font><font color=\"" + matcher.group().replace('$', '#') + "\">");
        }

        temp = unicodeInterpreter.resolveString(temp);

        pattern = Pattern.compile("\\$l(\\[+.+\\])+(.+)\\$l");
        matcher = pattern.matcher(temp);

        while (matcher.find())
        {
            String link = matcher.group(1).replace("\\", "");
            temp = temp.replace(matcher.group(), "<a href='http://" + link + "'>" + matcher.group(2) + "</a>");
        }

        temp = temp.replaceAll("\\$l(\\[+.+\\])", "");
        temp = temp.replace("$i", "<i>");
        temp = temp.replace("$g", "</font><font color='black'>");
        temp = temp.replace("$o", "<strong>");
        temp = temp.replace("$s", "");
        temp = temp.replace("$w", "");
        temp = temp.replace("$n", "");
        temp = temp.replace("$m", "</strong></i>");
        temp = temp.replace("$z", "</strong></i></font><font color='#000000'>");
        temp = temp.replace("$t", "");
        temp = temp.replace("$l", "");
        temp = temp.replace("$$", "");

        return temp;
    }

}
