package com.otf31.foro_hub.controller

import com.otf31.foro_hub.domain.users.DataUserAuthentication
import com.otf31.foro_hub.domain.users.User
import com.otf31.foro_hub.infra.security.DataJwtToken
import com.otf31.foro_hub.infra.security.TokenService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AutenticacionController @Autowired constructor(
  private val tokenService: TokenService,
  private val authenticationManager: AuthenticationManager
) {

  @PostMapping("login")
  fun autenticarUsuario(
    @Valid
    @RequestBody
    datosAutenticacionUsuario: DataUserAuthentication
  ): ResponseEntity<Any> =
    try {
      val authToken = UsernamePasswordAuthenticationToken(datosAutenticacionUsuario.email, datosAutenticacionUsuario.secret)
      // This method, internally calls the loadUserByUsername method from AutenticacionService
      // or throw AuthenticationException
      val usuarioAutenticado = authenticationManager.authenticate(authToken)
      val jwtToken = tokenService.generateToken(usuarioAutenticado.principal as User)

      ResponseEntity.ok(DataJwtToken(jwtToken))
    } catch (e: AuthenticationException) {
      ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.message)
    }
}
