package streams

import encoders.PageViewEvent
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.streams.processor.TimestampExtractor

class PageViewEventTimeExtractor extends TimestampExtractor {

  //TODO:Make extraction time zone aware
  override def extract(record: ConsumerRecord[AnyRef, AnyRef], previousTimestamp: Long): Long = {
    record.value() match {
      case x: PageViewEvent => x.timestamp
      case _ => throw new RuntimeException(s"PageViewEventTimeExtractor invoked for $record")
    }
  }
}
