package networkChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для чтения или записи в консоль
 */
public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    /**
     * Считывает строку с консоли
     * @return
     */
    public static String readString() {
        String line = null;
        while ( line == null) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                System.out.println("Произошла ошибка при попытке ввода текста. Попробуйте еще раз.");
            }
        }
        return line;
    }

    /**
     * Возвращает введёное число
     * @return
     */
    public static int readInt() {
        int value;
        while (true) {
            try {
                return value = Integer.parseInt(readString());
            } catch (Exception e) {
                System.out.println("Произошла ошибка при попытке ввода числа. Попробуйте еще раз.");
            }
        }
    }
}
