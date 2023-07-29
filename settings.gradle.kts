rootProject.name = "swagger-ui"

pluginManagement {
    val kotlinVersion: String by settings
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val ktlintVersion: String by settings
    val restdocsApiSpecVersion: String by settings
    val nodeVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion

        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion

        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("com.epages.restdocs-api-spec") version restdocsApiSpecVersion
        id("com.github.node-gradle.node") version nodeVersion
    }
}
