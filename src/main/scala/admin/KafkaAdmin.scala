package admin

import org.apache.kafka.clients.admin.{AdminClient, NewTopic}
import scala.collection.JavaConverters._

object KafkaAdmin extends App {

  val props = createBaseProps()
  val adminClient = AdminClient.create(props)

  createTopic()

  adminClient.listTopics().names().get().asScala.foreach(println)

  def createTopic(): Unit = {
    val topics = Seq(
      new NewTopic(pageViewTopic, 2, 1)
    ).asJava

    val values = adminClient.createTopics(topics).values()
    values.values().asScala.foreach(_.get())
  }

}
