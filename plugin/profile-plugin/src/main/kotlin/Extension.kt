import top.bettercode.gradle.profile.ProfileExtension
import top.bettercode.gradle.profile.configProject
import top.bettercode.gradle.profile.findActive
import org.gradle.api.Project
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.util.*

const val profilesDefaultActive: String = "default"
const val simpleProfilesActiveName: String = "P"
const val profilesActiveName: String = "profiles.active"

val Project.profileProperties: Properties
    get() {
        val props = Properties()
        props["summer.web.project-name"] = name
        val gradleProperties = rootProject.file("gradle.properties")
        if (gradleProperties.exists()) {
            props.load(gradleProperties.inputStream())
            props.keys.forEach { t ->
                val k = t as String
                if (rootProject.hasProperty(k))
                    props[k] = rootProject.properties[k]
            }
        }

        val profile = extensions.getByType(ProfileExtension::class.java)
        configProject { project ->
            val defaultConfigYmlFile =
                project.file("${profile.configDir}/$profilesDefaultActive.yml")
            val yaml = Yaml()
            if (defaultConfigYmlFile.exists()) {
                props.putAll(
                    parseYml(
                        yaml.loadAs(
                            defaultConfigYmlFile.inputStream(),
                            Map::class.java
                        )
                    )
                )
            }
            if (profilesActive != profilesDefaultActive) {
                val activeYmlFile =
                    project.file("${profile.configDir}/$profilesActive${profile.activeFileSuffix}.yml")
                if (activeYmlFile.exists()) {
                    props.putAll(
                        parseYml(
                            yaml.loadAs(
                                activeYmlFile.inputStream(),
                                Map::class.java
                            )
                        )
                    )
                }
            }
            val defaultConfigYamlFile =
                project.file("${profile.configDir}/$profilesDefaultActive.yaml")
            if (defaultConfigYamlFile.exists()) {
                props.putAll(
                    parseYml(
                        yaml.loadAs(
                            defaultConfigYamlFile.inputStream(),
                            Map::class.java
                        )
                    )
                )
            }
            if (profilesActive != profilesDefaultActive) {
                val activeYamlFile =
                    project.file("${profile.configDir}/$profilesActive${profile.activeFileSuffix}.yaml")
                if (activeYamlFile.exists()) {
                    props.putAll(
                        parseYml(
                            yaml.loadAs(
                                activeYamlFile.inputStream(),
                                Map::class.java
                            )
                        )
                    )
                }
            }
            val defaultConfigFile =
                project.file("${profile.configDir}/$profilesDefaultActive.properties")
            if (defaultConfigFile.exists()) {
                props.load(defaultConfigFile.inputStream())
            }

            if (profilesActive != profilesDefaultActive) {
                val activeFile =
                    project.file("${profile.configDir}/$profilesActive${profile.activeFileSuffix}.properties")
                if (activeFile.exists()) {
                    props.load(activeFile.inputStream())
                }
            }
        }
        if (profile.configFile.isNotBlank()) {
            val uri = uri(profile.configFile)
            if (uri.scheme.isNullOrEmpty()) {
                val configFile = File(uri)
                if (configFile.exists())
                    props.load(configFile.inputStream())
            } else {
                props.load(uri.toURL().openStream())
            }
        }

        props.putAll(System.getProperties())

        val packageName = props["app.packageName"]?.toString()
        if (packageName != null) {
            props["app.packagePath"] = packageName.replace(".", "/")
        }

        props[profilesActiveName] = profilesActive
        configProject { project ->
            props.forEach { t, u ->
                val k = t as String
                if (project.hasProperty(k)) {
                    project.setProperty(k, u)
                }
            }
        }
        return props
    }

private fun parseYml(
    map: Map<*, *>,
    result: MutableMap<Any, Any> = mutableMapOf(),
    prefix: String = ""
): MutableMap<Any, Any> {
    map.forEach { (k, u) ->
        if (u != null) {
            if (u is Map<*, *>) {
                parseYml(u, result, "$prefix$k.")
            } else {
                result["$prefix$k"] = u
            }
        }
    }
    return result
}

val Project.profilesActive: String
    get() {
        val systemProperties = System.getProperties()
        val findActive = findActive { name ->
            systemProperties.getProperty(name)
        } ?: findActive { name ->
            rootProject.findProperty(name) as? String
        }
        return if (findActive.isNullOrBlank()) {
            profilesDefaultActive
        } else
            findActive
    }

fun Project.profileClosure(closure: Project.(ProfileExtension) -> Unit) {
    val profile = project.extensions.getByType(ProfileExtension::class.java)
    profile.closure.add(closure)
}

fun Project.profileClosure(active: String, closure: Project.(ProfileExtension) -> Unit) {
    val profile = project.extensions.getByType(ProfileExtension::class.java)
    profile.profileClosure.computeIfAbsent(active) { mutableSetOf() }.add(closure)
}
