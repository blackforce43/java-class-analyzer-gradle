package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Конфигурация логгера Log4j2 для приложения.
 */
public class LoggerConfig {
    /** Основной логгер приложения */
    public static final Logger logger = LogManager.getLogger("Analyzer");
}
