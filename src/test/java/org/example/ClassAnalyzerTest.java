package org.example;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class ClassAnalyzerTest {

    @Test
    void testAnalyzerCounts() throws Exception {

        Path classFile = Path.of(
                "bin/test/org/example/TestTarget.class"
        );

        ClassAnalyzer analyzer = new ClassAnalyzer();
        analyzer.analyze(classFile.toString());

        String result = analyzer.printResultToLogger();

        assertTrue(result.contains("IF: 1"), "Должен быть 1 if");
        assertTrue(result.contains("Всего циклов: 2"), "Всего должно быть 2 цикла");
        assertTrue(result.contains("Инструкции циклов: 21"), "Должна быть 21 инструкция");
        assertTrue(result.contains("Объявления переменных: 3"), "Подсчёт переменных должен быть 3");
    }
}
