package org.example.swagger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.router
import java.net.URI

/**
 * @author starrychoon
 */
@Configuration(proxyBeanMethods = false)
class SwaggerUIConfiguration {

    @Bean
    fun indexRouter() = router {
        (path("/") or path("/index.html") or path("/swagger-ui.html")) {
            status(HttpStatus.PERMANENT_REDIRECT).location(URI.create("/swagger-ui/index.html")).build()
        }
    }
}
