import java.util.Properties
import org.apache.kafka.clients.producer.ProducerConfig

package object admin {

  val pageViewTopic = "page-view-topic"

  def createBaseProps(): Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props
  }

}
