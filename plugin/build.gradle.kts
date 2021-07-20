plugins { `java-library` }
subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("summer.plugin-publish")
    }
    dependencies {
        api(gradleApi())
        api("org.jetbrains.kotlin:kotlin-stdlib")

        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    }
}
