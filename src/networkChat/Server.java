package networkChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Основной класс сервера
 */
public class Server {
    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
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
