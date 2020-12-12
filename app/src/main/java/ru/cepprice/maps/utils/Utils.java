package ru.cepprice.maps.utils;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

import ru.cepprice.maps.data.Region;

public class Utils {

    private static final String sCommonSuffix = "_europe_2.obf.zip";

    public static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static ArrayList<Region> parseXml(XmlPullParser p)  {
        RegionParser parser = new RegionParser(p);
        int countriesCount = 49;
        ArrayList<Region> countries = new ArrayList<>(countriesCount);

        parser.setPointerToCountries();
        while (parser.isPointerInsideCountries()) {

            if (!parser.isTagStart()) {
                parser.next();
                continue;
            }

            if (parser.isCountry()) {
                Region country = getRegion(parser, parser.getNameAttr());
                countries.add(country);
                Log.d("M_Utils", "Added country: " + country);
            }

            if (parser.isRegion()) {
                int idxOfLastCountry = countries.size() - 1;
                Region parentCountry = countries.get(idxOfLastCountry);
                Region region = getRegion(parser, parentCountry.getInnerDownloadPrefix());
                parentCountry.getSubRegions().add(region);
                Log.d("M_Utils", "  └─Added region: " + region);
            }

            if (parser.isSubRegion()) {
                int idxOfLastCountry = countries.size() - 1;
                int idxOfLastRegion = countries.get(idxOfLastCountry).getSubRegions().size() - 1;
                Region parentRegion = countries
                        .get(idxOfLastCountry)
                        .getSubRegions()
                        .get(idxOfLastRegion);

                // Region has neither map nor inner_download_prefix to be downloaded,
                // so no need to add it
                if (parentRegion.getInnerDownloadPrefix().length() != 0) {
                    Region subRegion = getRegion(parser, parentRegion.getInnerDownloadPrefix());
                    parentRegion.getSubRegions().add(subRegion);
                    Log.d("M_Utils", "    └─Added sub region: " + subRegion);
                }
            }

            if (parser.isSubSubRegion()) {
                // TODO: add or not add?
            }

            parser.next();
        }
        Log.d("M_Utils", "Completed parsing");
        return countries;
    }

    private static Region getRegion(RegionParser parser, String prefix) {
        String name;
        String translate = parser.getTranslateAttr();

        name = translate.isEmpty() ? parser.getNameAttr() : getTranslatedName(translate);

        return new Region(
                Utils.capitalize(name),
                parser.hasMap(),
                getDownloadName(parser, prefix),
                parser.getInnerDownloadPrefixAttr(),
                new ArrayList<>());
    }

    private static String getDownloadName(RegionParser parser, String prefix) {
        if (!parser.hasMap()) return "";

        if (parser.isCountry()) {
            return Utils.capitalize(parser.getNameAttr()) + sCommonSuffix;
        }

        // Regions and sub region left
        return Utils.capitalize(prefix) + "_" + parser.getNameAttr() + sCommonSuffix;

    }

    private static String getTranslatedName(String translate) {
        int end = translate.indexOf(";");
        int start = translate.indexOf("=") + 1;
        if (end == -1) end = translate.length();
        if (start > end) start = 0;

        try {
            return translate.substring(start, end);
        } catch (StringIndexOutOfBoundsException e) {
            Log.d("M_Utils", "Catch");
        }
        return "";
    }
}
