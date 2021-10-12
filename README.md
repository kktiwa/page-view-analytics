## Objective

Produce aggregated count metrics in real-time for incoming page view events.

### Summary
A JSON encoded page view event is generated each time a user views a webpage. 
A page view event records the `userId`, `pageId`, and `timestamp` of the page view, e.g.

```
{
"userId": "8597b840-7ab1-4016-b4e8-a9d0f1116b1d",
"pageId": "c05e6a4f-f59a-4e63-8400-4c7576175f1f",
"timestamp": 1617159755012
}
```

The `userId` and `pageId` are both v4 UUIDs, and the `timestamp` is a millisecond precision epoch timestamp. 
We need to keep track of the following aggregations:
* For each page `p`, the 7-day page view count, which counts the number of times that `p` has been viewed in the last 7
   days.
* For each user `u` and page `p`, the 7-day user-page view count, which counts the number of times that `p` has been viewed
   by `u` in the last 7 days.


## Tech Stack
* Scala version 2.11.12
* SBT version 1.2.8
* Kafka version 2.2.0

## Running Locally
* Import the project as a Scala SBT project
* Install kafka binaries from `https://kafka.apache.org/downloads` specifically version `2.2.0`
* Run `KafkaAdmin.scala` program to create the topic `page-view-topic`
* Run `PageViewEventProducer.scala` program to push some events into the topic `page-view-topic`
* Run `UserPageViewCountStream.scala` program which is the stream application for producing user-page-view aggregated metrics
* Run `PageViewCountStream.scala` program which is the stream application for producing page-view aggregated metrics

## TODOs & Improvements
* Add [tests](https://kafka.apache.org/20/documentation/streams/developer-guide/testing.html) for kafka streams
* Add a config framework and move things such as durations, topic names, store names etc. into it
* Dockerize the project and use confluent kafka docker images (for latest version) instead of installing kafka locally
* Use schema registry for registering page view event schema in avro format

## Sample Outputs from Console (Std out)
`PageViewCountStream`
```
[KTABLE-TOSTREAM-0000000007]: [p004@1617840000000/1618444800000], 1
[KTABLE-TOSTREAM-0000000007]: [p004@1617840000000/1618444800000], 2
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 1
[KTABLE-TOSTREAM-0000000007]: [p001@1618444800000/1619049600000], 1
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 2
[KTABLE-TOSTREAM-0000000007]: [p001@1618444800000/1619049600000], 2
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 3
[KTABLE-TOSTREAM-0000000007]: [p001@1618444800000/1619049600000], 3
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 4
[KTABLE-TOSTREAM-0000000007]: [p001@1618444800000/1619049600000], 4
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 5
[KTABLE-TOSTREAM-0000000007]: [p001@1618444800000/1619049600000], 5
[KTABLE-TOSTREAM-0000000007]: [p004@1618444800000/1619049600000], 6
[KTABLE-TOSTREAM-0000000007]: [p002@1619049600000/1619654400000], 1
[KTABLE-TOSTREAM-0000000007]: [p002@1619049600000/1619654400000], 2
```
`UserPageViewCountStream`
```
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p002)@1616630400000/1617235200000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1617840000000/1618444800000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1617840000000/1618444800000], 2
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1617840000000/1618444800000], 3
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1617840000000/1618444800000], 4
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1619049600000/1619654400000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u002,p002)@1619049600000/1619654400000], 2
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1616630400000/1617235200000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1616630400000/1617235200000], 2
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1616630400000/1617235200000], 3
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u004,p004)@1618444800000/1619049600000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1618444800000/1619049600000], 1
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u004,p004)@1618444800000/1619049600000], 2
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1618444800000/1619049600000], 2
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1618444800000/1619049600000], 3
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u004,p004)@1618444800000/1619049600000], 3
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u004,p004)@1618444800000/1619049600000], 4
[KTABLE-TOSTREAM-0000000007]: [UserPageViewKey(u001,p001)@1618444800000/1619049600000], 4
```
