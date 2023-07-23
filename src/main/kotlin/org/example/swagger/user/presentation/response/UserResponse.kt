package org.example.swagger.user.presentation.response

import org.example.swagger.common.ResourceType
import org.example.swagger.user.command.domain.User
import java.time.Instant

/**
 * @author starrychoon
 */
class UserResponse(
    val id: Long,
    val login: String,
    val name: String,
    val email: String,
    val created_at: Instant,
    val updated_at: Instant?,
) {
    val type: ResourceType
        get() = ResourceType.User

    companion object {
        fun from(user: User): UserResponse {
            return with(user) {
                UserResponse(
                    id = id,
                    login = login,
                    name = name,
                    email = email,
                    created_at = createdAt,
                    updated_at = updatedAt,
                )
            }
        }
    }
}
