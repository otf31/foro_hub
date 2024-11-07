package com.otf31.foro_hub.domain.users

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity(name = "User")
@Table(name = "Users", schema = "foro_hub")
data class User(
  val name: String = "",
  val email: String = "",
  val secret: String = "",
): UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null

  override fun getAuthorities(
  ): Collection<GrantedAuthority> =
    setOf(SimpleGrantedAuthority("ROLE_USER"))

  override fun getPassword(
  ): String =
    secret

  override fun getUsername(
  ): String =
    email
}
