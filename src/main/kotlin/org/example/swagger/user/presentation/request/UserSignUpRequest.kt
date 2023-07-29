package org.example.swagger.user.presentation.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

/**
 * @author starrychoon
 */
class UserSignUpRequest(
    @field:NotEmpty
    @field:Size(max = 255)
    val username: String?,
    @field:NotEmpty
    @field:Size(max = 255)
    val name: String?,
    @field:NotEmpty
    @field:Email
    val email: String?,
) {
    companion object
}
