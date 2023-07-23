package org.example.swagger.user.presentation

import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import org.example.swagger.user.command.application.UserProfileService
import org.example.swagger.user.presentation.request.UserProfileRequest
import org.example.swagger.user.presentation.request.UserSignUpRequest
import org.example.swagger.user.presentation.response.PublicUserResponse
import org.example.swagger.user.presentation.response.UserResponse
import org.example.swagger.user.query.UserSearchService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.json
import org.springframework.web.reactive.function.server.queryParamOrNull
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

/**
 * @author starrychoon
 */
@Component
class UserHandler(
    private val userProfileService: UserProfileService,
    private val userSearchService: UserSearchService,
    private val validator: Validator,
) {

    fun listUsers(request: ServerRequest): Mono<ServerResponse> {
        val since = request.queryParamOrNull("since")?.toLongOrNull()
        val perPage = (request.queryParamOrNull("per_page")?.toInt() ?: 30).coerceIn(1, 100)
        val users = userSearchService.listUsers(since, perPage)
            .map { PublicUserResponse.from(it) }
        return ok().json().body(users)
    }

    fun signUp(request: ServerRequest): Mono<ServerResponse> {
        val user = request.bodyToMono<UserSignUpRequest>()
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "No request body.")) }
            .flatMap {
                validate(it)
                userProfileService.signUp(it.username!!, it.name!!, it.email!!)
            }
            .map { UserResponse.from(it) }
        return ok().json().body(user)
    }

    fun findByUsername(request: ServerRequest): Mono<ServerResponse> {
        val username = request.pathVariable("username")
        val user = userSearchService.findByUsername(username)
            .map { UserResponse.from(it) }
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "User '$username' not found.")) }
        return ok().json().body(user)
    }

    fun updateProfile(request: ServerRequest): Mono<ServerResponse> {
        val username = request.pathVariable("username")
        val user = request.bodyToMono<UserProfileRequest>()
            .switchIfEmpty { Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "No request body.")) }
            .flatMap {
                validate(it)
                userProfileService.updateProfile(username, it.name!!, it.email!!)
            }
            .map { UserResponse.from(it) }
        return ok().json().body(user)
    }

    private fun <T> validate(request: T) {
        val result = validator.validate(request)
        if (result.isNotEmpty()) {
            val cause = ConstraintViolationException(result)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, cause.message, cause)
        }
    }
}
