package com.otf31.foro_hub.domain.topics

import jakarta.validation.constraints.NotBlank

@JvmRecord
data class DataCreateUpdateTopic(
  @field:NotBlank
  val title: String,
  @field:NotBlank
  val message: String,
  @field:NotBlank
  val author: String,
  @field:NotBlank
  val course: String
)
