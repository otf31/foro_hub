package com.otf31.foro_hub.domain.topics

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface TopicRepository:
  JpaRepository<Topic, Long>,
  JpaSpecificationExecutor<Topic>
{
  fun existsByTitleAndMessage(title: String, message: String): Boolean

  fun findTop10ByOrderByCreatedAtDesc(): List<Topic>
}
