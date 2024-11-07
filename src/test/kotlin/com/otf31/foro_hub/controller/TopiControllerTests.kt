package com.otf31.foro_hub.controller

import com.otf31.foro_hub.domain.topics.DataCreateUpdateTopic
import com.otf31.foro_hub.domain.topics.DataResponseTopic
import com.otf31.foro_hub.domain.topics.Topic
import com.otf31.foro_hub.domain.topics.TopicRepository
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class TopiControllerTests @Autowired constructor(
  private val mockMvc: MockMvc,
  private val dataRegisterTopicJson: JacksonTester<DataCreateUpdateTopic>,
  private val dataResponseTopicJson: JacksonTester<DataResponseTopic>,
  @MockBean
  private val topicRepository: TopicRepository
) {

  @Test
  @DisplayName("Should return HTTP status 400 when title is empty")
  @WithMockUser
  fun `Should return HTTP status 400 when title is empty`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "",
      message = "Message",
      author = "Author",
      course = "Course"
    )

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
  }

  @Test
  @DisplayName("Should return HTTP status 400 when message is empty")
  @WithMockUser
  fun `Should return HTTP status 400 when message is empty`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "",
      author = "Author",
      course = "Course"
    )

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
  }

  @Test
  @DisplayName("Should return HTTP status 400 when author is empty")
  @WithMockUser
  fun `Should return HTTP status 400 when author is empty`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "Message",
      author = "",
      course = "Course"
    )

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
  }

  @Test
  @DisplayName("Should return HTTP status 400 when course is empty")
  @WithMockUser
  fun `Should return HTTP status 400 when course is empty`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "Message",
      author = "Author",
      course = ""
    )

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
  }

  @Test
  @DisplayName("Should return HTTP status 400 when a topic already exists with the same title and message")
  @WithMockUser
  fun `Should return HTTP status 400 when a topic already exists with the same title and message`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "Message",
      author = "Author",
      course = "Course"
    )
    Mockito.`when`(topicRepository.existsByTitleAndMessage(dataCreateTopic.title, dataCreateTopic.message)).thenReturn(true)

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.BAD_REQUEST.value())
  }

  @Test
  @DisplayName("Should return HTTP status 201 when a topic is created successfully")
  @WithMockUser
  fun `Should return HTTP status 201 when a topic is created successfully`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "Message",
      author = "Author",
      course = "Course"
    )
    Mockito.`when`(topicRepository.existsByTitleAndMessage(dataCreateTopic.title, dataCreateTopic.message)).thenReturn(false)
    Mockito.`when`(topicRepository.save(any())).thenReturn(Topic.from(dataCreateTopic))

    // When
    val response = mockMvc.perform(post("/topics")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.CREATED.value())
  }

  @Test
  @DisplayName("Should return HTTP status 200 when a topic is updated successfully")
  @WithMockUser
  fun `Should return HTTP status 200 when a topic is updated successfully`() {
    // Given
    val dataCreateTopic = DataCreateUpdateTopic(
      title = "Title",
      message = "Message",
      author = "Author",
      course = "Course"
    )
    val dataUpdateTopic = DataCreateUpdateTopic(
      title = "Title Updated",
      message = "Message Updated",
      author = "Author Updated",
      course = "Course Updated"
    )
    val createTopic = Topic.from(dataCreateTopic)
    val updateTopic = Topic.from(dataUpdateTopic)
    updateTopic.createdAt = createTopic.createdAt
    Mockito.`when`(topicRepository.getReferenceById(1)).thenReturn(createTopic)
    Mockito.`when`(topicRepository.save(any())).thenReturn(updateTopic)

    // When
    val response = mockMvc.perform(put("/topics/1")
      .contentType("application/json")
      .content(dataRegisterTopicJson.write(dataCreateTopic).json))
      .andReturn().response

    // Then
    assertThat(response.contentAsString).isEqualTo(dataResponseTopicJson.write(DataResponseTopic.from(updateTopic)).json)
    assertThat(response.status).isEqualTo(HttpStatus.OK.value())
  }

  @Test
  @DisplayName("Should return HTTP status 404 when a topic is not found")
  @WithMockUser
  fun `Should return HTTP status 404 when a deleted topic is requested`() {
    // Given
    Mockito.`when`(topicRepository.getReferenceById(1)).thenThrow(EntityNotFoundException::class.java)

    // When
    val response = mockMvc.perform(get("/topics/1"))
      .andReturn().response

    // Then
    assertThat(response.status).isEqualTo(HttpStatus.NOT_FOUND.value())
  }
}
