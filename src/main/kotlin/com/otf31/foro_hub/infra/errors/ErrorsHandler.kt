package com.otf31.foro_hub.infra.errors

import com.auth0.jwt.exceptions.JWTVerificationException
import com.otf31.foro_hub.domain.ValidationException
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ErrorsHandler {

  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(
    e: ValidationException
  ): ResponseEntity<String> =
    ResponseEntity.badRequest().body(e.message)

  @ExceptionHandler(MethodArgumentNotValidException::class)
  fun handleError400(
    e: MethodArgumentNotValidException
  ): ResponseEntity<List<DataErrorValidation>> {
    val errores = e.fieldErrors.map { DataErrorValidation.from(it) }

    return ResponseEntity.badRequest().body(errores)
  }

  @ExceptionHandler(EntityNotFoundException::class)
  fun handleError404(
  ): ResponseEntity<String> =
    ResponseEntity.notFound().build()

  @ExceptionHandler(MethodArgumentTypeMismatchException::class)
  fun handleTypeMisMatch(
    e: MethodArgumentTypeMismatchException
  ): ResponseEntity<String> =
    ResponseEntity.badRequest().body("The IDENTIFIER ${e.name} must be a ${e.requiredType}")

  @ExceptionHandler(HttpMessageNotReadableException::class)
  fun handleHttpMessageNotReadableException(
  ): ResponseEntity<String> =
    ResponseEntity.badRequest().body("Required request body is missing or unreadable")

  @ExceptionHandler(JWTVerificationException::class)
  fun handleJWTVerificationException(e: JWTVerificationException): ResponseEntity<String> =
    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token: ${e.message}")

  @ExceptionHandler(Exception::class)
  fun handleRuntimeException(e: Exception): ResponseEntity<String> =
    ResponseEntity.internalServerError().body("An unexpected error occurred: ${e.message}")

  @JvmRecord
  data class DataErrorValidation(
    val field: String,
    val error: String?
  ) {
    companion object {
      fun from(error: FieldError): DataErrorValidation =
        DataErrorValidation(error.field, error.defaultMessage)
    }
  }
}
