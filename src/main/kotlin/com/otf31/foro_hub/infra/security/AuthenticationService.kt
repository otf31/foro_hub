package com.otf31.foro_hub.infra.security

import com.otf31.foro_hub.domain.users.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AuthenticationService @Autowired constructor(
  private val userRepository: UserRepository
): UserDetailsService {

  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(
    username: String
  ): UserDetails {
    val user = userRepository.findByEmail(username)
      ?: throw UsernameNotFoundException("User not found")

    return user
  }
}
