package org.example.swagger.user.command.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * @author starrychoon
 */
interface UserRepository : ReactiveCrudRepository<User, Long> {

    fun findAllByIdGreaterThan(id: Long, pageable: Pageable): Flux<User>

    fun findByLogin(login: String): Mono<User>
}
