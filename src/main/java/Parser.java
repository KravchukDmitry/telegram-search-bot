import network.Skynet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {

    public static List<Advert> getAdverts(String searchUrl) {
        // TODO проврека валидноси переданного URL и замена пробелов в начале и конце
        if (searchUrl.startsWith("https://www.avito.ru")) {
                return parseAdverts(Skynet.getPage(searchUrl, true));
        }
        return null;
    }
    private static List<Advert> parseAdverts(Document document) {
        List<Advert> adverts = new ArrayList<>();
        for(Element advertElem : document.select("div.js-catalog_serp > div[data-type='1']")){
            Advert advert = new Advert();
            advert.setName(advertElem.selectFirst("span[itemprop='name']").text());
            advert.setLink("https://www.avito.ru" + advertElem.selectFirst("span[itemprop='name']").parent().attr("href"));
            advert.setPrice(advertElem.selectFirst("span[itemprop='price']").attr("content"));
            advert.setDate(advertElem.selectFirst("div[data-marker='item-date']").attr("data-absolute-date").trim());
            adverts.add(advert);
        }
        return adverts;
    }
}
