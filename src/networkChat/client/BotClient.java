package networkChat.client;

import java.io.IOException;

public class BotClient extends Client {
    public class BotSocketThread extends SocketThread {

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
