package com.otf31.foro_hub.controller

import com.otf31.foro_hub.domain.topics.DataCreateUpdateTopic
import com.otf31.foro_hub.domain.topics.DataListTopic
import com.otf31.foro_hub.domain.topics.DataResponseTopic
import com.otf31.foro_hub.domain.topics.Topic
import com.otf31.foro_hub.domain.topics.TopicRepository
import com.otf31.foro_hub.domain.topics.validations.ValidatorOfTopics
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springdoc.core.annotations.ParameterObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.web.PagedModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.time.LocalDateTime

@RestController
@RequestMapping("topics")
@SecurityRequirement(name = "bearer-key")
class TopicController @Autowired constructor(
  private val topicRepository: TopicRepository,
  private val validators: List<ValidatorOfTopics>
) {

  @GetMapping
  fun getTopics(
    @ParameterObject
    pagination: Pageable,
    @RequestParam(required = false)
    course: String?,
    @RequestParam(required = false)
    year: Int?
  ): ResponseEntity<PagedModel<DataListTopic>> {
    val spec = Specification
      .where(course?.let { TopicSpecifications.hasCourse(it) })
      .and(year?.let { TopicSpecifications.hasCreatedAtYear(it) })
    val topics = topicRepository.findAll(
      spec,
      pagination
    )
    val dataResponseTopics = PagedModel(topics.map { DataListTopic.from(it) })

    return ResponseEntity.ok(dataResponseTopics)
  }

  @GetMapping("{id}")
  @Transactional
  fun getTopicById(
    @PathVariable
    id: Long
  ): ResponseEntity<DataResponseTopic> {
    val topic = topicRepository.getReferenceById(id)
    val dataResponseTopic = DataResponseTopic.from(topic)

    return ResponseEntity.ok(dataResponseTopic)
  }

  @PostMapping
  fun createTopic(
    @Valid
    @RequestBody
    dataCreateUpdateTopic: DataCreateUpdateTopic,
    uriComponentsBuilder: UriComponentsBuilder
  ): ResponseEntity<DataResponseTopic> {
    // Validate according to the rules
    validators.forEach { it.validate(dataCreateUpdateTopic) }

    val topic = topicRepository.save(Topic.from(dataCreateUpdateTopic))
    val dataResponseTopic = DataResponseTopic.from(topic)
    val location = uriComponentsBuilder.path("/topics/{id}").buildAndExpand(topic.id).toUri()

    return ResponseEntity.created(location).body(dataResponseTopic)
  }

  @PutMapping("{id}")
  @Transactional
  fun updateTopic(
    @PathVariable
    id: Long,
    @Valid
    @RequestBody
    dataCreateUpdateTopic: DataCreateUpdateTopic
  ): ResponseEntity<DataResponseTopic> {
    // Validate according to the rules
    validators.forEach { it.validate(dataCreateUpdateTopic) }

    val topic = topicRepository.getReferenceById(id)
    topic.update(dataCreateUpdateTopic)
    val updatedTopic = topicRepository.save(topic)

    return ResponseEntity.ok(DataResponseTopic.from(updatedTopic))
  }

  @DeleteMapping("{id}")
  @Transactional
  fun deleteTopic(
    @PathVariable
    id: Long
  ): ResponseEntity<String> {
    // Check if the topic exists
    val topic = topicRepository.getReferenceById(id)

    topicRepository.delete(topic)

    return ResponseEntity.noContent().build()
  }

  @GetMapping("ten-most-recent")
  fun getTenMostRecentTopics(): ResponseEntity<List<DataListTopic>> {
    val topics = topicRepository.findTop10ByOrderByCreatedAtDesc()
    val dataResponseTopics = topics.map { DataListTopic.from(it) }

    return ResponseEntity.ok(dataResponseTopics)
  }
}

object TopicSpecifications {
  fun hasCourse(course: String): Specification<Topic> {
    return Specification { root, _, cb ->
      cb.equal(root.get<String>("course"), course)
    }
  }

  fun hasCreatedAtYear(year: Int): Specification<Topic> {
    val startDate = LocalDateTime.of(year, 1, 1, 0, 0)

    return Specification { root, _, cb ->
      cb.between(root.get<LocalDateTime>("createdAt"), startDate, startDate.plusYears(1))
    }
  }
}
