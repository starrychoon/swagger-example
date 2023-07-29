import com.epages.restdocs.apispec.gradle.ApiSpecTask
import com.epages.restdocs.apispec.gradle.OpenApi3Task
import com.epages.restdocs.apispec.gradle.OpenApiBaseExtension
import com.epages.restdocs.apispec.model.ResourceModel
import com.github.gradle.node.npm.task.NpmTask
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.servers.Server

plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    `java-test-fixtures`

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("com.epages.restdocs-api-spec")
    id("com.github.node-gradle.node")
}

open class CustomRestdocsApiSpecPlugin : Plugin<Project> {

    private fun <T : ApiSpecTask> T.applyWithCommonConfiguration(block: T.() -> Unit): T {
        dependsOn("check")
        group = "documentation"
        block()
        return this
    }

    override fun apply(project: Project) {
        with(project) {
            extensions.create(OpenApi3CustomExtension.name, OpenApi3CustomExtension::class.java, project)

            afterEvaluate {
                val openapi3Custom = extensions.findByName(OpenApi3CustomExtension.name) as OpenApi3CustomExtension
                tasks.create<OpenApi3CustomTask>(OpenApi3CustomExtension.name).applyWithCommonConfiguration {
                    description = "Aggregate resource fragments into an OpenAPI 3 specification"
                    applyExtension(openapi3Custom)
                }
            }
        }
    }
}

open class OpenApi3CustomExtension(project: Project) : OpenApiBaseExtension(project) {

    override var outputFileNamePrefix = "openapi3"
    val outputFilePath: String
        get() = "$outputDirectory/$outputFileNamePrefix.$format"
    var specVersion: String = "3.0.1"
        set(value) {
            checkSpecVersion(value)
            field = value
        }

    private var _servers: List<Server> = mutableListOf(Server().apply { url = "http://localhost" })
    private var _contact: Contact? = null

    val servers: List<Server>
        get() = _servers

    fun setServer(serverUrl: String) {
        _servers = listOf(Server().apply { url = serverUrl })
    }

    fun setServers(vararg server: Server) {
        setServers(server.toList())
    }

    fun setServers(servers: List<Server>) {
        _servers = servers
    }

    val contact: Contact?
        get() = _contact

    fun setContact(contact: Contact) {
        _contact = contact
    }

    private fun checkSpecVersion(specVersion: String) {
        require(specVersion in VALID_SPEC_VERSIONS) { "Spec version must be one of $VALID_SPEC_VERSIONS." }
    }

    companion object {
        const val name = "openapi3Custom"
        private val VALID_SPEC_VERSIONS = setOf("3.0.1", "3.0.2", "3.0.3", "3.1.0")
    }
}

open class OpenApi3CustomTask : OpenApi3Task() {

    @Input
    @Optional
    var specVersion: String = "3.0.1"

    fun applyExtension(extension: OpenApi3CustomExtension) {
        super.applyExtension(extension)
        servers = extension.servers
        contact = extension.contact
        specVersion = extension.specVersion
    }

    override fun generateSpecification(resourceModels: List<ResourceModel>): String {
        val spec = super.generateSpecification(resourceModels)
        // restdocs-api-spec이 OpenAPI spec version 설정을 지원할 때 제거
        return spec.replaceFirst("openapi: 3.0.1", "openapi: $specVersion")
    }
}

val Project.openapi3Custom: OpenApi3CustomExtension
    get() = (this as ExtensionAware).extensions.getByName(OpenApi3CustomExtension.name) as OpenApi3CustomExtension

fun Project.openapi3Custom(configure: Action<OpenApi3CustomExtension>): Unit =
    (this as ExtensionAware).extensions.configure(OpenApi3CustomExtension.name, configure)

apply<CustomRestdocsApiSpecPlugin>()

repositories {
    mavenCentral()
}

node {
    nodeProjectDir.set(file("src/main/swagger-ui"))
}

openapi3Custom {
    title = "WebFlux APIs"
    description = "API documentation with WebFlux, RestDocs, restdocs-api-spec"
    tagDescriptionsPropertiesFile = "src/test/resources/docs/tags.yaml"
    format = "yaml"
    specVersion = "3.1.0"
    setServers(
        Server().url("/").description("relative path"),
        Server().url("http://localhost:8080").description("localhost"),
    )
    setContact(
        Contact()
            .name("starrychoon")
            .url("https://github.com/starrychoon")
            .email("starrychoon@naver.com"),
    )
}

