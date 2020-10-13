package networkChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Основной класс сервера
 */
public class Server {
    // Потокобезопасная реализация интерфейса Map
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    /**
     * Отправляет сообщение message всем соединениям из connectionMap.
     * @param message
     */
    public static void sendBroadcastMessage(Message message) {
        try {
            Iterator<ConcurrentHashMap.Entry<String, Connection>> iterator = connectionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Connection> entry = iterator.next();
                entry.getValue().send(message);
            }
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Не удалось отправить сообщение");
        }
    }

    /**
     * Обработчик потоков
     */
    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Устанавливает контакт между сервером и клиентом
         * @param connection
         * @return
         * @throws IOException
         * @throws ClassNotFoundException
         */
        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            //Отправляет запрос имени клиента
            connection.send(new Message(MessageType.NAME_REQUEST));
            Message message = connection.receive();
            //Получает имя клиента
            String userName = message.getData();
            //Проверяет выполнение условий, если не выполняются запрашиваем снова
            if (!(message.getType() == MessageType.USER_NAME) || userName.isEmpty() || connectionMap.containsKey(userName)) {
                return serverHandshake(connection);
            }
            //Добавляет нового пользователя и соединение
            connectionMap.put(userName, connection);
            //Отправляет подтверждение об успешном добавлении имени
            connection.send(new Message(MessageType.NAME_ACCEPTED));
            return userName;
        }

        /**
         * Отправляет участникам чата сообщение с именем нового клиента
         * @param connection
         * @param userName
         * @throws IOException
         */
        private void notifyUsers(Connection connection, String userName) throws IOException {
            for (ConcurrentHashMap.Entry<String, Connection> keys : connectionMap.entrySet()) {
                String key = keys.getKey();
                if (key != userName) {
                    Message message = new Message(MessageType.USER_ADDED, key);
                    connection.send(message);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        //Запрашивает порт сервера, используя ConsoleHelper.
        try (ServerSocket serverSocket = new ServerSocket(ConsoleHelper.readInt())) {
            System.out.println("Сервер запущен");

            while (true) {
                // Ожидает когда какой-нибудь клиент подключится к сокету.
                Socket socket = serverSocket.accept();
                // Создает новый поток обработчик Handler, в котором будет происходить обмен сообщениями с клиентом.
                new Handler(socket).start();
            }
        } catch (Exception e) {
            ConsoleHelper.writeMessage(e.getMessage());
        }
    }
}
