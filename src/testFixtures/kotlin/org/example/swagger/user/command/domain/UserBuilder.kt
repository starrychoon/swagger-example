package org.example.swagger.user.command.domain

import java.time.Instant

/**
 * @author starrychoon
 */
class UserBuilder {
    var id: Long = 1
    var login: String = "starrychoon"
    var name: String = "sungchoon.park"
    var email: String = "starrychoon@naver.com"
    var createdAt: Instant = Instant.now()
    var updatedAt: Instant? = null

    fun build(): User {
        return User(id, login, name, email, createdAt, updatedAt)
    }
}

fun User(init: UserBuilder.() -> Unit = {}): User {
    val builder = UserBuilder()
    builder.init()
    return builder.build()
}
