package org.example.swagger.user.presentation.response

import org.example.swagger.common.ResourceType
import org.example.swagger.user.command.domain.User

/**
 * @author starrychoon
 */
class PublicUserResponse(
    val id: Long,
    val login: String,
) {
    val type: ResourceType
        get() = ResourceType.User

    companion object {
        fun from(user: User): PublicUserResponse {
            return with(user) {
                PublicUserResponse(id, login)
            }
        }
    }
}
