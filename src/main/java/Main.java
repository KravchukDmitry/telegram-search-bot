import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    private static Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
        if (false) { // для запуска на локальной машине проставить true и запустить tor_proxy
            String adr = "127.0.0.1";
            int port = 9050;
            botOptions.setProxyHost(adr);
            botOptions.setProxyPort(port);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
            LOGGER.log(Level.INFO, "network.Proxy on with " + adr + ":" + port);
        }

        SearchBot bot = new SearchBot( botOptions);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
