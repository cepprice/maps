package ru.cepprice.maps.utils;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import ru.cepprice.maps.R;
import ru.cepprice.maps.data.Region;

public class RegionProvider {

    private static final String commonSuffix = "_europe_2.obf.zip";

    private static XmlPullParser parser;

    public static ArrayList<Region> getRegions(Context context) {
        ArrayList<String> downloadedMaps = InternalStorageHelper.getDownloadedMaps(context);
        parser = context.getResources().getXml(R.xml.regions);

        int countriesCount = 49;
        ArrayList<Region> countries = new ArrayList<>(countriesCount);

        setPointerToCountries();
        while (isPointerInsideCountries()) {

            if (!isStartTag()) {
                next();
                continue;
            }

            if (isCountry()) {
                Region country = createRegion(getNameAttr(), downloadedMaps);
                countries.add(country);
                Log.d("M_Utils", "Added country: " + country);
                next();
                continue;
            }

            int idxOfLastCountry = countries.size() - 1;
            Region parentCountry = countries.get(idxOfLastCountry);

            if (isRegion()) {
                Region region = createRegion(
                        parentCountry.getInnerDownloadPrefix(), downloadedMaps);
                parentCountry.getChildRegions().add(region);
                Log.d("M_Utils", "  └─Added region: " + region);
                next();
                continue;
            }

            int idxOfLastRegion = parentCountry.getChildRegions().size() - 1;
            Region lastParentRegion = parentCountry
                    .getChildRegions()
                    .get(idxOfLastRegion);

            if (isSubRegion()) {
                Region subRegion = createRegion(
                        lastParentRegion.getInnerDownloadPrefix(), downloadedMaps);
                lastParentRegion.getChildRegions().add(subRegion);
                Log.d("M_Utils", "    └─Added sub region: " + subRegion);
                next();
                continue;
            }

            int idxOfLastSubRegion = lastParentRegion.getChildRegions().size() - 1;
            Region lastParentSubRegion = lastParentRegion
                    .getChildRegions()
                    .get(idxOfLastSubRegion);

            if (isSubSubRegion()) {
                Region subSubRegion = createRegion(
                        lastParentSubRegion.getInnerDownloadPrefix(), downloadedMaps);
                lastParentSubRegion.getChildRegions().add(subSubRegion);
                next();
            }
        }

        return countries;

    }

    private static Region createRegion(String prefix, ArrayList<String> downloadedMaps) {
        String name;
        String translate = getTranslateAttr();
        name = translate.isEmpty() ? getNameAttr() : getTranslatedName(translate);

        String downloadName = getDownloadName(prefix);

        MapState mapState = MapState.NOT_PROVIDED;
        if (isMapProvided()) {
            if (downloadedMaps.contains(downloadName)) mapState = MapState.DOWNLOADED;
            else mapState = MapState.NOT_DOWNLOADED;
        }

        return Region.builder()
                .name(Utils.capitalize(name))
                .downloadName(downloadName)
                .innerDownloadPrefix(getInnerDownloadPrefixAttr())
                .mapState(mapState)
                .build();
    }

    private static void next() {
        try {
            parser.next();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private static void setPointerToCountries()  {
        while (parser.getDepth() != 3) {
            try {
                parser.next();
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isPointerInsideCountries() {
        return !getNameAttr().equals("World_basemap");
    }

    private static boolean isStartTag() {
        try {
            return parser.getEventType() == XmlPullParser.START_TAG;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean isCountry() {
        return parser.getDepth() == 3;
    }

    private static boolean isRegion() {
        return parser.getDepth() == 4;
    }

    private static boolean isSubRegion() {
        return parser.getDepth() == 5;
    }

    private static boolean isSubSubRegion() {
        return parser.getDepth() == 6;
    }

    private static Boolean isMapProvided() {
        return getTypeAttr().equals("map") ||
                getMapAttr().equals("yes") ||
                (getMapAttr().isEmpty() && getTypeAttr().isEmpty());
    }

    // Returns download name of region, based on its name and
    // inner_download_prefix
    private static String getDownloadName(String prefix) {
        if (!isMapProvided()) return "";

        if (isCountry()) {
            return Utils.capitalize(getNameAttr()) + commonSuffix;
        }

        // Regions and sub region left
        return Utils.capitalize(prefix) + "_" + getNameAttr() + commonSuffix;

    }

    // Returns value of attribute "translate", cleared from unneeded information
    private static String getTranslatedName(String translate) {
        int end = translate.indexOf(";");
        int start = translate.indexOf("=") + 1;
        if (end == -1) end = translate.length();
        if (start > end) start = 0;

        return translate.substring(start, end);
    }

    // Returns value of attribute "name" or empty string
    private static String getNameAttr() {
        int nameIdx = getAttrIdx("name");
        if (nameIdx == -1) return "";
        return parser.getAttributeValue(nameIdx);
    }

    // Returns value of attribute "inner_download_prefix" or empty string
    private static String getInnerDownloadPrefixAttr() {
        int prefixIdx = getAttrIdx("inner_download_prefix");
        if (prefixIdx == -1) return "";
        String attrValue = parser.getAttributeValue(prefixIdx);
        return attrValue.equals("$name") ? getNameAttr() : attrValue;
    }

    // Returns value of attribute "translate" or empty string
    private static String getTranslateAttr() {
        int translateIdx = getAttrIdx("translate");
        if (translateIdx == -1) return "";
        return parser.getAttributeValue(translateIdx);
    }

    // Returns value of attribute "type" or empty string
    private static String getTypeAttr() {
        int typeIdx = getAttrIdx("type");
        if (typeIdx == -1) return "";
        return parser.getAttributeValue(typeIdx);
    }

    // Returns value of attribute "map" or empty string
    private static String getMapAttr() {
        int mapIdx = getAttrIdx("map");
        if (mapIdx == -1) return "";
        return parser.getAttributeValue(mapIdx);
    }

    // Returns either index of attribute inside current tag or -1
    private static int getAttrIdx(String attrName) {
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            if (parser.getAttributeName(i).equals(attrName)) return i;
        }
        return -1;
    }
}