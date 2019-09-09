import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;



@Slf4j
public class Main {


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        if (System.getProperty("hostingMode") == null) { // для запуска на локальной машине запустить tor_proxy
            String tor_proxy_adr = "127.0.0.1";
            int tor_proxy_port = 9050;
            botOptions.setProxyHost(tor_proxy_adr);
            botOptions.setProxyPort(tor_proxy_port);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            log.info("Start bot with proxy server {} port {}", tor_proxy_adr, tor_proxy_port);
        }

        SearchBot bot = new SearchBot( botOptions);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
