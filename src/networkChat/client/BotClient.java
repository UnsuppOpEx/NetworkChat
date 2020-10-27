package networkChat.client;

import networkChat.ConsoleHelper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {

        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {

            sendTextMessage(
                    "Привет чатику. Я бот. Понимаю команды: дата, день, месяц, год, время, час, минуты, секунды."
            );
            super.clientMainLoop();
        }

        /**
         * Обрабатывает запросы от клиентов и отправляет ответ
         * @param message
         */
        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            String[] messageParts = message.split(": ");
            if (messageParts.length == 2) {
                String messageAuthor = messageParts[0];
                String messageText = messageParts[1].toLowerCase();
                String dateTimeformat = null;
                switch (messageText) {
                    case "дата":
                        dateTimeformat = "d.MM.YYYY";
                        break;
                    case "день":
                        dateTimeformat = "d";
                        break;
                    case "месяц":
                        dateTimeformat = "MMMM";
                        break;
                    case "год":
                        dateTimeformat = "YYYY";
                        break;
                    case "время":
                        dateTimeformat = "H:mm:ss";
                        break;
                    case "час":
                        dateTimeformat = "H";
                        break;
                    case "минуты":
                        dateTimeformat = "m";
                        break;
                    case "секунды":
                        dateTimeformat = "s";
                        break;
                }
                if (dateTimeformat != null) {
                    String reply = String.format("Информация для %s: %s",
                            messageAuthor,
                            new SimpleDateFormat(dateTimeformat).format(Calendar.getInstance().getTime())
                    );
                    sendTextMessage(reply);
                }
            }
        }
    }

    /**
     * Запрет отправки текста введённого в консоль
     * @return
     */
    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    /**
     * Создаёт и возвращает новый объект BotSocketThread
     * @return
     */
    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    /**
     * Генерирует новое имя бота
     * @return
     * @throws IOException
     */
    @Override
    protected String getUserName() throws IOException {
        String userName = "date_bot_" + (int) (Math.random()*100);
        return userName;
    }

    /**
     * Создаёт новый объект и вызывает у него метод run
     * @param args
     */
    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }

}
