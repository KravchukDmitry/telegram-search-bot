import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ApiClient {
    public static Proxy getRandomProxy() throws IOException{
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet("http://pubproxy.com/api/proxy?country=RU&type=http");
        HttpResponse response = client.execute(get);
        String json = EntityUtils.toString(response.getEntity());
        ProxyData proxyData = new Gson().fromJson(json, ProxyData.class);
        Proxy randomProxy = proxyData.getProxies().get(0);
        System.out.println("Set random prosy: " + randomProxy);
        return  randomProxy;
    }

}
