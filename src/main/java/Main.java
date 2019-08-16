import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Main {
    public static void main(String[] args) {
            ApiContextInitializer.init();

            // Create the TelegramBotsApi object to register your bots
            TelegramBotsApi botsApi = new TelegramBotsApi();

            // Set up Http proxy
            DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);

            if(false) {
                String adr = "127.0.0.1";
                int port = 9050;
                botOptions.setProxyHost("adr");
                botOptions.setProxyPort(port);
                // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
                System.out.println("Proxy on with " + adr + ":" + port);
            }

            // Register your newly created AbilityBot

        SearchBot bot = new SearchBot( botOptions);
        try {
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        /*while(true){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(395271714l);
            sendMessage.setText("Сообщение по таймауту");
            bot.sendMsg(sendMessage);
        }*/
    }
}
