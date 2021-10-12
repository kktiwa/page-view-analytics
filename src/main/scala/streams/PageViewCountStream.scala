package streams

import java.time.Duration
import admin._
import encoders._
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder}
import org.apache.kafka.streams.kstream.{Materialized, Printed, TimeWindows}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.kstream.{Consumed, _}

/**
 * For each page p, the 7-day page view count, which counts the number of times
 * that p has been viewed in the last 7 days.
 */
object PageViewCountStream extends App {

  val props = createBaseProps()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "view-count-stream")
  props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  implicit val pageViewEventSerde: Serde[PageViewEvent] = Serdes.serdeFrom(new SimpleCaseClassSerializer[PageViewEvent], new SimpleCaseClassDeserializer[PageViewEvent])

  val builder = new StreamsBuilder()
  val pageViewCountStream: KStream[String, PageViewEvent] = builder
    .stream(pageViewTopic,
      Consumed.`with`(Serdes.String(), pageViewEventSerde)
        .withTimestampExtractor(new PageViewEventTimeExtractor)
    )

  //TODO: Move durations into config
  val windowDuration = Duration.ofDays(7)
  val timeWindows = TimeWindows
    .of(windowDuration)
    .advanceBy(windowDuration)

  val groupedEventSerde = Grouped.`with`(Serdes.String(), pageViewEventSerde)

  pageViewCountStream
    .selectKey((_, v) => v.pageId)
    .groupByKey(groupedEventSerde)
    .windowedBy(timeWindows)
    .count()(Materialized.as("page-view-count-store"))
    .toStream
    .print(Printed.toSysOut())
  //Ideally we want to write the aggregated counts to another topic
  //.to("page-view-count-topic")(Produced.`with`(ScalaSerdes.timeWindowedSerde, ScalaSerdes.Long))

  val streams = new KafkaStreams(builder.build(), props)

  streams.cleanUp()
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
