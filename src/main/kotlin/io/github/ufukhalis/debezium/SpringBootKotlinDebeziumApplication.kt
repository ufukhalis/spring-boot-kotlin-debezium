package io.github.ufukhalis.debezium

import com.fasterxml.jackson.databind.DeserializationFeature
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootKotlinDebeziumApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKotlinDebeziumApplication>(*args)
}

val objectMapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)!!
