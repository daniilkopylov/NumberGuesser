import java.io.IOException;
import java.util.Random;

public class NumberGuesser {

    public static void main(String[] args) {
        // Инициализация игры
        Random random = new Random();
        int targetNumber = random.nextInt(100) + 1;      // Случайное число от 1 до 100
        int maxAttempts = 7;                                    // Ограниченное число попыток для диапазона
        int attemptsUsed = 0;

        System.out.println("=== ИГРА: УГАДАЙ ЧИСЛО ===");
        System.out.println("Я загадал число от 1 до 100.");
        System.out.println("У вас есть " + maxAttempts + " попыток, чтобы угадать его.\n");

        while (attemptsUsed < maxAttempts) {
            System.out.println("Попытка " + (attemptsUsed + 1) + " (осталось " + (maxAttempts - attemptsUsed) + ").");
            System.out.println("Введите ваше предположение (от 1 до 100):");

            try {
                long accumulatedNumber = 0;
                boolean hasDigits = false;
                boolean isNegative = false;
                boolean isOverflow = false;
                boolean isInvalidChar = false;

                // Посимвольное чтение без буфера
                while (true) {
                    int code = System.in.read();

                    if (code == '\n' || code == '\r' || code == -1) {
                        if (code == '\r' && System.in.available() > 0) {
                            System.in.read();
                        }
                        break;
                    }

                    char ch = (char) code;

                    if (!hasDigits && ch == '-') {
                        isNegative = true;
                        continue;
                    }

                    if (ch >= '0' && ch <= '9') {
                        accumulatedNumber = accumulatedNumber * 10 + (ch - '0');
                        hasDigits = true;

                        if (accumulatedNumber > Integer.MAX_VALUE) {
                            isOverflow = true;
                        }
                    } else {
                        isInvalidChar = true;
                        // Защита от бесконечного цикла: вычищаем мусор из буфера ОС
                        while (System.in.available() > 0) {
                            int next = System.in.read();
                            if (next == '\n' || next == '\r') break;
                        }
                        break;
                    }
                }

                // Матрица валидации ввода
                if (isInvalidChar) {
                    System.out.println("Ошибка: вводите только цифры.\n");
                    continue;
                }
                if (!hasDigits) {
                    System.out.println("Ошибка: вы ничего не ввели.\n");
                    continue;
                }
                if (isNegative) {
                    System.out.println("Ошибка: загаданное число не может быть отрицательным.\n");
                    continue;
                }
                if (isOverflow || accumulatedNumber > 100 || accumulatedNumber < 1) {
                    System.out.println("Ошибка: число должно быть в строго диапазоне от 1 до 100.\n");
                    continue;
                }

                // Ввод прошел все проверки
                int userGuess = (int) accumulatedNumber;
                attemptsUsed++;

                // Механизм проверки и подсказок
                if (userGuess == targetNumber) {
                    System.out.println("\n Поздравляем! Вы угадали число " + targetNumber + " за " + attemptsUsed + " попыток!");
                    return; // Успешное завершение игры
                } else if (userGuess < targetNumber) {
                    System.out.println("Неверно. Моё число БОЛЬШЕ, чем " + userGuess + ".\n");
                } else {
                    System.out.println("Неверно. Моё число МЕНЬШЕ, чем " + userGuess + ".\n");
                }

            } catch (IOException e) {
                System.out.println("Критическая ошибка ввода-вывода: " + e.getMessage());
                break;
            }
        }

        // Финал игры при исчерпании попыток
        System.out.println("=== ИГРА ОКОНЧЕНА ===");
        System.out.println("К сожалению, у вас закончились попытки.");
        System.out.println("Я загадал число: " + targetNumber);
    }
}
