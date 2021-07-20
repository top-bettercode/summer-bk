plugins {
    `java-library`
}

dependencies {
    api(project(":framework:config"))
    api(project(":util:autodoc-gen"))
    api("org.springframework.boot:spring-boot-starter-test")

    compileOnly(project(":framework:security-resource"))

    testImplementation(project(":framework:security-resource"))
    testImplementation(project(":framework:security-server"))
}

