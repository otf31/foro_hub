package com.otf31.foro_hub.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
class SecurityConfigurations {

  @Throws(Exception::class)
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
        .authorizeHttpRequests { it
          .anyRequest()
          .permitAll()
        }
        .sessionManagement { it
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
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
