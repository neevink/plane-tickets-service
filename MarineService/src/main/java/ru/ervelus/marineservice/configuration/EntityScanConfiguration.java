package ru.ervelus.marineservice.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("ru.egormit.library")
public class EntityScanConfiguration {
}