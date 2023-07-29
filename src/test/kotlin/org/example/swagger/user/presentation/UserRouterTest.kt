package org.example.swagger.user.presentation

import com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName
import com.epages.restdocs.apispec.ResourceDocumentation.resource
import com.epages.restdocs.apispec.ResourceSnippetParameters
import com.epages.restdocs.apispec.SimpleType
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.example.swagger.user.command.application.UserProfileService
import org.example.swagger.user.command.domain.User
import org.example.swagger.user.presentation.request.UserProfileRequest
import org.example.swagger.user.presentation.request.UserSignUpRequest
import org.example.swagger.user.presentation.request.fieldDescriptors
import org.example.swagger.user.presentation.request.schema
import org.example.swagger.user.presentation.response.PublicUserResponse
import org.example.swagger.user.presentation.response.UserResponse
import org.example.swagger.user.presentation.response.fieldDescriptors
import org.example.swagger.user.presentation.response.schema
import org.example.swagger.user.query.UserSearchService
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@WebFluxTest(UserRouter::class)
@AutoConfigureRestDocs
@Import(UserHandler::class)
class UserRouterTest(
    private val webTestClient: WebTestClient,
) {

    @MockkBean
    private lateinit var userSearchService: UserSearchService

    @MockkBean
    private lateinit var userProfileService: UserProfileService

    @Test
    fun listUsers() {
        val user = User()
        every { userSearchService.listUsers(any(), any()) } returns Flux.just(user)

        webTestClient.get()
            .uri("/users")
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody().consumeWith(
                document(
                    "list-users",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tags("Users")
                            .summary("List users")
                            .description("Lists all users, in the order that they signed up on. See also [List users](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#list-users).")
                            .queryParameters(
                                parameterWithName("since")
                                    .optional()
                                    .type(SimpleType.INTEGER)
                                    .description("A user ID. Only return users with an ID greater than this ID."),
                                parameterWithName("per_page")
                                    .optional()
                                    .type(SimpleType.INTEGER)
                                    .description("The number of results per page (max 100).")
                                    .defaultValue(30),
                            )
                            .responseSchema(PublicUserResponse.schema())
                            .responseFields(PublicUserResponse.fieldDescriptors("[]."))
                            .build(),
                    ),
                ),
            )
    }

    @Test
    fun signUp() {
        // language=json
        val request = """
            {
              "username": "tester",
              "name": "Test user",
              "email": "tester@gmail.com"
            }
        """.trimIndent()
        val user = User {
            id = 2
            login = "tester"
            name = "Test user"
            email = "tester@gmail.com"
        }
        every { userProfileService.signUp(any(), any(), any()) } returns Mono.just(user)

        webTestClient.post()
            .uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody().consumeWith(
                document(
                    "sign-up",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tags("Users")
                            .summary("Sign up")
                            .description("Create a new account.")
                            .requestSchema(UserSignUpRequest.schema())
                            .requestFields(UserSignUpRequest.fieldDescriptors())
                            .responseSchema(UserResponse.schema())
                            .responseFields(UserResponse.fieldDescriptors())
                            .build(),
                    ),
                ),
            )
    }

    @Test
    fun getUser() {
        val username = "starrychoon"
        val user = User()
        every { userSearchService.findByUsername(username) } returns Mono.just(user)

        webTestClient.get()
            .uri("/users/{username}", username)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody().consumeWith(
                document(
                    "get-a-user",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tags("Users")
                            .summary("Get a user")
                            .description("Provides publicly available information about someone with a account. See also [Get a user](https://docs.github.com/en/rest/users/users?apiVersion=2022-11-28#get-a-user).")
                            .pathParameters(
                                parameterWithName("username")
                                    .type(SimpleType.STRING)
                                    .description("The handle for the user account."),
                            )
                            .responseSchema(UserResponse.schema())
                            .responseFields(UserResponse.fieldDescriptors())
                            .build(),
                    ),
                ),
            )
    }

    @Test
    fun updateProfile() {
        val username = "starrychoon"
        // language=json
        val request = """
            {
              "name": "Sungchoon Park",
              "email": "sungchoon.park@test.com"
            }
        """.trimIndent()
        val user = User {
            login = username
            name = "Sungchoon Park"
            email = "sungchoon.park@test.com"
            updatedAt = Instant.now()
        }
        every { userProfileService.updateProfile(username, any(), any()) } returns Mono.just(user)

        webTestClient.post()
            .uri("/users/{username}", username)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus().isOk
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody().consumeWith(
                document(
                    "update-profile",
                    resource(
                        ResourceSnippetParameters.builder()
                            .tags("Users")
                            .summary("Change user profile")
                            .description("Update someone's profile with account.")
                            .pathParameters(
                                parameterWithName("username")
                                    .type(SimpleType.STRING)
                                    .description("The handle for the user account."),
                            )
                            .requestSchema(UserProfileRequest.schema())
                            .requestFields(UserProfileRequest.fieldDescriptors())
                            .responseSchema(UserResponse.schema())
                            .responseFields(UserResponse.fieldDescriptors())
                            .build(),
                    ),
                ),
            )
    }
}
