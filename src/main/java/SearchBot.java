import exceptions.PageGettingException;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchBot extends TelegramLongPollingBot {
    String mode;
    String desk;
    private static Logger LOGGER = Logger.getLogger(SearchBot.class.getName());

    public SearchBot(DefaultBotOptions options) {
        super(options);
    }

    public void onUpdateReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        LOGGER.log(Level.INFO, "Чат:" + chatId + " сообщение: " + update.getMessage().getMessageId());
        if (!(update.hasMessage() && update.getMessage().hasText())) {
            return;
        }
        try {
            switch (update.getMessage().getText()) {
                case "/start":
                    sendReplyKeyboard(chatId, "Выберите тип поиска:", "Разовый", "По расписанию");
                    break;
                case "test":
                    sendNewAdverts(chatId, new Parser().getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=ryzen+3+2200"));
                    break;
                case "Разовый":
                    mode = "Разовый";
                    sendReplyKeyboard(chatId, "Выберите доску объявлений:", "Авито", "Юла");
                    break;
                case "Авито":
                    desk = "Авито";
                    sendReplyKeyboard(chatId, "Введите строку поиска", "ryzen 1200", "ryzen 2200");
                    break;
                case "В начало":
                    mode = "";
                    desk = "";
                    sendReplyKeyboard(chatId, "Выберите тип поиска:", "Разовый", "По расписанию");
                    break;
                default:
                    if (mode.equals("Разовый") && desk.equals("Авито")) {
                        sendMsg(new SendMessage(chatId, "Подождите, выполняется поиск..."));
                        sendNewAdverts(chatId, Parser.getAdverts("https://www.avito.ru/moskva/tovary_dlya_kompyutera?s_trg=3&bt=1&q=" + update.getMessage().getText().replace(" ", "+")));
                        sendReplyKeyboard(chatId, "Навигация", "В начало");
                    } else {
                        sendReplyKeyboard(chatId, "Упс. Тут ничего нет...", "В начало");
                    }
                    break;

            }
        } catch (PageGettingException pe) {
            pe.printStackTrace();
            sendReplyKeyboard(chatId, "Ошибка получения объявлений", "В начало");
        }
    }

    public String getBotUsername() {
        return System.getenv("BotUsername");
    }

    public String getBotToken() {
        return System.getenv("BotToken");
    }

    public void sendNewAdverts(long chatId, List<Advert> adverts) {
        SendMessage message = new SendMessage().setChatId(chatId);
        if (adverts.size() == 0) {
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
        SendMessage message = new SendMessage().setChatId(chatId).setText(keyboardTittle);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup().setSelective(true).setResizeKeyboard(true).setOneTimeKeyboard(false);
        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Первая строчка клавиатуры
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboard.add(keyboardRow);
        for (String s : buttons) {
            if (s.equals("\n")) {
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


    private void sendMsg(SendMessage message) {
        try {
            execute(message); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}