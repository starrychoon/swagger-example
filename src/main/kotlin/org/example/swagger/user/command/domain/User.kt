package org.example.swagger.user.command.domain

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

/**
 * @property id Subject identifier
 * @property login User account. Unique.
 *
 * @author starrychoon
 */
@Table(name = "user_table")
class User @PersistenceCreator constructor(
    @Id
    val id: Long,
    val login: String,
    name: String,
    email: String,
    val createdAt: Instant,
    updatedAt: Instant?,
) {
    var name: String = name
        private set

    var email: String = email
        private set

    var updatedAt: Instant? = updatedAt
        private set

    constructor(
        id: Long,
        login: String,
        name: String,
        email: String,
    ) : this(
        id = id,
        login = login,
        name = name,
        email = email,
        createdAt = Instant.now(),
        updatedAt = null,
    )

    init {
        checkLogin(login)
        checkName(name)
        checkEmail(email)
    }

    private fun checkLogin(login: String) {
        require(login.isNotBlank()) { "Username must not be empty." }
        require(login.length <= 255) { "Username must not exceed 255 characters in length." }
    }

    private fun checkName(name: String) {
        require(name.isNotBlank()) { "User name must not be empty." }
        require(name.length <= 255) { "User name must not exceed 255 characters in length." }
    }

    private fun checkEmail(email: String) {
        require(email.isNotBlank()) { "User email must not be empty." }
        require(email.length <= 320) { "User email must not exceed 320 characters in length." }
    }

    @Suppress("unused")
    private fun withId(id: Long): User {
        return User(id, login, name, email, createdAt, updatedAt)
    }

    fun updateProfile(name: String, email: String) {
        checkName(name)
        checkEmail(email)
        this.name = name
        this.email = email
        updatedAt = Instant.now()
    }

    companion object {
        fun signUp(login: String, name: String, email: String): User {
            return User(0, login, name, email)
        }
    }
}
