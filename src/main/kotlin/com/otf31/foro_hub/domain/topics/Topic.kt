package com.otf31.foro_hub.domain.topics

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "Topic")
@Table(name = "Topics", schema = "foro_hub")
data class Topic(
  var title: String = "",
  var message: String = "",
  var status: String = "NOT_ANSWERED",
  var author: String = "",
  var course: String = "",
  var createdAt: LocalDateTime = LocalDateTime.now()
) {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  companion object {
    fun from(
      dataCreateUpdateTopic: DataCreateUpdateTopic
    ): Topic =
      Topic(
        title = dataCreateUpdateTopic.title,
        message = dataCreateUpdateTopic.message,
        author = dataCreateUpdateTopic.author,
        course =  dataCreateUpdateTopic.course,
      )
  }

  fun update(
    dataCreateUpdateTopic: DataCreateUpdateTopic
  ) {
    title = dataCreateUpdateTopic.title
    message = dataCreateUpdateTopic.message
    author = dataCreateUpdateTopic.author
    course = dataCreateUpdateTopic.course
  }
}
