import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SearchBot extends TelegramLongPollingBot {
    String mode;
    String desk;

    public SearchBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        System.out.println("Чат:" + chatId + " сообщение: " + update.getMessage().getMessageId());
        if (update.hasMessage() && update.getMessage().hasText()) {
            switch (update.getMessage().getText()){
                case "/start":
                    sendReplyKeyboard(chatId, "Выберите тип поиска:", "Разовый","По расписанию");
                    break;
                case "test":
                    sendNewAdverts(chatId, new Parser().getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=ryzen+3+2200"));
                    break;
                case "Разовый":
                    mode = "Разовый";
                    sendReplyKeyboard(chatId, "Выберите доску объявлений:", "Авито","Юла");
                    break;
                case "Авито":
                    desk = "Авито";
                    sendReplyKeyboard(chatId, "Введите строку поиска", "ryzen 1200", "ryzen 2200");
                    break;
                case "В начало":
                    mode = "";
                    desk = "";
                    sendReplyKeyboard(chatId, "Выберите тип поиска:", "Разовый","По расписанию");
                    break;
                default:
                    if(mode.equals("Разовый") && desk.equals("Авито")){
                        sendNewAdverts(chatId, new Parser().getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=" + update.getMessage().getText().replace(" ", "+")));
                        sendReplyKeyboard(chatId, "Навигация", "В начало");
                    }
                    break;
            }
        }
    }

    public String getBotUsername() {
        return "knigga_sbot";
    }

    public String getBotToken() {
        return "864092425:AAEnAhjJbHLkgVu37m1bSrE6L_fbM45cyh8";
    }

    public void sendNewAdverts(long chatId, ArrayList<Advert> adverts){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        if(adverts.size() == 0){
            message.setText("Новые объявлений не найдено");
        } else {
            message.setText("Новые объявления:");
            List<List<InlineKeyboardButton>> messageButtons = new ArrayList<>();
            for (Advert advert : adverts) {
                List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
                InlineKeyboardButton advertBtn = new InlineKeyboardButton();
                advertBtn.setText(new StringBuilder(advert.getPrice()).append("\n").append(advert.getName()).toString());
                advertBtn.setUrl(advert.getLink());
                buttonsRow.add(advertBtn);
                messageButtons.add(buttonsRow);
                InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
                markupKeyboard.setKeyboard(messageButtons);
                message.setReplyMarkup(markupKeyboard);
            }
        }
        sendMsg(message);
    }

    private void sendReplyKeyboard(long chatId, String keyboardTittle, String... buttons) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(keyboardTittle);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Первая строчка клавиатуры
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboard.add(keyboardRow);
        for (String s : buttons){
            if(s.equals("\n")){
                keyboardRow = new KeyboardRow();
                keyboard.add(keyboardRow);
            } else {
                keyboardRow.add(new KeyboardButton(s));
            }
        }
        replyKeyboardMarkup.setKeyboard(keyboard);

        message.setReplyMarkup(replyKeyboardMarkup);
        sendMsg(message);
    }


    private void sendMsg(SendMessage message){
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sleep (long seconds){
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}