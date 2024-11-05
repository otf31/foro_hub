package com.otf31.foro_hub.domain.topics.validations

import com.otf31.foro_hub.domain.ValidationException
import com.otf31.foro_hub.domain.topics.DataCreateUpdateTopic
import com.otf31.foro_hub.domain.topics.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ValidatorTopicSameTitleAndMessage @Autowired constructor(
  private val topicRepository: TopicRepository
): ValidatorOfTopics {
  override fun validate(dataCreateUpdateTopic: DataCreateUpdateTopic) {
    val topicExists = topicRepository.existsByTitleAndMessage(dataCreateUpdateTopic.title, dataCreateUpdateTopic.message)

    if (topicExists) {
      throw ValidationException("Topic with same title and message already exists")
    }
  }
}
