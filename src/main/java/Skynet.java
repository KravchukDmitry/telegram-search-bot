import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

public class Skynet {

    public static Document getPageByHtmlUnit(String searchUrl){
        System.out.println("Get doc by htmlUnit from url : " + searchUrl);
        Skynet.Proxy randomProxy = gerRandomProxy();
        System.setProperty("http.proxyHost", randomProxy.getHost());
        System.setProperty("http.proxyPort", randomProxy.getPort());
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        HtmlPage page = null;
        try {
            page = webClient.getPage(searchUrl);
        } catch (IOException ioe) {
            System.out.println("Error while getPage");
            ioe.printStackTrace();
        }
        return Jsoup.parse(page.getWebResponse().getContentAsString());
    }

    public static Document getPageByJsoup(String searchUrl) {
        System.out.println("Get doc by Jsoup from url : " + searchUrl);
        Document doc = null;
        try {
            Skynet.Proxy randomProxy = gerRandomProxy();
            System.setProperty("http.proxyHost", randomProxy.getHost());
            System.setProperty("http.proxyPort", randomProxy.getPort());
            Connection connection = Jsoup.connect(searchUrl);
            connection.userAgent(getRandomUserAgent());
            connection.referrer("https://www.avito.ru/moskva/tovary_dlya_kompyutera");
            connection.timeout(5000);
            connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
                    .header("Accept-Encoding", "gzip, deflate, br")
                    .header("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("cache-control", "no-cache")
                    .header("sec-fetch-mode", "navigate")
                    .header("sec-fetch-site", "same-origin")
                    .header("sec-fetch-user", "?1")
                    .header("pragma", "no-cache")
                    .header("upgrade-insecure-requests", "1");
            doc = connection.get();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("ошибка получения html");
        }
        return doc;
    }


    protected static String getRandomUserAgent(){
        ArrayList<String> userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        String userAgent = userAgents.get(new Random().nextInt(userAgents.size()));
        System.out.println("Set UA : " + userAgent);
        return userAgent;
    }

    protected static String getRandomReferrer(){
        ArrayList<String> referrers = new ArrayList<>();
        referrers.add("https://www.google.com");
        referrers.add("https://yandex.ru/");
        referrers.add("https://www.bing.com/");
        String referrer = referrers.get(new Random().nextInt(referrers.size()));
        System.out.println("Set referrer : " + referrer);
        return referrer;
    }

    protected static Proxy gerRandomProxy() {
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



    public static class Proxy {
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
