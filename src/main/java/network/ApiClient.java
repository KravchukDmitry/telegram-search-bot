package network;

import com.google.gson.Gson;
import exceptions.PageGettingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.InetAddress;

@Slf4j
public class ApiClient {

    public static Proxy getRandomProxy() {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet("http://pubproxy.com/api/proxy?country=RU&type=http&port=8080,80&https=true");
            HttpResponse response = client.execute(get);
            String json = EntityUtils.toString(response.getEntity());
            ProxyData proxyData = new Gson().fromJson(json, ProxyData.class);
            Proxy randomProxy = proxyData.getData().get(0);
            log.info("Set random proxy: " + randomProxy);
            randomProxy.setWorks(true);
            return randomProxy;
        } catch (IOException e) {
            e.printStackTrace();
            throw new PageGettingException(e);
        }
    }

    public static boolean proxyIsReachable(Proxy proxy) {
        try {
            return InetAddress.getByName(proxy.getIp()).isReachable(1000);
        } catch (IOException ioe) {
            return false;
        }
    }

}
