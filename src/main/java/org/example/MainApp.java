package org.example;

import java.util.Scanner;

/**
 * Точка входа консольного приложения Java Class Analyzer.
 * Принимает путь к .class файлу, запускает анализ и выводит результат.
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String path;

        // Если путь передан через аргументы командной строки
        if (args.length > 0) {
            path = args[0];
        } else {
            // Иначе запрашиваем у пользователя
            System.out.print("Введите путь до .class файла: ");
            path = scanner.nextLine();
        }

        try {
            ClassAnalyzer analyzer = new ClassAnalyzer();
            analyzer.analyze(path);

            analyzer.printResult();

            String textToLogger = analyzer.printResultToLogger();
            LoggerConfig.logger.info("Файл успешно проанализирован\n");

        } catch (Exception e) {
            System.err.println("Ошибка анализа: " + e.getMessage());
            LoggerConfig.logger.error("Ошибка анализа", e);
        }
    }
}
