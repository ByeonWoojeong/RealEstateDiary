package app.cosmos.ghrealestatediary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class MarketVersionChecker {

    public static String getMarketVersion(String packageName) {
        try {
            Document doc = Jsoup.connect( "https://play.google.com/store/apps/details?id=" + packageName).get();
            Elements Version = doc.select(".content");
            for (Element mElement : Version) {
                if (mElement.attr("itemprop").equals("softwareVersion")) {
                    return mElement.text().trim();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

