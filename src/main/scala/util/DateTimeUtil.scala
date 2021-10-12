package util

import java.time.{LocalDateTime, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.UUID

object DateTimeUtil extends App {
  println(dateTimeStringToEpoch("2021-04-23 00:00:00"))
  //println(epochToDate(1618099200000L))
  //println(epochToDate(1619049600000L))

  def epochToDate(epochMillis: Long): String = {
    val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd")
    df.format(epochMillis)
  }

  def dateTimeStringToEpoch(s: String, pattern: String = "yyyy-MM-dd HH:mm:ss"): Long = {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    LocalDateTime.parse(s, formatter)
      .atZone(ZoneOffset.UTC)
      .toInstant
      .toEpochMilli
  }
}
