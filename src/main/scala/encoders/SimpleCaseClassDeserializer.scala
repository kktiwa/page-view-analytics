package encoders

import java.io.{ByteArrayInputStream, ObjectInputStream}
import java.util
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer


class SimpleCaseClassDeserializer[T] extends Deserializer[T] {

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}

  override def deserialize(topic: String, data: Array[Byte]): T =
    try {
      val byteIn = new ByteArrayInputStream(data)
      val objIn = new ObjectInputStream(byteIn)
      val obj = objIn.readObject().asInstanceOf[T]
      byteIn.close()
      objIn.close()
      obj
    }
    catch {
      case e: Exception =>
        throw new SerializationException(e)
    }

  override def close(): Unit = {}

}
