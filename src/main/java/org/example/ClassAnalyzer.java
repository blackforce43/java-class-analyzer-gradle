package org.example;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import java.io.FileInputStream;

/**
 * Класс для анализа .class файла.
 * Использует {@link MethodAnalyzer} для анализа каждого метода.
 * Считает количество IF, циклов, инструкций циклов и объявлений переменных.
 */
public class ClassAnalyzer {

    /** Количество условных операторов IF */
    private int ifCount = 0;

    /** Общее количество циклов (for, while, do-while, backward jumps, switch) */
    private int loopCount = 0;

    /** Количество инструкций, входящих в циклы */
    private int loopInstructions = 0;

    /** Количество объявлений переменных */
    private int variableDeclarations = 0;
    
    /**
     * Анализирует переданный class-файл.
     *
     * @param classPath путь к .class файлу
     * @throws Exception если файл не найден или ошибка чтения
     */
    public void analyze(String classPath) throws Exception {
        try (FileInputStream fis = new FileInputStream(classPath)) {
            ClassReader reader = new ClassReader(fis);

            ClassNode classNode = new ClassNode();
            reader.accept(classNode, ClassReader.SKIP_FRAMES);

            MethodAnalyzer methodAnalyzer = new MethodAnalyzer();

            // Используем Stream API для перебора методов класса
            classNode.methods.stream().forEach(method -> {
                methodAnalyzer.analyze(method);

                ifCount += methodAnalyzer.ifCount;
                loopCount += methodAnalyzer.loopCount;
                loopInstructions += methodAnalyzer.loopInstructions;
                variableDeclarations += methodAnalyzer.variableCount;

                methodAnalyzer.reset();
            });
        }
    }

    /**
     * Выводит результаты анализа в консоль.
     */
    public void printResult() {
        System.out.println("\n===== РЕЗУЛЬТАТ =====");
        System.out.println("Количество IF: " + ifCount);
        System.out.println("Всего циклов: " + loopCount);
        System.out.println("Инструкции циклов: " + loopInstructions);
        System.out.println("Объявления переменных: " + variableDeclarations);
    }

    /**
     * Формирует строку для логирования с результатами анализа.
     *
     * @return текст анализа
     */
    public String printResultToLogger() {
        return """
                ===== РЕЗУЛЬТАТ =====
                Количество IF: %d
                Всего циклов: %d
                Инструкции циклов: %d
                Объявления переменных: %d
                """.formatted(
                ifCount,
                loopCount,
                loopInstructions,
                variableDeclarations
        );
    }
}
