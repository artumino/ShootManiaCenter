package com.artum.shootmaniacenter.utilities;

import java.lang.Character;import java.lang.Integer;import java.lang.String;import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by artum on 24/05/13.
 */
public class UnicodeInterpreter
{
    public String resolveString(String temp)
    {
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(temp);

        while (matcher.find())
        {
            int c = Integer.parseInt(matcher.group().substring(2), 16);
            String s = Character.toString((char)c);
            temp = temp.replace(matcher.group(), s);
        }
        return temp;
    }
}
