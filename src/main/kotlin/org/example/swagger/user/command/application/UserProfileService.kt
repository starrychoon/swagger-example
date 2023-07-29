package org.example.swagger.user.command.application

import org.example.swagger.user.command.domain.User
import org.example.swagger.user.command.domain.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

/**
 * @author starrychoon
 */
@Service
class UserProfileService(
    private val userRepository: UserRepository,
    private val transactionalOperator: TransactionalOperator,
) {

    fun signUp(username: String, name: String, email: String): Mono<User> {
        return userRepository.findByLogin(username)
            .flatMap<User> {
                Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Username '$username' already in use."))
            }
            .switchIfEmpty { userRepository.save(User.signUp(username, name, email)) }
    }

    fun updateProfile(username: String, name: String, email: String): Mono<User> {
        return userRepository.findByLogin(username)
            .flatMap { userRepository.save(it.apply { updateProfile(name, email) }) }
            .`as`(transactionalOperator::transactional)
            .switchIfEmpty {
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Username '$username' not found.",
                    ),
                )
            }
    }
}
