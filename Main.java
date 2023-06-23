/**
 * Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке, разделенные пробелом:
 * Фамилия Имя Отчество датарождения номертелефона пол
 * Форматы данных:
 * фамилия, имя, отчество - строки
 * дата_рождения - строка формата dd.mm.yyyy
 * номер_телефона - целое беззнаковое число без форматирования
 * пол - символ латиницей f или m.
 * Приложение должно проверить введенные данные по количеству.
 * Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и показать пользователю сообщение,
 * что он ввел меньше и больше данных, чем требуется.
 * Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
 * Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы.
 * Можно использовать встроенные типы java и создать свои.
 * Исключение должно быть корректно обработано, пользователю выведено сообщение с информацией, что именно неверно.
 * Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку должны
 * записаться полученные данные, вида
 * <Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
 * Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
 * Не забудьте закрыть соединение с файлом.
 * При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь должен
 * увидеть стектрейс ошибки.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите данные через пробел: Фамилия Имя Отчество датарождения номертелефона пол.");
        Scanner input = new Scanner(System.in);
        String[] items = input.nextLine().split(" ");

        if (items.length != 6) {
            throw new ArrayIndexOutOfBoundsException("Данные введены некорректно, проверьте число заполненных полей");
        } else {
            StringBuilder stringData = new StringBuilder();

            for (String item : items) {
                ValidateData(item, stringData);
            }

            String surname = items[0];
            WriteFile(surname, stringData);
        }
    }

    public static void WriteFile(String surname, StringBuilder sd) {
        File file = new File(surname + ".txt");
        if (file.exists()) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                pw.write(String.valueOf(sd));
                pw.close();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось записать даные в файл.");

            }
        } else {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write(String.valueOf(sd));
                fw.close();
                System.out.println("Файл с фамилией " + file + " создан");
            } catch (Exception e) {
                throw new RuntimeException("Файл не создан");
            }
        }
    }

    public static void ValidateData(String data, StringBuilder sd) {
        if (data.chars().allMatch(Character::isLetter) && data.length() == 1) {
            if (data.equals("f") || data.equals("m")) {
                sd.append("<").append(data).append(">");
            } else {
                throw new RuntimeException("Неверный ввод, введите пол в формате f/m");
            }
        } else if (data.chars().allMatch(Character::isLetter) && data.length() > 1) {
            // Проверка значения ФИО
            if (data.length() < 25) {
                sd.append("<").append(data).append(">");
            } else {
                throw new RuntimeException("Неверный ввод, введите корректное значение ФИО");
            }
        } else {
            // Проверка телефона
            if (data.length() >= 11) {
                try {
                    long phoneNumber = Long.parseLong(data);
                    sd.append("<").append(phoneNumber).append(">");
                } catch (Exception e) {
                    throw new RuntimeException("Номер телефона должен состоять из целочисленных значений.");
                }
            } else {
                // Проверка дня рождения
                String[] birthDate = data.split("\\.");

                try {
                    int day = Integer.parseInt(birthDate[0]);
                    int month = Integer.parseInt(birthDate[1]);
                    int year = Integer.parseInt(birthDate[2]);

                    if ((day > 0 && day <= 31)) {
                        sd.append("<").append(day).append(".");
                    }
                    if (month > 0 && month <= 12) {
                        sd.append(birthDate[1]).append(".");
                    }
                    if (year > 1900) {
                        sd.append(year).append(">");
                    } else {
                        throw new RuntimeException("Неверный формат даты.");
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Введены не целочисленные значения");
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new RuntimeException("Неверный формат даты. Необходимо ввести данные в формате dd.mm.yyyy");
                }
            }
        }
    }
}
