package encoders

case class PageViewEvent(userId: String,
                         pageId: String,
                         timestamp: Long
                        )

case class UserPageViewKey(userId: String,
                           pageId: String
                          )