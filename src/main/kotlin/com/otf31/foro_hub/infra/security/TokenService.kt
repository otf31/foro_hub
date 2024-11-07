package com.otf31.foro_hub.infra.security

import java.lang.RuntimeException
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTCreationException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.otf31.foro_hub.domain.users.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TokenService {
  @Value("\${api.security.secret}")
  private val apiSecret: String = ""

 fun generateToken(
   user: User
 ): String {
   try {
     val algorithm = Algorithm.HMAC256(apiSecret)

     return JWT.create()
       .withIssuer("foro_hub otf31")
       .withSubject(user.email)
       .withClaim("id", user.id)
       .withExpiresAt(generateExpirationDate())
       .sign(algorithm)
   } catch (_: JWTCreationException) {
     throw RuntimeException("There was an error creating the token")
   }
 }

  fun getSubject(
    token: String
  ): String? {
    val algorithm = Algorithm.HMAC256(apiSecret)
    val verifier = JWT.require(algorithm)
      .withIssuer("foro_hub otf31")
      .build()
      .verify(token)

    return verifier.subject
  }

  private fun generateExpirationDate(
  ): Instant =
    LocalDateTime.now().plusHours(24).toInstant(ZoneOffset.of("-05:00"))
}
