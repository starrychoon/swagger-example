package org.example.swagger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

/**
 * @author starrychoon
 */
@SpringBootApplication
@ConfigurationPropertiesScan
class SwaggerApplication

fun main(args: Array<String>) {
    runApplication<SwaggerApplication>(*args)
}
