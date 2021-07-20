plugins {
    `java-library`
}

dependencies {
    api(project(":util:autodoc-core"))
    api(project(":framework:web"))
    api("org.springframework.boot:spring-boot-starter-test")

    //util
    api(project(":util:api-sign"))
    api(project(":util:generator"))

    //test
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
    testImplementation("com.h2database:h2")
}