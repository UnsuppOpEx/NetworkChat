package networkChat.client;

import networkChat.Connection;
import networkChat.ConsoleHelper;
import networkChat.Message;
import networkChat.MessageType;

import java.io.IOException;

/**
 * Класс клиент
 */
public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    /**
     * Запрашивает ввод адреса сервера
     * @return
     * @throws IOException
     */
    protected String getServerAddress() throws IOException {
        ConsoleHelper.writeMessage("Введите адрес сервера");
        String address = ConsoleHelper.readString();
        return address;
    }

    /**
     * Запрашивает ввод порта сервера
     * @return
     * @throws IOException
     */
    protected int getServerPort() throws IOException {
        ConsoleHelper.writeMessage("Введите порт");
        int port = ConsoleHelper.readInt();
        return port;
    }

    /**
     * Запрашивает имя пользователя
     * @return
     * @throws IOException
     */
    protected String getUserName() throws IOException {
        ConsoleHelper.writeMessage("Введите имя пользователя");
        String userName = ConsoleHelper.readString();
        return userName;
    }

    /**
     * Отправляем текст всегда в консоль
     * @return
     */
    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    /**
     * Возвращает новый объект SocketThread
     * @return
     */
    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    /**
     * Создает и отправляет новое текстовое сообщение
     * @param text
     */
    protected void sendTextMessage(String text) {
        try {
            Message message = new Message(MessageType.TEXT, text);
            connection.send(message);
        } catch (IOException e) {
            ConsoleHelper.writeMessage(e.getMessage());
            clientConnected = false;
        }
    }

    public class SocketThread extends Thread {

    }
}
