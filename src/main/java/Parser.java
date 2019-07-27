import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.*;
import java.util.ArrayList;

public class Parser {

    public ArrayList<Advert> getAdverts(String searchUrl) {
        // TODO проврека валидноси переданного URL и замена пробелов в начале и конце
        if (searchUrl.startsWith("https://www.avito.ru")) {
            return parseAvito(searchUrl);
        }
        return null;
    }

    private ArrayList<Advert> parseAvito(String searchUrl) {
        ArrayList<Advert> adverts = new ArrayList<Advert>();
        Configuration.headless = false;
        Configuration.browser = "chrome";
        open(searchUrl);
        for (SelenideElement advertDesc : $$(By.xpath(".//div[contains(@itemtype, 'Product') and @data-item-id]"))){
            Advert advert = new Advert();
            advert.setName(advertDesc.$(By.xpath(".//span[@itemprop='name']")).getText());
            advert.setLink(advertDesc.$(By.xpath(".//span[@itemprop='name']/..")).attr("href"));
            advert.setPrice(advertDesc.$(By.xpath(".//span[@itemprop='price']")).attr("content"));
            adverts.add(advert);
        }
        close();
        return adverts;
    }
}
