package io.github.ufukhalis.debezium.listener

import io.debezium.config.Configuration
import io.debezium.data.Envelope.FieldName.*
import io.debezium.embedded.EmbeddedEngine
import io.github.ufukhalis.debezium.objectMapper
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Component
final class CDCListener(
    colleagueConnector: Configuration,
    private val template: KafkaTemplate<String, String>
) {

    private val executor = Executors.newSingleThreadExecutor()

    private val embeddedEngine: EmbeddedEngine? = EmbeddedEngine.create()
        .using(colleagueConnector)
        .notifying(this::handleEvent)
        .build()


    @PostConstruct
    fun init() {
        embeddedEngine?.let {
            this.executor.execute(it)
        }
    }

    @PreDestroy
    fun destroy() {
        this.embeddedEngine?.stop()
    }

    private fun handleEvent(sourceRecord: SourceRecord) {
        val sourceRecordValue = sourceRecord.value() as Struct?

        sourceRecordValue?.let { value ->
            val operation = value.get(OPERATION) as String

            if (operation != "r") {

                var record = AFTER
                if (operation == "d") {
                    record = BEFORE
                }

                val struct = sourceRecordValue.get(record) as Struct

                val dataMap = struct.schema().fields()
                    .map { it.name() }
                    .filter { struct.get(it) != null }
                    .associateWith { struct.get(it) }


                val cdcMessage = objectMapper.writeValueAsString(CDCMessage(operation, dataMap))
                template.send("cdc-topic", UUID.randomUUID().toString(), cdcMessage)


                println("CDC captured, operation $operation data $dataMap and sent to Kafka ")
            }
        }
    }
}

data class CDCMessage(val operation: String, val data:  Map<String, Any>)
