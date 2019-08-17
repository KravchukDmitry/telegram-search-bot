import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


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
        Skynet.getPageByHtmlUnit("https://www.avito.ru");
        Document doc = Skynet.getPageByHtmlUnit(searchUrl);
        for(Element advertElem : doc.select("div.js-catalog_serp > div[data-type='1']")){
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
