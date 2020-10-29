package networkChat.client;

import java.util.HashSet;
import java.util.Set;

/**
 * Модель представления клиента
 */
public class ClientGuiModel {

    //Список всех участников чата
    private final Set<String> allUserNames = new HashSet();
    //Новое сообщение которое получит клиент
    private String newMessage;

    public Set<String> getAllUserNames() {
        return allUserNames;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    //Добавляет имя участника во множество Set
    public void addUser(String newUserName) {
        allUserNames.add(newUserName);
    }

    //Удаляет имя участника из множества
    public void deleteUser(String userName) {
        allUserNames.remove(userName);
    }
}
