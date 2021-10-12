import sbt._

object Dependencies {

  //versions
  val enumeratumVersion = "1.5.13"
  val kafkaVersion = "2.2.0"
  val typesafeLogVersion = "3.9.2"
  val scalaTestVersion = "3.0.5"

  lazy val kafka = Seq(
    "org.apache.kafka" %% "kafka-streams-scala",
    "org.apache.kafka" % "kafka-clients"
  ).map(_ % kafkaVersion)

  lazy val enumeratum = "com.beachape" %% "enumeratum" % enumeratumVersion
  lazy val typesafeLog = "com.typesafe.scala-logging" %% "scala-logging" % typesafeLogVersion

  // test dependencies
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
  lazy val kafkaTest = "org.apache.kafka" % "kafka-streams-test-utils" % kafkaVersion % Test

  lazy val dependencies = Seq(enumeratum, typesafeLog, kafkaTest) ++ kafka

}