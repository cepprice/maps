package ru.cepprice.maps.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import androidx.annotation.NonNull;

public class RegionParser {

    private XmlPullParser mParser;

    public RegionParser(@NonNull XmlPullParser parser) {
        mParser = parser;
    }

    public void next() {
        try {
            mParser.next();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void setPointerToCountries()  {
        while (mParser.getDepth() != 3) {
            try {
                mParser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isPointerInsideCountries() {
        return !getNameAttr().equals("World_basemap");
    }

    public boolean isTagStart() {
        try {
            return mParser.getEventType() == XmlPullParser.START_TAG;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isCountry() {
        return mParser.getDepth() == 3;
    }

    public boolean isRegion() {
        return mParser.getDepth() == 4;
    }

    public boolean isSubRegion() {
        return mParser.getDepth() == 5;
    }

    public boolean isSubSubRegion() {
        return mParser.getDepth() == 6;
    }

    public String getNameAttr() {
        int nameIdx = getAttrIdx("name");
        if (nameIdx == -1) return "";
        return mParser.getAttributeValue(nameIdx);
    }

    public Boolean hasMap() {
        return getTypeAttr().equals("map") ||
                getMapAttr().equals("yes") ||
                (getMapAttr().isEmpty() && getTypeAttr().isEmpty());
    }

    public String getInnerDownloadPrefixAttr() {
        int prefixIdx = getAttrIdx("inner_download_prefix");
        if (prefixIdx == -1) return "";
        String attrValue = mParser.getAttributeValue(prefixIdx);
        return attrValue.equals("$name") ? getNameAttr() : attrValue;
    }

    public String getTranslateAttr() {
        int translateIdx = getAttrIdx("translate");
        if (translateIdx == -1) return "";
        return mParser.getAttributeValue(translateIdx);
    }

    private String getTypeAttr() {
        int typeIdx = getAttrIdx("type");
        if (typeIdx == -1) return "";
        return mParser.getAttributeValue(typeIdx);
    }

    private String getMapAttr() {
        int mapIdx = getAttrIdx("map");
        if (mapIdx == -1) return "";
        return mParser.getAttributeValue(mapIdx);
    }

    private int getAttrIdx(String attrName) {
        for (int i = 0; i < mParser.getAttributeCount(); i++) {
            if (mParser.getAttributeName(i).equals(attrName)) return i;
        }
        return -1;
    }
}