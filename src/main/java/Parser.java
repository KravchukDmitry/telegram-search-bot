import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

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
        Document doc = getDocument(searchUrl);
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

    protected String getRandomUserAgent(){
        ArrayList<String> userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        String userAgent = userAgents.get(new Random().nextInt(userAgents.size()));
        System.out.println("Set UA : " + userAgent);
        return userAgent;
    }

    protected String getRandomReferrer(){
        ArrayList<String> referrers = new ArrayList<>();
        referrers.add("https://www.google.com");
        referrers.add("https://yandex.ru/");
        referrers.add("https://www.bing.com/");
        String referrer = referrers.get(new Random().nextInt(referrers.size()));
        System.out.println("Set referrer : " + referrer);
        return referrer;
    }

    protected Proxy gerRandomProxy() {
        ArrayList<Proxy> proxies = new ArrayList<>();
        proxies.add(new Proxy("35.245.145.147", "8080"));
        proxies.add(new Proxy("123.200.13.54", "8080"));
        proxies.add(new Proxy("103.224.48.73", "8080"));
        proxies.add(new Proxy("203.143.24.209", "8080"));
        Proxy proxy = null;
        while (true) {
            proxy = proxies.get(new Random().nextInt(proxies.size()));
            if (!proxy.isReachable())
                proxies.remove(proxy);
            else
                break;
        }
        System.out.println("Set proxy : " + proxies);
        return proxy;
    }

    protected Document getDocument(String url){
        System.out.println("Get doc from url : " + url);
        Document doc = null;
        try {
            Proxy randomProxy = gerRandomProxy();
            System.setProperty("http.proxyHost", randomProxy.getHost());
            System.setProperty("http.proxyPort", randomProxy.getPort());
            Connection connection = Jsoup.connect(url);
            connection.userAgent(getRandomUserAgent());
            connection.referrer(getRandomReferrer());
            connection.timeout(5000);
            connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("cache-control", "no-cache")
                    .header("sec-fetch-mode", "navigate")
                    .header("sec-fetch-site", "none")
                    .header("sec-fetch-user", "?1")
                    .header("pragma", "no-cache");
            doc = connection.get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("ошибка получения html");
        }
        return doc;
    }

    public class Proxy {
        protected String host;
        protected String port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public Proxy(String host, String port) {
            this.host = host;
            this.port = port;
        }

        public boolean isReachable(){
            boolean isReachable = false;
            try {
                isReachable = InetAddress.getByName(host).isReachable(1000);
            } catch (IOException ioe) {
                System.out.println("host validation error");
                ioe.printStackTrace();
            }
            return isReachable;
        }

        @Override
        public String toString() {
            return "Proxy host : " + host + " port : " + port;
        }
    }
}
