package streams

import java.time.Duration
import admin._
import encoders._
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{Serde, Serdes}
import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}
import org.apache.kafka.streams.kstream.{Materialized, Printed, TimeWindows}
import org.apache.kafka.streams.scala.ImplicitConversions._
import org.apache.kafka.streams.scala.kstream.{Consumed, _}

/**
 * For each user u and page p, the 7-day user-page view count,
 * which counts the number of times that p has been viewed by u in the last 7 days.
 */
object UserPageViewCountStream extends App {

  val props = createBaseProps()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-page-view-count-stream")
  props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0")
  props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")

  implicit val pageViewEventSerde: Serde[PageViewEvent] = Serdes.serdeFrom(new SimpleCaseClassSerializer[PageViewEvent], new SimpleCaseClassDeserializer[PageViewEvent])
  implicit val userPageViewKeySerde: Serde[UserPageViewKey] = Serdes.serdeFrom(new SimpleCaseClassSerializer[UserPageViewKey], new SimpleCaseClassDeserializer[UserPageViewKey])

  val builder = new StreamsBuilder()
  val userPageViewCountStream: KStream[String, PageViewEvent] = builder
    .stream(pageViewTopic,
      Consumed.`with`(Serdes.String(), pageViewEventSerde)
        .withTimestampExtractor(new PageViewEventTimeExtractor)
    )

  //TODO: Move durations into config
  val windowDuration = Duration.ofDays(7)
  val timeWindows = TimeWindows
    .of(windowDuration)
    .advanceBy(windowDuration)

  val groupedEventSerde = Grouped.`with`(userPageViewKeySerde, pageViewEventSerde)

  userPageViewCountStream
    .selectKey((_, v) => UserPageViewKey(v.userId, v.pageId))
    .groupByKey(groupedEventSerde)
    .windowedBy(timeWindows)
    .count()(Materialized.as("user-page-view-count-store"))
    .toStream
    .print(Printed.toSysOut())
  //Ideally we want to write the aggregated counts to another topic
  //.to("user-page-view-count-topic")(Produced.`with`(ScalaSerdes.timeWindowedSerde, ScalaSerdes.Long))


  val streams = new KafkaStreams(builder.build(), props)

  streams.cleanUp()
  streams.start()

  sys.ShutdownHookThread {
    streams.close(Duration.ofSeconds(10))
  }

}
