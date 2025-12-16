package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Графическое приложение на JavaFX для анализа Java class-файлов.
 * <p>
 * Позволяет выбрать .class файл через диалог выбора файла и выводит
 * результаты анализа: количество IF, циклов, инструкций циклов и объявлений переменных.
 */
public class FxMainApp extends Application {

    /**
     * Точка входа JavaFX приложения.
     * Создает основное окно с кнопкой выбора файла и текстовым полем для вывода результатов анализа.
     *
     * @param stage основная сцена приложения
     */
    @Override
    public void start(Stage stage) {

        // Заголовок окна
        Label title = new Label("Java Class Analyzer");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Текстовое поле для вывода результатов анализа
        TextArea output = new TextArea();
        output.setEditable(false);
        output.setPrefHeight(300);

        // Кнопка выбора файла
        Button chooseFileBtn = new Button("Выбрать .class файл");

        chooseFileBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Class files", "*.class")
            );

            File file = chooser.showOpenDialog(stage);
            if (file == null) return;

            try {
                // Анализ выбранного class-файла
                ClassAnalyzer analyzer = new ClassAnalyzer();
                analyzer.analyze(file.getAbsolutePath());

                String result = analyzer.printResultToLogger();
                output.setText(result);

                LoggerConfig.logger.info("Файл проанализирован: " + file.getName());

            } catch (Exception ex) {
                output.setText("Ошибка анализа:\n" + ex.getMessage());
                LoggerConfig.logger.error("Ошибка анализа", ex);
            }
        });

        // Основной контейнер
        VBox root = new VBox(10, title, chooseFileBtn, output);
        root.setPadding(new Insets(10));

        stage.setTitle("Java Class Analyzer");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    /**
     * Точка входа в приложение.
     * Вызывает метод {@link Application#launch(String...)} для запуска JavaFX.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        launch(args);
    }
}
