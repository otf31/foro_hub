package com.otf31.foro_hub.domain.users

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

@JvmRecord
data class DataUserAuthentication(
  @field:NotBlank
  @field:Email
  val email: String,
  @field:NotBlank
  val secret: String
)
