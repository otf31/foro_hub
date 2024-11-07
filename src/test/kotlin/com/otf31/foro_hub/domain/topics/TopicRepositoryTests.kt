package com.otf31.foro_hub.domain.topics

import jakarta.persistence.EntityManager
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TopicRepositoryTests @Autowired constructor(
  private val topicRepository: TopicRepository,
  private val entityManager: EntityManager
) {

  @Test
  @DisplayName("Should topics created exists")
  fun `Should topics created exists`() {
    // Given
    val topic1 = registerTopic("Title", "Message", "Author", "Course")
    val topic2 = registerTopic("Title 2", "Message 2", "Author 2", "Course 2")

    // When
    val topic1Exists = topicRepository.findById(topic1.id!!)
    val topic2Exists = topicRepository.findById(topic2.id!!)

    println("topic1: $topic1Exists")
    println("topic2: $topic2Exists")

    // Then
    assertThat(topic1Exists.isPresent).isTrue
    assertThat(topic2Exists.isPresent).isTrue
  }

  @Test
  @DisplayName("Should topic not exists when deleted")
  fun `Should topic not exists when deleted`() {
    // Given
    val topic = registerTopic("Title", "Message", "Author", "Course")

    // When
    topicRepository.deleteById(topic.id!!)

    // Then
    val topicExists = topicRepository.findById(topic.id!!)
    assertThat(topicExists.isEmpty).isTrue
  }

  @Test
  @DisplayName("Should return true when a topic already exists with the same title and message")
  fun `Should return true when a topic already exists with the same title and message`() {
    // Given
    registerTopic("Title", "Message", "Author", "Course")
    registerTopic("Title 2", "Message 2", "Author 2", "Course 2")
    registerTopic("Title 3", "Message 3", "Author 3", "Course 3")

    // When
    val exists = topicRepository.existsByTitleAndMessage("Title 2", "Message 2")

    // Then
    assertThat(exists).isTrue
  }

  @Test
  @DisplayName("Should return false when a topic with the same title and message does not exist yet")
  fun `Should return false when a topic with the same title and message does not exist yet`() {
    // Given
    registerTopic("Title", "Message", "Author", "Course")
    registerTopic("Title 2", "Message 2", "Author 2", "Course 2")
    registerTopic("Title 3", "Message 3", "Author 3", "Course 3")

    // When
    val exists = topicRepository.existsByTitleAndMessage("Title 2", "Message 3")

    // Then
    assertThat(exists).isFalse
  }

  private fun registerTopic(
    title: String,
    message: String,
    author: String,
    course: String,
  ): Topic {
    val topic = Topic.from(dataTopic(title, message, author, course))
    entityManager.persist(topic)

    return topic
  }

  private fun dataTopic(
    title: String,
    message: String,
    author: String,
    course: String,
  ): DataCreateUpdateTopic =
    DataCreateUpdateTopic(
      title = title,
      message = message,
      course = course,
      author = author,
    )
}
