package com.example.demo.config

import com.example.demo.service.PodStateNotFoundException
import io.kubernetes.client.openapi.ApiException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
internal class WebExceptionHandler {
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Pod State not found")
    @ExceptionHandler(PodStateNotFoundException::class)
    fun handleUserNotFound() { /* Nothing to do here */ }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "K8s API responded with an error for this request")
    @ExceptionHandler(ApiException::class)
    fun handleK8sApiException() { /* Nothing to do here */ }


}