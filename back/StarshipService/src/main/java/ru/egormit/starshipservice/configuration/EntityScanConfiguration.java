package ru.egormit.starshipservice.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

/**
 * Подключение сущностей из библиотеки.
 *
 * @author Egor Mitrofanov.
 */
@Configuration
@EntityScan("ru.egormit.library")
public class EntityScanConfiguration {
}