dependencyManagement {
    val springRestdocsVersion: String by project

    imports {
        mavenBom("org.springframework.restdocs:spring-restdocs-bom:$springRestdocsVersion")
    }
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    val arch = providers.systemProperty("os.arch").orNull
    if (arch == "aarch64") {
        runtimeOnly(group = "io.netty", name = "netty-resolver-dns-native-macos", classifier = "osx-aarch_64")
    }

    val restdocsApiSpecVersion: String by project
    testFixturesImplementation(kotlin("test"))
    testFixturesImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
    testFixturesImplementation("com.epages:restdocs-api-spec:$restdocsApiSpecVersion")

    val mockkVersion: String by project
    val springMockkVersion: String by project
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("com.epages:restdocs-api-spec:$restdocsApiSpecVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    val jdkVersion: String by project
    jvmToolchain(jdkVersion.toInt())
}

tasks {
    processResources {
        into("static/swagger-ui") {
            from("src/main/swagger-ui/dist")
            from("build/api-spec/openapi3.yaml")
        }
        // always copy files
        outputs.upToDateWhen { false }
    }

    register<NpmTask>("npmBuild") {
        args.set(listOf("run", "build"))
    }

    register<NpmTask>("npmStart") {
        args.set(listOf("start"))
    }

    test {
        useJUnitPlatform()
        outputs.doNotCacheIf("build generated-snippets for openapi task") { true }
    }
}

open class CustomRestdocsApiSpecPlugin : Plugin<Project> {

    private fun <T : ApiSpecTask> T.applyWithCommonConfiguration(block: T.() -> Unit): T {
        dependsOn("check")
        group = "documentation"
        block()
        return this
    }

    override fun apply(project: Project) {
        with(project) {
            extensions.create(OpenApi3CustomExtension.name, OpenApi3CustomExtension::class.java, project)

            afterEvaluate {
                val openapi3Custom = extensions.findByName(OpenApi3CustomExtension.name) as OpenApi3CustomExtension
                tasks.create<OpenApi3CustomTask>(OpenApi3CustomExtension.name).applyWithCommonConfiguration {
                    description = "Aggregate resource fragments into an OpenAPI 3 specification"
                    applyExtension(openapi3Custom)
                }
            }
        }
    }
}

open class OpenApi3CustomExtension(project: Project) : OpenApiBaseExtension(project) {

    override var outputFileNamePrefix = "openapi3"
    var specVersion: String = "3.0.1"
        set(value) {
            checkSpecVersion(value)
            field = value
        }

    private var _servers: List<Server> = mutableListOf(Server().apply { url = "http://localhost" })
    private var _contact: Contact? = null

    val servers: List<Server>
        get() = _servers

    fun setServer(serverUrl: String) {
        _servers = listOf(Server().apply { url = serverUrl })
    }

    fun setServers(vararg server: Server) {
        setServers(server.toList())
    }

    fun setServers(servers: List<Server>) {
        _servers = servers
    }

    val contact: Contact?
        get() = _contact

    fun setContact(contact: Contact) {
        _contact = contact
    }

    private fun checkSpecVersion(specVersion: String) {
        require(specVersion in VALID_SPEC_VERSIONS) { "Spec version must be one of $VALID_SPEC_VERSIONS." }
    }

    companion object {
        const val name = "openapi3"
        private val VALID_SPEC_VERSIONS = setOf("3.0.0", "3.0.1", "3.0.2", "3.0.3", "3.1.0")
    }
}

open class OpenApi3CustomTask : OpenApi3Task() {

    @Input
    @Optional
    var specVersion: String = "3.0.1"

    fun applyExtension(extension: OpenApi3CustomExtension) {
        super.applyExtension(extension)
        servers = extension.servers
        contact = extension.contact
        specVersion = extension.specVersion
    }

    override fun generateSpecification(resourceModels: List<ResourceModel>): String {
        val spec = super.generateSpecification(resourceModels)
        // restdocs-api-spec이 OpenAPI spec version 설정을 지원할 때 제거
        return spec.replaceFirst("openapi: 3.0.1", "openapi: $specVersion")
    }
}

val Project.openapi3: OpenApi3CustomExtension
    get() = (this as ExtensionAware).extensions.getByName(OpenApi3CustomExtension.name) as OpenApi3CustomExtension

fun Project.openapi3(configure: Action<OpenApi3CustomExtension>): Unit =
    (this as ExtensionAware).extensions.configure(OpenApi3CustomExtension.name, configure)
