import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    public ArrayList<Advert> getAdverts(String searchUrl){
        // TODO проврека валидноси переданного URL и замена пробелов в начале и конце
        if (searchUrl.startsWith("https://www.avito.ru")) {
            return parseByJsoup(searchUrl);
        }
        return null;
    }
    private  ArrayList<Advert> parseByJsoup(String searchUrl) {
        ArrayList<Advert> adverts = new ArrayList<>();
        Document doc = null;
        try {
            Connection connection = Jsoup.connect(searchUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
            doc = connection.get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("ошибка получения html");
        }
        for(Element advertElem : doc.select("[data-type='1']")){
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
