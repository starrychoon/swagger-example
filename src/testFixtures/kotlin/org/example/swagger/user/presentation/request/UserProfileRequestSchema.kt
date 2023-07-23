package org.example.swagger.user.presentation.request

import com.epages.restdocs.apispec.FieldDescriptors
import com.epages.restdocs.apispec.Schema
import org.example.swagger.support.restdocs.apispec.length
import org.example.swagger.support.restdocs.apispec.required
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

/**
 * @author starrychoon
 */
fun UserProfileRequest.Companion.fieldDescriptors(): FieldDescriptors {
    return FieldDescriptors(
        fieldWithPath("name")
            .required()
            .type(JsonFieldType.STRING)
            .length(1, 255)
            .description("User name"),
        fieldWithPath("email")
            .required()
            .type(JsonFieldType.STRING)
            .description("User email"),
    )
}

fun UserProfileRequest.Companion.schema(): Schema {
    return Schema("UserProfileRequest")
}
