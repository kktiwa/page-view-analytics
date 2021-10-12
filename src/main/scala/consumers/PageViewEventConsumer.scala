package consumers

import admin._
import encoders.PageViewEvent
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import scala.collection.JavaConverters._
import java.time.Duration

object PageViewEventConsumer extends App {
  new PageViewEventConsumer
}

class PageViewEventConsumer {

  val props = createBaseProps()
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "encoders.SimpleCaseClassDeserializer")
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "encoders.SimpleCaseClassDeserializer")
  props.put(ConsumerConfig.GROUP_ID_CONFIG, "page-view-event-consumer")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
  val topics = Seq(pageViewTopic)

  val consumer: KafkaConsumer[PageViewEvent, PageViewEvent] = new KafkaConsumer[PageViewEvent, PageViewEvent](props)
  consumer.subscribe(topics.asJava)
  println(s"Subscribing to topics: ${topics.mkString(", ")}")

  while (true) {
    val records: ConsumerRecords[PageViewEvent, PageViewEvent] = consumer.poll(Duration.ofMillis(100))
    records.iterator().asScala.foreach(record => {
      val key = record.key()
      val response = record.value()
      println(s"Received response with key as $key, userId ${response.userId} and pageId ${response.pageId}")
    })
  }

}
