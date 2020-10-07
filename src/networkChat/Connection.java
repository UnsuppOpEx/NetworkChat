package networkChat;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Соединение между клиентом и сервером
 */
public class Connection implements Closeable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Записывает сообщение(сериализует) message в ObjectOutputStream
     * @param message
     * @throws IOException
     */
    public void send(Message message) throws IOException {
        synchronized (out) {
            out.writeObject(message);
            out.flush();
        }
    }

    /**
     * Читает данные из ObjectInputStream (десериализовывает)
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message receive() throws IOException, ClassNotFoundException {
        synchronized (in) {
            Message message = (Message) in.readObject();
            return message;
        }
    }

    /**
     * Возвращает удаленный адрес сокетного соединения
     * @return
     */
    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    /**
     * Закрывает все ресурсы класса
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        socket.close();
        out.close();
        in.close();
    }
}
