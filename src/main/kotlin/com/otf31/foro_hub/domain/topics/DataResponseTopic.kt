package com.otf31.foro_hub.domain.topics

import java.time.LocalDateTime

@JvmRecord
data class DataResponseTopic(
  val title: String,
  val message: String,
  val status: String,
  val author: String,
  val course: String,
  val createdAt: LocalDateTime
) {
  companion object {
    fun from(topic: Topic): DataResponseTopic =
      DataResponseTopic(
        title = topic.title,
        message = topic.message,
        status = topic.status,
        author = topic.author,
        course = topic.course,
        createdAt = topic.createdAt
      )
  }
}
