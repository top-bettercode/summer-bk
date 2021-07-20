package top.bettercode.logging

import top.bettercode.lang.util.LocalDateTimeHelper
import org.springframework.core.env.Environment
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 *
 * @author Peter Wu
 */
internal fun warnSubject(environment: Environment): String = environment.getProperty(
    "summer.logging.warn-subject",
    "${environment.getProperty("spring.application.name", "")}${
        if (existProperty(
                environment,
                "summer.web.project-name"
            )
        ) " " + environment.getProperty("summer.web.project-name") else ""
    } ${environment.getProperty("spring.profiles.active", "")}"
)

internal fun existProperty(environment: Environment, key: String) =
    environment.containsProperty(key) && !environment.getProperty(key).isNullOrBlank()