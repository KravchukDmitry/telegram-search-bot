package network;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import exceptions.PageGettingException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class Skynet {
    private static Proxy currentProxy;


    public static Document getPage(String searchUrl, boolean proxyEnabled) {
        try {
            return Skynet.getPageByJsoup(searchUrl, proxyEnabled);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error on getPageByJsoup");
            try {
                return Skynet.getPageByHtmlUnit(searchUrl, proxyEnabled);
            } catch (IOException ee) {
                ee.printStackTrace();
                log.error("Error on getPageByHtmlUnit. Current proxy doesn't work");
                currentProxy.setWorks(false);
                throw new PageGettingException(ee);
            }
        }
    }

    public static Document getPageByHtmlUnit(String searchUrl, boolean proxyEnabled) throws IOException {
        log.info("Get doc by htmlUnit from url : {}", searchUrl);
        WebClient webClient = getWebClient();
        if (proxyEnabled) {
            setProxy();
            ProxyConfig proxyConfig = new ProxyConfig(currentProxy.getIp(), currentProxy.getPort());
            webClient.getOptions().setProxyConfig(proxyConfig);
        }
        return Jsoup.parse(webClient.getPage(searchUrl).getWebResponse().getContentAsString());
    }

    public static Document getPageByJsoup(String searchUrl, boolean proxyEnabled) throws IOException {
        log.info("Get doc by Jsoup from url : {}", searchUrl);
        Connection connection = getConnection(searchUrl);
        if (proxyEnabled) {
            setProxy();
            connection.proxy(currentProxy.getIp(), currentProxy.getPort());
        }
        return connection.get();

    }


    private static String getRandomUserAgent() {
        ArrayList<String> userAgents = new ArrayList<>();
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");
        userAgents.add("Mozilla/5.0 (Macintosh; Intel Mac OS X x.y; rv:42.0) Gecko/20100101 Firefox/42.0");
        userAgents.add("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        String userAgent = userAgents.get(new Random().nextInt(userAgents.size()));
        log.info("Set user agent : {}", userAgent);
        return userAgent;
    }

    private static String getRandomReferrer() {
        ArrayList<String> referrers = new ArrayList<>();
        referrers.add("https://www.google.com");
        referrers.add("https://yandex.ru/");
        referrers.add("https://www.bing.com/");
        String referrer = referrers.get(new Random().nextInt(referrers.size()));
        log.info("Set referrer : {}", referrer);
        return referrer;
    }

    private static WebClient getWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX_52);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        return webClient;
    }

    private static void setProxy() {
        if (currentProxy == null || !currentProxy.isWorks()) {
            currentProxy = ApiClient.getRandomProxy();
        }
    }

    private static Connection getConnection(String searchUrl){
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
        return connection;
    }
}
