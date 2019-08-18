import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Skynet {
    private static Proxy currentProxy;


    public static Document getPage(String searchUrl, boolean  proxyEnabled) {
        Document doc = null;
        try {
            doc = Skynet.getPageByJsoup(searchUrl, proxyEnabled);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on getPageByJsoup");
            try {
                doc = Skynet.getPageByHtmlUnit(searchUrl, proxyEnabled);
            } catch (Exception ee){
                ee.printStackTrace();
                System.out.println("Error on getPageByHtmlUnit. Current proxy doesn't work");
                currentProxy.setWorks(false);
            }
        }
        return doc;
    }

    public static Document getPageByHtmlUnit(String searchUrl, boolean proxyEnabled) throws IOException {
        System.out.println("Get doc by htmlUnit from url : " + searchUrl);
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        if (proxyEnabled) {
            if(currentProxy == null || !currentProxy.isWorks())
                currentProxy = ApiClient.getRandomProxy();
            ProxyConfig proxyConfig = new ProxyConfig(currentProxy.getIp(), currentProxy.getPort());
            webClient.getOptions().setProxyConfig(proxyConfig);
        }
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        HtmlPage page = webClient.getPage(searchUrl);
        return Jsoup.parse(page.getWebResponse().getContentAsString());
    }

    public static Document getPageByJsoup(String searchUrl, boolean proxyEnabled) throws IOException{
        System.out.println("Get doc by Jsoup from url : " + searchUrl);
        Connection connection = Jsoup.connect(searchUrl);
        if (proxyEnabled) {
            if(currentProxy == null || !currentProxy.isWorks())
                currentProxy = ApiClient.getRandomProxy();
            connection.proxy(currentProxy.getIp(), currentProxy.getPort());
        }
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
        return connection.get();
    }


    private static String getRandomUserAgent(){
        ArrayList<String> userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        String userAgent = userAgents.get(new Random().nextInt(userAgents.size()));
        System.out.println("Set UA : " + userAgent);
        return userAgent;
    }

    private static String getRandomReferrer(){
        ArrayList<String> referrers = new ArrayList<>();
        referrers.add("https://www.google.com");
        referrers.add("https://yandex.ru/");
        referrers.add("https://www.bing.com/");
        String referrer = referrers.get(new Random().nextInt(referrers.size()));
        System.out.println("Set referrer : " + referrer);
        return referrer;
    }
}
