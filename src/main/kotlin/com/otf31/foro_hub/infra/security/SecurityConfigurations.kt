package com.otf31.foro_hub.infra.security

import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
class SecurityConfigurations @Autowired constructor(
  private val securityFilter: SecurityFilter
) {

  @Bean
  fun securityFilterChain(
    http: HttpSecurity
  ): SecurityFilterChain =
    http
        .csrf { it
          .disable()
        }
        .cors { it
          .disable()
        }
        .sessionManagement { it
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authorizeHttpRequests { it
          .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
          .anyRequest().authenticated()
        }
        .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

  @Bean
  fun authenticationManager (
    authenticationConfiguration: AuthenticationConfiguration
  ): AuthenticationManager =
    authenticationConfiguration.authenticationManager

  @Bean
  fun passwordEncoder(): PasswordEncoder =
    BCryptPasswordEncoder()
}
