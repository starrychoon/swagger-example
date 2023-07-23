package org.example.swagger.user.query

import org.example.swagger.user.command.domain.User
import org.example.swagger.user.command.domain.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author starrychoon
 */
@Service
class UserSearchService(
    private val userRepository: UserRepository,
) {

    fun listUsers(since: Long?, perPage: Int): Flux<User> {
        val id = since ?: 0
        val pageable = PageRequest.of(0, perPage)
        return userRepository.findAllByIdGreaterThan(id, pageable)
    }

    fun findByUsername(username: String): Mono<User> {
        return userRepository.findByLogin(username)
    }
}
