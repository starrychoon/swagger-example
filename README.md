# swagger-example

API Documentation with Spring REST Docs, Swagger UI

### Run with API Spec

```shell
./gradlew clean openapi3 npmBuild && ./gradlew bootRun
```

Then open http://localhost:8080

### Package with API Spec

```shell
./gradlew clean openapi3 npmBuild && ./gradlew bootJar
```
