package producers

import admin.createBaseProps
import encoders.PageViewEvent
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import admin._
import java.util.UUID

object PageViewEventProducer extends App {
  new PageViewEventProducer
}

class PageViewEventProducer {

  val props = createBaseProps()
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "encoders.SimpleCaseClassSerializer")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "encoders.SimpleCaseClassSerializer")
  props.put(ProducerConfig.ACKS_CONFIG, "all")
  val producer: KafkaProducer[String, PageViewEvent] = new KafkaProducer(props)
  val topic = pageViewTopic

  //TODO: Stream from an external source
  try {
    Seq(
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p001", 1617159755012L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p001", 1617159755012L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p001", 1617159755012L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p002", 1617159755012L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u002", "p002", 1619136000000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u002", "p002", 1619136000000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p001", 1618704000000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u001", "p001", 1618358400000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u004", "p004", 1618185600000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u004", "p004", 1618185600000L)),
      new ProducerRecord(topic, UUID.randomUUID().toString, PageViewEvent("u004", "p004", 1618790400000L))
    ).foreach(e => {
      producer.send(e)
      println(s"Page View event sent for page: ${e.value().pageId} user: ${e.value().userId} with timestamp: ${e.value().timestamp}")
    })
  }
  catch {
    case e: Exception => producer.abortTransaction()
      e.printStackTrace()
  }

  producer.close()
}
