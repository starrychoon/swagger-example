package org.example.swagger.user.presentation

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

/**
 * @author starrychoon
 */
@Configuration(proxyBeanMethods = false)
class UserRouter(
    private val userHandler: UserHandler,
) {

    @Bean
    fun routeUser() = router {
        GET("/users", userHandler::listUsers)
        POST("/users", userHandler::signUp)
        GET("/users/{username}", userHandler::findByUsername)
        POST("/users/{username}", userHandler::updateProfile)
    }
}
