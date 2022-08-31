package io.github.ufukhalis.debezium.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DebeziumConnectorConfig(
    @Value("\${datasource.dbName}")
    private val dbName: String,
    @Value("\${datasource.host}")
    private val host: String,
    @Value("\${datasource.port}")
    private val port: String,
    @Value("\${datasource.username}")
    private val username: String,
    @Value("\${datasource.password}")
    private val password: String,
) {


    @Bean
    fun colleagueConnector(): io.debezium.config.Configuration {
        return io.debezium.config.Configuration.create()
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with(
                "offset.storage.file.filename",
                "/colleague-offset.dat"
            )
            .with("offset.flush.interval.ms", 60000)
            .with("name", "colleague-postgres-connector")
            .with("database.server.name", "$host-$dbName")
            .with("database.hostname", host)
            .with("database.port", port)
            .with("database.user", username)
            .with("database.password", password)
            .with("database.dbname", dbName)
            .build()
    }
}
