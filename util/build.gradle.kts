plugins {
    `java-library`
}

subprojects {

    apply {
        plugin("org.springframework.boot")
    }


    if (arrayOf("excel", "wechat", "ueditor").contains(name)) {
        apply {
            plugin("summer.publish")
        }
    } else {
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin("org.jetbrains.kotlin.plugin.spring")
            plugin("summer.kotlin-publish")
        }
    }

    tasks {
        "jar"(Jar::class) {
            enabled = true
            archiveClassifier.convention("")
        }
        "bootJar" { enabled = false }
    }
}
