/**
 * Created by saian on 18.08.16.
 */
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SimpleBot extends TelegramLongPollingBot {

    // ней содержится команда(ключ) и значение(это цифра идентификатор) для того что бы идентифицировать что ввел пользователь
    private static Map<String, Integer> comandMap = new HashMap<String, Integer>();//

    private HashSet<Master> listMasters;

    public static void main(String[] args) {
        inicialize();//fill up comandMap our comands and their keys
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new SimpleBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "MedvedBot";
    }

    @Override
    public String getBotToken() {
        return "261871245:AAHJOn0vvKTi3Fl4a3GchH_CH-AOXepuK3k";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            handlerIncomingMessage(message);
        }

    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplayToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //формирование текста с описанием для вывода пользователя
    private String getDescriptionComands()
    {
        String descriptionComands = " /masters Выберите номер мастера из списка \n" +
                                    "и добное для вас время \n" +
                                    " /bonus проверить бонусные баллы \n ";
        return descriptionComands;
    }

    //заполнение хэшмап
    private static void  inicialize(){
        comandMap.put("/help", 1);
        comandMap.put("/start", 2);
        comandMap.put("/masters", 3);
    }
    private void handlerIncomingMessage(Message message)
    {
        String stringMess="";
        switch (comandMap.get(message.getText())) {
            case 3://  matches command /masters
                //не уверен что это правильно каждый раз создавать обьект базы данных
                DbDAO dbDAO = new DbDAO();
                listMasters = dbDAO.getListMasters();
                //create answer for comand /masters
                stringMess=outputListMasters();

                break;

            case 2:// matches command /start
                stringMess= "Теперь вы можете записаться к нам нажатием 2 кнопок  \n " + getDescriptionComands();
                break;
            case 1:
                stringMess="Бот для записи в парикмахерскую  \n "+getDescriptionComands();
                break;
            default:
                stringMess =  "Возможно вы ввели не верную команду" + getDescriptionComands();
        }
        sendMsg(message, stringMess);
    }
    private String outputListMasters() {
            String outputMasters = "";

        if (listMasters!=null && !listMasters.isEmpty()){
            Iterator<Master> i1 = listMasters.iterator();
            outputMasters ="ВЫберите мастера  \n ";
            while(i1.hasNext()){
                Master master=i1.next();
                outputMasters += "/"+master.getComandMaster()+" "+master.getName()+"\n ";

            }
            return outputMasters;
        }
        else
        // должна обрабатываться ошибка почему так произошло что нет мастреров.Должен писать в лог
            outputMasters = "Нет Мастеров.Позвоните нам +7914111111";

        return outputMasters;
    }
}
