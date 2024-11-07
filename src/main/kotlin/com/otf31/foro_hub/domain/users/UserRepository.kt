package com.otf31.foro_hub.domain.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.security.core.userdetails.UserDetails

interface UserRepository : JpaRepository<User, Long> {

  fun findByEmail(
    email: String
  ): UserDetails?
}
