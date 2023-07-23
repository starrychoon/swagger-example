package org.example.swagger.user.presentation.response

import com.epages.restdocs.apispec.FieldDescriptors
import com.epages.restdocs.apispec.Schema
import org.example.swagger.common.ResourceType
import org.example.swagger.support.restdocs.apispec.enumType
import org.example.swagger.support.restdocs.apispec.length
import org.example.swagger.support.restdocs.apispec.min
import org.example.swagger.support.restdocs.apispec.pattern
import org.example.swagger.support.restdocs.apispec.required
import org.example.swagger.support.restdocs.apispec.withPathPrefix
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath

/**
 * @author starrychoon
 */
fun UserResponse.Companion.fieldDescriptors(pathPrefix: String = ""): FieldDescriptors {
    return FieldDescriptors(
        fieldWithPath("id")
            .required()
            .type(JsonFieldType.NUMBER)
            .min(1)
            .description("Subject identifier(User ID)"),
        fieldWithPath("type")
            .required()
            .enumType(listOf(ResourceType.User))
            .description("Resource type(User)"),
        fieldWithPath("login")
            .required()
            .type(JsonFieldType.STRING)
            .length(1, 255)
            .description("User account"),
        fieldWithPath("name")
            .required()
            .type(JsonFieldType.STRING)
            .length(1, 255)
            .description("User name"),
        fieldWithPath("email")
            .required()
            .type(JsonFieldType.STRING)
            .description("User email"),
        fieldWithPath("created_at")
            .required()
            .type(JsonFieldType.STRING)
            .pattern("yyyy-MM-ddThh.mm.ss.SSSSSSSSSZ")
            .description("Signed up date in UTC"),
        fieldWithPath("updated_at")
            .optional()
            .type(JsonFieldType.STRING)
            .pattern("yyyy-MM-ddThh.mm.ss.SSSSSSSSSZ")
            .description("Last updated date in UTC"),
    ) withPathPrefix pathPrefix
}

fun UserResponse.Companion.schema(): Schema {
    return Schema("UserResponse")
}
