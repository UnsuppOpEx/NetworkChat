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

        /**
         * Вывводит текст message в консоль
         * @param message
         */
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        /**
         * Выводит имя участника просоеденивщегося к чату
         * @param userName
         */
        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage("Участник с именем " + userName + " присоединился к чату");
        }

        /**
         * Выводит имя участника покинувшего чат
         * @param userName
         */
        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage("Участник с именем " + userName + " покинул чат");
        }

        /**
         * Уст. значение поля clientConnected и пробуждает осн. поток класса Client
         * @param clientConnected
         */
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        /**
         * Представление клиента серверу
         * @throws IOException
         * @throws ClassNotFoundException
         */
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                while (connection.receive().getType() == MessageType.NAME_REQUEST) {
                    Message message = new Message(MessageType.USER_NAME, getUserName());
                    connection.send(message);
                }
                if (connection.receive().getType() == MessageType.NAME_ACCEPTED) {
                    notifyConnectionStatusChanged(true);
                } else
                    throw new IOException("Unexpected MessageType");
                break;
            }
        }

        /**
         * Цикл обработки сообщений сервера
         * @throws IOException
         * @throws ClassNotFoundException
         */
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() != null) {

                    switch (message.getType()) {
                        case TEXT:
                            processIncomingMessage(message.getData());
                            break;
                        case USER_ADDED:
                            informAboutAddingNewUser(message.getData());
                            break;
                        case USER_REMOVED:
                            informAboutDeletingNewUser(message.getData());
                            break;
                        default:
                            throw new IOException("Unexpected MessageType");
                    }
                } else {
                    throw new IOException("Unexpected MessageType");
                }
            }

        }

    }

    /**
     * Создаёт новый поток полученный с помощью getSocketThread
     */
    public void run() {
        SocketThread socketThread = getSocketThread();
        //Создаёт и запускает поток демон
        socketThread.setDaemon(true);
        socketThread.start();

        //Синхонизируем и ожидаем пока не будет пробуждён
        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                ConsoleHelper.writeMessage(e.getMessage());
                return;
            }
        }

        if (clientConnected)
            ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
        else
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");

        while (clientConnected) {
            String text = ConsoleHelper.readString();
            if (text.toLowerCase().equals("exit"))
                break;

            if (shouldSendTextFromConsole())
                sendTextMessage(text);
        }
    }

    /**
     * Создаёт новый объект типа Client с вызовом run
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client();
        client.run();

    }
}
