package com.np.bug.config

import com.np.bug.dto.BugSubmitResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<BugSubmitResponseDto> {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(BugSubmitResponseDto(false, ex.message))
    }
}