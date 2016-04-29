
package com.milton.common.util;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Xml;

public class DrawableUtil {
    public static void parseXML2Drawable(Resources r, String tagName, int resID, Drawable d) {
        if (null == r || null == tagName || 0 >= resID || null == d)
            return;

        XmlResourceParser parser = r.getXml(resID);

        try {
            int type;
            while ((type = parser.next()) != XmlPullParser.START_TAG
                    && type != XmlPullParser.END_DOCUMENT) {
            }
            if (!tagName.equals(parser.getName()))
                return;

            AttributeSet attrs = Xml.asAttributeSet(parser);
            d.inflate(r, parser, attrs);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
