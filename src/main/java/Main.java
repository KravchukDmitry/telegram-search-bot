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

            if(true) {
                botOptions.setProxyHost("127.0.0.1");
                botOptions.setProxyPort(9050);
                // Select proxy type: [HTTP|SOCKS4|SOCKS5] (default: NO_PROXY)
                botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);
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
